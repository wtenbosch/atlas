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
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class CompactMenuItemPopup<T> extends AbstractMenuItemPopup<T> {
  private static final SimpleMenuItemPopupUiBinder UI_BINDER = GWT.create(SimpleMenuItemPopupUiBinder.class);

  interface SimpleMenuItemPopupUiBinder extends UiBinder<Widget, CompactMenuItemPopup<?>> {}

  public CompactMenuItemPopup() {
    setWidget(UI_BINDER.createAndBindUi(this));
  }

  @Override
  protected void config() {
    super.config();
    setAnimationEnabled(true);
    setAnimationType(AnimationType.ROLL_DOWN);
  }

  @Override
  public void show() {
    super.show();

    setPopupPosition(parent.getAbsoluteLeft(), parent.getAbsoluteTop() + parent.getOffsetHeight());
    getElement().getStyle().setProperty("minWidth", parent.getOffsetWidth(), Unit.PX);
  }

  @Override
  protected MaskedButton<T> createMaskedButton(final T item) {
    return new SimplePopupControl<T>(item, this);
  }
}
