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
package nl.overheid.aerius.wui.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.wui.event.NotificationEvent;

public class NotificationPanel extends Composite implements HasEventBus {
  private final NotificationPanelEventBinder EVENT_BINDER = GWT.create(NotificationPanelEventBinder.class);

  interface NotificationPanelEventBinder extends EventBinder<NotificationPanel> {}

  private static final NotificationPanelUiBinder UI_BINDER = GWT.create(NotificationPanelUiBinder.class);

  interface NotificationPanelUiBinder extends UiBinder<Widget, NotificationPanel> {}

  public interface CustomStyle extends CssResource {
    String hide();
  }

  @UiField CustomStyle style;

  @UiField FlowPanel container;

  private String baseID;

  public NotificationPanel() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @EventHandler
  public void onNotification(final NotificationEvent event) {
    addNotification(new NotificationWidget(event.getValue()));
  }

  private void addNotification(final NotificationWidget notificationWidget) {
    container.add(notificationWidget);

    new Timer() {
      @Override
      public void run() {
        notificationWidget.setStyleName(style.hide(), true);
      }
    }.schedule(9000);

    new Timer() {
      @Override
      public void run() {
        container.remove(notificationWidget);
      }
    }.schedule(10000);
  }
}
