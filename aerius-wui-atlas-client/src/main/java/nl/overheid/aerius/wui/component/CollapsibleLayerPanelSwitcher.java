package nl.overheid.aerius.wui.component;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.domain.IsMapCohort;
import nl.overheid.aerius.geo.event.MapEventBus;
import nl.overheid.aerius.geo.resources.GeoResources;
import nl.overheid.aerius.geo.wui.Map;
import nl.overheid.aerius.wui.atlas.command.ToggleLayerPanelCommand;
import nl.overheid.aerius.wui.atlas.daemon.config.ConfigurationDaemon;
import nl.overheid.aerius.wui.atlas.event.StoryFragmentChangedEvent;
import nl.overheid.aerius.wui.atlas.event.ToggleLayerPanelEvent;
import nl.overheid.aerius.wui.atlas.factories.LayerLegendBuilder;
import nl.overheid.aerius.wui.util.ObservingReplacementAssistant;
import nl.overheid.aerius.wui.widget.EventComposite;
import nl.overheid.aerius.wui.widget.FlipButton;
import nl.overheid.aerius.wui.widget.HasEventBus;
import nl.overheid.aerius.wui.widget.SwitchPanel;

public class CollapsibleLayerPanelSwitcher extends EventComposite implements IsMapCohort, HasEventBus {
  private static final CollapsibleLayerPanelSwitcherUiBinder UI_BINDER = GWT.create(CollapsibleLayerPanelSwitcherUiBinder.class);

  interface CollapsibleLayerPanelSwitcherUiBinder extends UiBinder<Widget, CollapsibleLayerPanelSwitcher> {}

  interface LayerPanelSwitcherEventBinder extends EventBinder<CollapsibleLayerPanelSwitcher> {}

  private final LayerPanelSwitcherEventBinder EVENT_BINDER = GWT.create(LayerPanelSwitcherEventBinder.class);

  private final java.util.Map<String, Integer> panels = new HashMap<>();

  private final GeoResources geoResources;
  private final LayerLegendBuilder legendFactory;

  private final ObservingReplacementAssistant replacer;

  @UiField(provided = true) FlipButton flipButton;
  @UiField SwitchPanel switchPanel;

  @UiField Label label;

  private final ConfigurationDaemon daemon;

  @Inject
  public CollapsibleLayerPanelSwitcher(final LayerLegendBuilder legendFactory, final GeoResources geoResources,
      final ObservingReplacementAssistant replacer, final ConfigurationDaemon daemon) {
    this.legendFactory = legendFactory;
    this.geoResources = geoResources;
    this.replacer = replacer;
    this.daemon = daemon;
    this.flipButton = new FlipButton(v -> {
      switchPanel.setVisible(v);
      eventBus.fireEvent(new ToggleLayerPanelEvent(v));
    });

    initWidget(UI_BINDER.createAndBindUi(this));

    label.addClickHandler(v -> flipButton.toggle());
  }

  @Override
  public void notifyMap(final Map map, final MapEventBus mapEventBus) {
    final String uniqueId = map.getUniqueId();

    if (!panels.containsKey(uniqueId)) {
      initializeLayerPanel(uniqueId, map);
    }

    switchPanel.showWidget(panels.get(uniqueId));
  }

  @EventHandler
  public void onToggleLayerPanelCommand(final ToggleLayerPanelCommand c) {
    c.silence();

    Scheduler.get().scheduleDeferred(() -> flipButton.setState(!c.getValue()));
  }

  @EventHandler
  public void onStoryFragmentChangedEvent(final StoryFragmentChangedEvent e) {
    reset();
  }

  private void reset() {
    panels.clear();
    switchPanel.clear();
  }

  private void initializeLayerPanel(final String uniqueId, final Map map) {
    final LayerPanel panel = new LayerPanel(legendFactory, geoResources, replacer, daemon);
    panel.setAnimate(false);
    panel.setEventBus(eventBus);
    map.registerEventCohort(panel);

    panels.put(uniqueId, switchPanel.getWidgetCount());
    switchPanel.add(panel);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
