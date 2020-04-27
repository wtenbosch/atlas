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
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public abstract class CompactMenuItemListWidget<T> extends ImageMenuItemListWidget<CompactMenuItemPopup<T>, T> {
  interface CompactMenuItemListWidgetUiBinder extends UiBinder<Widget, CompactMenuItemListWidget<?>> {}

  private static final CompactMenuItemListWidgetUiBinder UI_BINDER = GWT.create(CompactMenuItemListWidgetUiBinder.class);

  public @UiField Label label;

  public CompactMenuItemListWidget(final CompactMenuItemPopup<T> popup) {
    super(popup);

    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @Override
  public void setSelected(final T selection) {
    super.setSelected(selection);

    setText(items.contains(selection) ? selection : items.iterator().next());
  }

  private void setText(final T item) {
    label.setText(getName(item));
  }
}
