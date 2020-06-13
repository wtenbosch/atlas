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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public abstract class HorizontalMenuItemListWidget<T> extends AbstractMenuItemListWidget<HorizontalMenuItemPopup<T>, T> {
  interface HorizontalMenuItemListWidgetUiBinder extends UiBinder<Widget, HorizontalMenuItemListWidget<?>> {}

  private static final HorizontalMenuItemListWidgetUiBinder UI_BINDER = GWT.create(HorizontalMenuItemListWidgetUiBinder.class);

  public HorizontalMenuItemListWidget() {
    super();
  }

  public HorizontalMenuItemListWidget(final String name, final String description) {
    super(name, description);
  }

  public HorizontalMenuItemListWidget(final HorizontalMenuItemPopup<T> popup) {
    super(popup);
  }

  @Override
  protected Widget createWidget() {
    return UI_BINDER.createAndBindUi(this);
  }

  @Override
  protected HorizontalMenuItemPopup<T> createMenuPopup() {
    return new HorizontalMenuItemPopup<T>();
  }
}
