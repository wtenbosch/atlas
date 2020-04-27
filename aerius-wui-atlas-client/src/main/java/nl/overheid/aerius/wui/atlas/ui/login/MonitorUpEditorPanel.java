package nl.overheid.aerius.wui.atlas.ui.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.wui.atlas.event.UserAuthorizationChangedEvent;
import nl.overheid.aerius.wui.command.PlaceChangeCommand;
import nl.overheid.aerius.wui.domain.auth.AuthContext;
import nl.overheid.aerius.wui.place.ApplicationPlace;
import nl.overheid.aerius.wui.widget.AnimatedFlowPanel;
import nl.overheid.aerius.wui.widget.EventComposite;

public class MonitorUpEditorPanel extends EventComposite {
  interface MonitorUpEditorPanelEventBinder extends EventBinder<MonitorUpEditorPanel> {}

  private final MonitorUpEditorPanelEventBinder EVENT_BINDER = GWT.create(MonitorUpEditorPanelEventBinder.class);

  private static final MonitorUpEditorPanelUiBinder UI_BINDER = GWT.create(MonitorUpEditorPanelUiBinder.class);

  interface MonitorUpEditorPanelUiBinder extends UiBinder<Widget, MonitorUpEditorPanel> {}

  private ApplicationPlace currentPlace;
  @UiField AnimatedFlowPanel panel;

  @UiField EditorPanel redactionPanel;

  @Inject
  public MonitorUpEditorPanel(final AuthContext context) {
    initWidget(UI_BINDER.createAndBindUi(this));

    if (context.isAuthenticated()) {
      panel.setVisible(true);
    }
  }

  @EventHandler
  public void onPlaceChangeEvent(final PlaceChangeCommand e) {
    redraw(e.getValue());
    currentPlace = e.getValue();
  }

  private void redraw(final ApplicationPlace place) {
    if (!place.isAuth()) {
      return;
    }

    if (currentPlace != null && currentPlace.getClass().getSimpleName().equals(place.getClass().getSimpleName()) && panel.isVisible()) {
      return;
    }

    if (!panel.isVisible()) {
      panel.setVisible(true);
    }
  }

  @EventHandler
  public void onUserAuthorizationChangedEvent(final UserAuthorizationChangedEvent e) {
    if (e.getValue() == null) {
      panel.setVisible(false);
      redactionPanel.setEventBus(null);
      return;
    }

    panel.setVisible(true);
    redactionPanel.setEventBus(eventBus);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }
}
