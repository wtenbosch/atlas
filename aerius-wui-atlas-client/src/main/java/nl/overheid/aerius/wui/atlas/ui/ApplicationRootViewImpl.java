/*
 * Copyright Dutch Ministry of Agriculture, Nature and Food Quality
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package nl.overheid.aerius.wui.atlas.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.debug.client.DebugInfo;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.wui.ApplicationRootView;
import nl.overheid.aerius.wui.atlas.command.HealthcheckRequestCommand;
import nl.overheid.aerius.wui.atlas.event.AdblockerDetectedEvent;
import nl.overheid.aerius.wui.atlas.event.RequestClientLoadFailureEvent;
import nl.overheid.aerius.wui.atlas.event.RequestConnectionFailureEvent;
import nl.overheid.aerius.wui.atlas.event.RequestServerLoadFailureEvent;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.atlas.ui.login.MonitorUpEditorPanel;
import nl.overheid.aerius.wui.widget.EventComposite;
import nl.overheid.aerius.wui.widget.NotificationPanel;

public class ApplicationRootViewImpl extends EventComposite implements ApplicationRootView {
  private static final ApplicationRootViewImplUiBinder UI_BINDER = GWT.create(ApplicationRootViewImplUiBinder.class);

  interface ApplicationRootViewImplUiBinder extends UiBinder<Widget, ApplicationRootViewImpl> {}

  interface ApplicationRootViewImplEventBinder extends EventBinder<ApplicationRootViewImpl> {}

  private final ApplicationRootViewImplEventBinder EVENT_BINDER = GWT.create(ApplicationRootViewImplEventBinder.class);

  @UiField FlowPanel adblockerWarning;
  @UiField FlowPanel serverLoadFailureWarning;
  @UiField FlowPanel clientLoadFailureWarning;
  @UiField FlowPanel connectionFailureWarning;

  @UiField SimplePanel contentPanel;

  @UiField(provided = true) MonitorUpEditorPanel loginPanel;
  @UiField NotificationPanel notificationPanel;

  @Inject
  public ApplicationRootViewImpl(final MonitorUpEditorPanel loginPanel) {
    this.loginPanel = loginPanel;

    initWidget(UI_BINDER.createAndBindUi(this));
    DebugInfo.setDebugIdPrefix(AtlasTestIDs.TEST_PREFIX);
    contentPanel.ensureDebugId(AtlasTestIDs.CONTENT_PANEL);
    notificationPanel.ensureDebugId(AtlasTestIDs.NOTIFICATION_PANEL);
  }

  @UiHandler(value = { "refresh1", "refresh2", "refresh3", "refresh4" })
  public void onRefreshButton(final ClickEvent e) {
    eventBus.fireEvent(new HealthcheckRequestCommand());
    adblockerWarning.setVisible(false);
    serverLoadFailureWarning.setVisible(false);
    clientLoadFailureWarning.setVisible(false);
    connectionFailureWarning.setVisible(false);
  }

  @EventHandler
  public void onAdblockerDetectedEvent(final AdblockerDetectedEvent e) {
    adblockerWarning.setVisible(e.getValue());
  }

  @EventHandler
  public void onRequestClientLoadFailureEvent(final RequestClientLoadFailureEvent e) {
    clientLoadFailureWarning.setVisible(e.getValue());
  }

  @EventHandler
  public void onRequestServerLoadFailureEvent(final RequestServerLoadFailureEvent e) {
    serverLoadFailureWarning.setVisible(e.getValue());
  }

  @EventHandler
  public void onRequestConnectionFailureEvent(final RequestConnectionFailureEvent e) {
    connectionFailureWarning.setVisible(e.getValue());
  }

  @Override
  public void setWidget(final IsWidget w) {
    contentPanel.setWidget(w);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER, notificationPanel, loginPanel);
  };
}
