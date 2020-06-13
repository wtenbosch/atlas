package nl.overheid.aerius.wui.component;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.command.LayerHiddenCommand;
import nl.overheid.aerius.geo.command.LayerVisibleCommand;
import nl.overheid.aerius.geo.domain.IsLayer;
import nl.overheid.aerius.geo.domain.IsMapCohort;
import nl.overheid.aerius.geo.domain.LayerInfo;
import nl.overheid.aerius.geo.event.LayerAddedEvent;
import nl.overheid.aerius.geo.event.LayerHiddenEvent;
import nl.overheid.aerius.geo.event.LayerOpacityEvent;
import nl.overheid.aerius.geo.event.LayerRemovedEvent;
import nl.overheid.aerius.geo.event.LayerVisibleEvent;
import nl.overheid.aerius.geo.event.MapEventBus;
import nl.overheid.aerius.geo.resources.GeoResources;
import nl.overheid.aerius.geo.wui.Map;
import nl.overheid.aerius.wui.atlas.command.SelectionEvent;
import nl.overheid.aerius.wui.atlas.daemon.config.ConfigurationDaemon;
import nl.overheid.aerius.wui.atlas.factories.LayerLegendBuilder;
import nl.overheid.aerius.wui.util.ObservingReplacementAssistant;
import nl.overheid.aerius.wui.widget.AnimatedFlowPanel;
import nl.overheid.aerius.wui.widget.EventComposite;
import nl.overheid.aerius.wui.widget.HasEventBus;

public class LayerPanel extends EventComposite implements IsMapCohort {
  private static final LayerPanelUiBinder UI_BINDER = GWT.create(LayerPanelUiBinder.class);

  interface LayerPanelUiBinder extends UiBinder<Widget, LayerPanel> {}

  private final LayerPanelEventBinder EVENT_BINDER = GWT.create(LayerPanelEventBinder.class);

  interface LayerPanelEventBinder extends EventBinder<LayerPanel> {}

  private final LayerPanelLegacyEventBinder LEGACY_EVENT_BINDER = GWT.create(LayerPanelLegacyEventBinder.class);

  interface LayerPanelLegacyEventBinder extends EventBinder<LegacyEvents> {}

  public class LegacyEvents {
    @EventHandler
    void onSelectionEvent(final SelectionEvent e) {
      selectFriendlyName(e.getValue());
    }
  }

  private final ObservingReplacementAssistant replacer;
  private final GeoResources geoResources;
  private final LayerLegendBuilder legendFactory;

  @UiField AnimatedFlowPanel layerPanel;
  private final java.util.Map<IsLayer<?>, LayerPanelItem> layers = new HashMap<>();

  private final java.util.Map<String, IsLayer<?>> friendlies = new HashMap<>();
  private final java.util.Map<String, LayerPanelRadioItem> bundles = new HashMap<>();
  private final java.util.Map<String, IsLayer<?>> clusters = new HashMap<>();

  private EventBus legacyEventBus;
  private final LegacyEvents legacyEvents = new LegacyEvents();

  private final ConfigurationDaemon daemon;

  @Inject
  public LayerPanel(final LayerLegendBuilder legendFactory, final GeoResources geoResources,
      final ObservingReplacementAssistant replacer, final ConfigurationDaemon daemon) {
    this.legendFactory = legendFactory;
    this.geoResources = geoResources;
    this.replacer = replacer;
    this.daemon = daemon;

    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @Override
  public void setEventBus(final EventBus legacyEventBus) {
    this.legacyEventBus = legacyEventBus;
    LEGACY_EVENT_BINDER.bindEventHandlers(legacyEvents, legacyEventBus);
  }

  @EventHandler
  void onLayerVisible(final LayerVisibleEvent e) {
    setLayerVisible(e.getValue(), true);
  }

  @EventHandler
  void onLayerOpacityEvent(final LayerOpacityEvent e) {
    setLayerOpacity(e.getValue(), e.getOpacity());
  }

  @EventHandler
  void onLayerHidden(final LayerHiddenEvent e) {
    setLayerVisible(e.getValue(), false);
  }

  @EventHandler
  void onLayerAddedEvent(final LayerAddedEvent e) {
    addLayer(e.getValue());
  }

  @EventHandler
  void onLayerRemovedEvent(final LayerRemovedEvent e) {
    removeLayer(e.getValue());
  }

  private void addLayer(final IsLayer<?> isLayer) {
    isLayer.getInfo().ifPresent(info -> {
      if (info.isBundle()) {
        final String bundle = info.getBundle();
        LayerPanelRadioItem item;
        if (bundles.containsKey(bundle)) {
          item = bundles.get(bundle);
        } else {
          item = new LayerPanelRadioItem(eventBus, geoResources, replacer, legendFactory, daemon);
          item.setEventBus(legacyEventBus);
          bundles.put(bundle, item);
          addLayerItemInternal(isLayer, item);
        }
        putLayer(isLayer, item);
        item.add(isLayer);
      } else {
        addSimpleItem(isLayer, info);
      }
    });
  }

  private void putLayer(final IsLayer<?> isLayer, final LayerPanelItem item) {
    layers.put(isLayer, item);
    isLayer.getInfo()
        .flatMap(v -> v.getFriendlyName())
        .ifPresent(name -> friendlies.put(name, isLayer));
  }

  private void addLayerItemInternal(final IsLayer<?> isLayer, final LayerPanelItem item) {
    layerPanel.add(item.asWidget());
    item.setClustered(isLayer.getInfo()
        .flatMap(v -> v.getCluster()).isPresent());
  }

  private void selectFriendlyName(final String value) {
    Optional.ofNullable(friendlies.get(value))
        .ifPresent(layer -> eventBus.fireEvent(new LayerVisibleCommand(layer)));
  }

  private void addSimpleItem(final IsLayer<?> isLayer, final LayerInfo info) {
    final LayerPanelSimpleItem item = new LayerPanelSimpleItem(isLayer, eventBus, geoResources, replacer, daemon);
    item.setEventBus(legacyEventBus);

    final Widget legendWidget = legendFactory.getLegendWidget(info);
    if (legendWidget instanceof HasEventBus) {
      ((HasEventBus) legendWidget).setEventBus(legacyEventBus);
    }

    item.setLegendPanel(legendWidget);

    putLayer(isLayer, item);
    addLayerItemInternal(isLayer, item);
    layerPanel.add(item);
  }

  private void removeLayer(final IsLayer<?> isLayer) {
    removeLayerOptional(isLayer).ifPresent(v -> v.remove(isLayer));
  }

  private void setLayerVisible(final IsLayer<?> isLayer, final boolean visible) {
    getLayerOptional(isLayer).ifPresent(item -> {
      item.setLayerVisible(isLayer, visible);
      toggleCluster(isLayer, visible);
    });
  }

  private void toggleCluster(final IsLayer<?> layer, final boolean v) {
    layer.getInfo().flatMap(l -> l.getCluster())
        .ifPresent(c -> clusters.merge(c, layer, clusterMerger(v)));
  }

  private BiFunction<IsLayer<?>, IsLayer<?>, IsLayer<?>> clusterMerger(final boolean visible) {
    return (v1, v2) -> {
      if (visible) {
        eventBus.fireEvent(new LayerHiddenCommand(v1));
        return v2;
      } else {
        return v1.equals(v2) ? null : v1;
      }
    };
  }

  private void setLayerOpacity(final IsLayer<?> isLayer, final double opacity) {
    getLayerOptional(isLayer).ifPresent(v -> v.setLayerOpacity(opacity));
  }

  private Optional<LayerPanelItem> getLayerOptional(final IsLayer<?> isLayer) {
    return Optional.ofNullable(layers.get(isLayer));
  }

  private Optional<LayerPanelItem> removeLayerOptional(final IsLayer<?> isLayer) {
    return Optional.ofNullable(layers.remove(isLayer));
  }

  @Override
  public void setVisible(final boolean visible) {
    layerPanel.setVisible(visible);
  }

  public void setAnimate(final boolean animate) {
    layerPanel.setAnimate(animate);
  }

  @Override
  public void notifyMap(final Map map, final MapEventBus mapEventBus) {
    super.setEventBus(mapEventBus, this, EVENT_BINDER);
  }
}
