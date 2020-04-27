package nl.overheid.aerius.wui.atlas.service.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.geo.domain.layer.LayerConfig;
import nl.overheid.aerius.geo.domain.layer.LayerConfig.LayerType;
import nl.overheid.aerius.geo.domain.layer.LayerStyleActivator;
import nl.overheid.aerius.geo.domain.layer.WMSLayerConfig;
import nl.overheid.aerius.geo.domain.legend.ComponentLegendConfig;
import nl.overheid.aerius.geo.domain.legend.LegendConfig;
import nl.overheid.aerius.geo.domain.legend.TextLegendConfig;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson.JSONArrayHandle;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson.JSONObjectHandle;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.future.JsonAsyncCallback;

public final class LayerJsonParser implements AsyncCallback<JSONValue> {
  private final AsyncCallback<List<LayerConfig>> callback;

  public LayerJsonParser(final AsyncCallback<List<LayerConfig>> callback) {
    this.callback = callback;
  }

  @Override
  public void onSuccess(final JSONValue result) {
    parse(result);
  }

  private void parse(final JSONValue object) {
    final List<LayerConfig> lst = new ArrayList<>();

    if (object.isArray() != null) {
      lst.addAll(parseGroup(new JSONArrayHandle(object.isArray())));
    } else if (object.isObject() != null) {
      lst.add(parseLayer(new JSONObjectHandle(object.isObject())));
    }

    callback.onSuccess(lst);
  }

  private List<LayerConfig> parseGroup(final JSONArrayHandle arr) {
    final List<LayerConfig> lst = new ArrayList<>();

    for (int i = 0; i < arr.size(); i++) {
      lst.add(parseLayer(arr.getObject(i)));
    }

    return lst;
  }

  private LayerConfig parseLayer(final JSONObjectHandle object) {
    final LayerConfig conf = getTypedConfig(object);

    conf.setName(object.getString("name"));
    conf.setTitle(object.getString("title"));
    conf.setVisible(object.getBoolean("visible"));
    conf.setOpacity(object.getNumber("opacity"));

    final List<String> selectables = new ArrayList<>();
    object.getArrayOptional("selectables").ifPresent(v -> {
      for (int i = 0; i < v.size(); i++) {
        selectables.add(v.getString(i));
      }
    });
    conf.setSelectables(selectables);

    object.getObjectOptional("behaviour").ifPresent(v -> parseBehaviour(v, conf));

    object.getObjectOptional("legend").ifPresent(v -> conf.setLegend(parseLegend(v)));

    return conf;
  }

  private void parseBehaviour(final JSONObjectHandle behaviour, final LayerConfig conf) {
    behaviour.getStringOptional("bundle").ifPresent(conf::setBundleName);
    behaviour.getStringOptional("cluster").ifPresent(conf::setClusterName);
    behaviour.getStringOptional("friendly").ifPresent(conf::setFriendlyName);
  }

  private LegendConfig parseLegend(final JSONObjectHandle legendJson) {
    final LegendConfig conf;

    switch (legendJson.getString("type")) {
    case "text":
    case "TEXT":
      conf = getTextLegend(legendJson);
      break;
    case "component":
    case "COMPONENT":
      conf = getComponentLegend(legendJson);
      break;
    default:
      GWTProd.warn("Unknown legend type: " + legendJson);
      throw new RuntimeException("Unknown legend type encountered. " + legendJson.getInner());
    }

    return conf;
  }

  private ComponentLegendConfig getComponentLegend(final JSONObjectHandle legendJson) {
    final ComponentLegendConfig conf = new ComponentLegendConfig();

    final JSONObjectHandle componentJson = legendJson.getObject("component");

    conf.setComponentName(componentJson.getString("name"));
    conf.setComponentSource(componentJson.getString("source"));

    componentJson.getObjectOptional("params").ifPresent(v -> {
      final Map<String, String> parameters = new HashMap<>();
      for (final String key : v.keySet()) {
        parameters.put(key, v.getString(key));
      }
      conf.setParameters(parameters);
    });

    return conf;
  }

  private TextLegendConfig getTextLegend(final JSONObjectHandle legendJson) {
    final TextLegendConfig conf = new TextLegendConfig();

    conf.setText(legendJson.getString("text"));

    return conf;
  }

  private LayerConfig getTypedConfig(final JSONObjectHandle object) {
    LayerConfig conf;

    final LayerType layerType = LayerType.valueOf(object.getString("type"));
    switch (layerType) {
    case WMS:
      conf = getWMSConfig(object.getObject("wms"));
      break;
    default:
      throw new IllegalArgumentException("Unsupported layer type encountered. Cannot parse this type: " + layerType);
    }

    return conf;
  }

  private WMSLayerConfig getWMSConfig(final JSONObjectHandle object) {
    final WMSLayerConfig conf = new WMSLayerConfig();
    conf.setUrl(object.getString("url"));
    conf.setLayer(object.getString("layer"));
    conf.setFormat(object.getString("format"));
    conf.setStyle(object.getStringOptional("style").orElse(""));

    object.getStringOptional("cql").ifPresent(conf::setCql);
    object.getStringOptional("viewparams").ifPresent(conf::setViewParams);

    object.getObjectOptional("activators").ifPresent(activatorsJson -> {
      final List<LayerStyleActivator> activators = new ArrayList<>();

      activatorsJson.keySet().forEach(key -> {
        activatorsJson.getObjectOptional(key).ifPresent(obj -> {
          activators.add(LayerStyleActivator.create(key, obj.getStringOptional("layer").orElse(conf.getLayer()), obj.getString("style")));
        });
      });

      conf.setActivators(activators);
    });

    return conf;
  }

  @Override
  public void onFailure(final Throwable caught) {
    callback.onFailure(caught);
  }

  public static AsyncCallback<JSONValue> convert(final AsyncCallback<List<LayerConfig>> callback) {
    return new LayerJsonParser(callback);
  }

  public static RequestCallback wrap(final AsyncCallback<List<LayerConfig>> callback) {
    return JsonAsyncCallback.create(convert(callback));
  }
}
