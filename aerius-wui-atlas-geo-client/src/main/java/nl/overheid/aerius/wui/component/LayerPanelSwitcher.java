package nl.overheid.aerius.wui.component;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.domain.IsMapCohort;
import nl.overheid.aerius.geo.event.MapEventBus;
import nl.overheid.aerius.geo.resources.GeoResources;
import nl.overheid.aerius.geo.wui.Map;
import nl.overheid.aerius.wui.atlas.daemon.config.ConfigurationDaemon;
import nl.overheid.aerius.wui.atlas.event.StorySelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.factories.LayerLegendBuilder;
import nl.overheid.aerius.wui.util.ObservingReplacementAssistant;
import nl.overheid.aerius.wui.widget.HasEventBus;
import nl.overheid.aerius.wui.widget.SwitchPanel;

public class LayerPanelSwitcher extends SwitchPanel implements IsMapCohort, HasEventBus {
  interface LayerPanelSwitcherEventBinder extends EventBinder<LayerPanelSwitcher> {}

  private final LayerPanelSwitcherEventBinder EVENT_BINDER = GWT.create(LayerPanelSwitcherEventBinder.class);

  private final java.util.Map<String, Integer> panels = new HashMap<>();

  private final GeoResources geoResources;
  private final LayerLegendBuilder legendFactory;

  private EventBus eventBus;

  private final ObservingReplacementAssistant replacer;
  private final ConfigurationDaemon daemon;

  @Inject
  public LayerPanelSwitcher(final LayerLegendBuilder legendFactory, final GeoResources geoResources, final ObservingReplacementAssistant replacer,
      final ConfigurationDaemon daemon) {
    this.legendFactory = legendFactory;
    this.geoResources = geoResources;
    this.replacer = replacer;
    this.daemon = daemon;
  }

  @Override
  public void notifyMap(final Map map, final MapEventBus mapEventBus) {
    final String uniqueId = map.getUniqueId();

    if (!panels.containsKey(uniqueId)) {
      initializeLayerPanel(uniqueId, map);
    }

    showWidget(panels.get(uniqueId));
  }

  @EventHandler
  public void onStoryChangeEvent(final StorySelectionChangeEvent e) {
    reset();
  }

  private void reset() {
    panels.clear();
    clear();
  }

  private void initializeLayerPanel(final String uniqueId, final Map map) {
    final LayerPanel panel = new LayerPanel(legendFactory, geoResources, replacer, daemon);
    panel.setAnimate(false);
    panel.setEventBus(eventBus);
    map.registerEventCohort(panel);

    panels.put(uniqueId, getWidgetCount());
    add(panel);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    this.eventBus = eventBus;

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }
}
