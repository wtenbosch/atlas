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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.wui.util.Notification;

public class NotificationWidget extends Composite {
  private static final NotificationWidgetUiBinder UI_BINDER = GWT.create(NotificationWidgetUiBinder.class);

  interface NotificationWidgetUiBinder extends UiBinder<Widget, NotificationWidget> {}

  public interface CustomStyle extends CssResource {
    String error();

    String warning();
  }

  @UiField Label message;

  @UiField CustomStyle style;

  public NotificationWidget(final Notification notification) {
    initWidget(UI_BINDER.createAndBindUi(this));

    message.setText(notification.getMessage());

    setStyleName(style.error(), notification.isError());
    setStyleName(style.warning(), notification.isWarning());
  }

  @UiHandler("message")
  public void onMessageClick(final ClickEvent e) {
    removeFromParent();
  }
}
