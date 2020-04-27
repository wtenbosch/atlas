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

import java.util.Collection;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractMenuItemListWidget<P extends AbstractMenuItemPopup<T>, T> extends MenuItemListWidget<P, T> {
  public @UiField Label label;
  public @UiField SimplePanel arrow;

  public AbstractMenuItemListWidget() {
    setPopup(createMenuPopup());
    initWidget(createWidget());
  }

  public AbstractMenuItemListWidget(final String name, final String description) {
    setPopup(createMenuPopup());
    init(name, description);

    initWidget(createWidget());
  }

  public AbstractMenuItemListWidget(final P popup) {
    super(popup);

    initWidget(createWidget());
  }
  
  @Override
  public void setList(final Collection<T> items) {
    super.setList(items);
    
    arrow.setVisible(items != null && items.size() > 1);
  }

  protected abstract Widget createWidget();

  public void init(final String name, final String description) {
    popup.setTitleText(name);
    popup.setDescription(description);
  }

  protected abstract P createMenuPopup();

  @Override
  public void setSelected(final T selection) {
    // FIXME The reason this is needed is a race condition related to deferring -
    // fix instead of patch
    if (items == null) {
      return;
    }

    super.setSelected(selection);

    setText(items.contains(selection) ? selection : items.iterator().next());
  }

  private void setText(final T item) {
    label.setText(getName(item));
  }
}
