package nl.overheid.aerius.wui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.wui.atlas.event.ContextPanelCollapseEvent;
import nl.overheid.aerius.wui.atlas.event.ContextPanelOpenEvent;
import nl.overheid.aerius.wui.widget.EventComposite;

public class PanelCollapseButton extends EventComposite {
  private static final PanelCollapseButtonUiBinder UI_BINDER = GWT.create(PanelCollapseButtonUiBinder.class);

  interface PanelCollapseButtonUiBinder extends UiBinder<Widget, PanelCollapseButton> {}

  private final PanelCollapseButtonEventBinder EVENT_BINDER = GWT.create(PanelCollapseButtonEventBinder.class);

  interface PanelCollapseButtonEventBinder extends EventBinder<PanelCollapseButton> {}

  @UiField PanelVisibilityButton visibilityButton;

  public PanelCollapseButton() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @EventHandler
  public void onContextPanelOpenEvent(final ContextPanelOpenEvent e) {
    visibilityButton.setOpen();
  }

  @EventHandler
  public void onContextPanelCollapseEvent(final ContextPanelCollapseEvent e) {
    visibilityButton.setClosed();
    visibilityButton.getElement().getStyle().setProperty("transform", "rotate(0deg)");
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER, visibilityButton);
  }
}
