package nl.overheid.aerius.wui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.wui.atlas.event.LegendDisplayEvent;
import nl.overheid.aerius.wui.atlas.event.LegendHiddenEvent;
import nl.overheid.aerius.wui.widget.EventComposite;
import nl.overheid.aerius.wui.widget.SwitchPanel;

public class LegendToggleButton extends EventComposite {
  interface LegendToggleButtonEventBinder extends EventBinder<LegendToggleButton> {}

  private final LegendToggleButtonEventBinder EVENT_BINDER = GWT.create(LegendToggleButtonEventBinder.class);

  interface LegendToggleButtonUiBinder extends UiBinder<Widget, LegendToggleButton> {}

  private static final LegendToggleButtonUiBinder UI_BINDER = GWT.create(LegendToggleButtonUiBinder.class);

  @UiField SwitchPanel switchPanel;
  @UiField LegendHideButton hideButton;
  @UiField LegendButton displayButton;

  private boolean visible;

  public LegendToggleButton() {
    initWidget(UI_BINDER.createAndBindUi(this));

    switchPanel.showWidget(0);
  }

  @EventHandler
  public void onLegendDisplayEvent(final LegendDisplayEvent e) {
    switchPanel.showWidget(1);
    visible = true;
  }

  @EventHandler
  public void onLegendHiddenEvent(final LegendHiddenEvent e) {
    switchPanel.showWidget(0);
    visible = false;
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER, displayButton, hideButton);
  }

  public void setEnabled(final boolean enabled) {
    if (enabled) {
      switchPanel.showWidget(visible ? 1 : 0);
    } else {
      switchPanel.showWidget(2);
    }
  }
}
