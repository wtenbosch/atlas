package nl.overheid.aerius.wui.component;

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
import nl.overheid.aerius.geo.wui.Map;
import nl.overheid.aerius.wui.atlas.command.ToggleLayerPanelCommand;
import nl.overheid.aerius.wui.atlas.event.ToggleLayerPanelEvent;
import nl.overheid.aerius.wui.widget.EventComposite;
import nl.overheid.aerius.wui.widget.FlipButton;

public class LayerPanelView extends EventComposite implements IsMapCohort {
  private static final LayerPanelViewUiBinder UI_BINDER = GWT.create(LayerPanelViewUiBinder.class);

  interface LayerPanelViewUiBinder extends UiBinder<Widget, LayerPanelView> {}

  interface LayerPanelViewEventBinder extends EventBinder<LayerPanelView> {}

  private final LayerPanelViewEventBinder EVENT_BINDER = GWT.create(LayerPanelViewEventBinder.class);

  @UiField(provided = true) FlipButton flipButton;
  @UiField(provided = true) LayerPanel layerPanel;

  @UiField Label label;

  @Inject
  public LayerPanelView(final LayerPanel layerPanel) {
    this.layerPanel = layerPanel;
    this.flipButton = new FlipButton(v -> {
      layerPanel.setVisible(v);
      eventBus.fireEvent(new ToggleLayerPanelEvent(v));
    });

    initWidget(UI_BINDER.createAndBindUi(this));

    label.addClickHandler(v -> flipButton.toggle());
  }

  @EventHandler
  public void onToggleLayerPanelCommand(final ToggleLayerPanelCommand c) {
    c.silence();

    Scheduler.get().scheduleDeferred(() -> flipButton.setState(!c.getValue()));
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    if (eventBus == null) {
      return;
    }

    super.setEventBus(eventBus, this, EVENT_BINDER, layerPanel);

    flipButton.init();
  }

  @Override
  public void notifyMap(final Map map, final MapEventBus mapEventBus) {
    layerPanel.notifyMap(map, mapEventBus);
  }
}
