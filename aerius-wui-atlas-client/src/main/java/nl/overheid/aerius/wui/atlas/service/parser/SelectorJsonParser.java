package nl.overheid.aerius.wui.atlas.service.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.shared.domain.SelectorConfiguration;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.future.JsonAsyncCallback;

public class SelectorJsonParser extends CommonJson implements AsyncCallback<JSONValue> {
  private final AsyncCallback<SelectorConfiguration> callback;
  private final String origin;

  public SelectorJsonParser(final String origin, final AsyncCallback<SelectorConfiguration> callback) {
    this.origin = origin;
    this.callback = callback;
  }

  public static AsyncCallback<JSONValue> convert(final String origin, final AsyncCallback<SelectorConfiguration> callback) {
    return new SelectorJsonParser(origin, callback);
  }

  public static RequestCallback wrap(final String origin, final AsyncCallback<SelectorConfiguration> callback) {
    return JsonAsyncCallback.create(convert(origin, callback));
  }

  @Override
  public void onSuccess(final JSONValue resultJson) {
    final SelectorConfiguration config = new SelectorConfiguration();

    final JSONObjectHandle result = new JSONObjectHandle(resultJson.isObject());

    final String type = result.getString("type");
    config.setType(type);
    config.setTitle(result.getString("title"));
    config.setDescription(result.getString("description"));

    final JSONArrayHandle optionsJson = result.getArray("options");
    final List<Selector> options = new ArrayList<>();
    for (int i = 0; i < optionsJson.size(); i++) {
      final JSONObjectHandle selectorJson = optionsJson.getObject(i);
      final Selector selector = new Selector(type, selectorJson.getString("value"), selectorJson.getString("name"));

      if (selectorJson.has("tags")) {
        final Map<String, String> tags = new HashMap<>();

        final JSONObjectHandle tagsJson = selectorJson.getObject("tags");
        for (final String key : tagsJson.keySet()) {
          final String value = tagsJson.getString(key);
          tags.put(key, value);
        }

        selector.setTags(tags);
      }

      options.add(selector);
    }



    config.setOptions(options);

    UglyBoilerPlate.guardSelectorDeprecation(origin, result);

    callback.onSuccess(config);
  }

  @Override
  public void onFailure(final Throwable caught) {
    callback.onFailure(caught);
  }
}
