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

import java.util.function.Function;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class SimplePopupControl<T> extends MaskedButton<T> {
  private static final SimplePopupControlUiBinder UI_BINDER = GWT.create(SimplePopupControlUiBinder.class);

  interface SimplePopupControlUiBinder extends UiBinder<Widget, SimplePopupControl<?>> {}

  private final AbstractMenuItemPopup<T> popup;

  private Function<T, String> nameConsumer;

  public SimplePopupControl(final T item, final AbstractMenuItemPopup<T> popup) {
    this(item, popup, v -> String.valueOf(v));
  }

  public SimplePopupControl(final T item, final AbstractMenuItemPopup<T> popup, final Function<T, String> nameConsumer) {
    super(item);
    this.popup = popup;
    this.nameConsumer = nameConsumer;

    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @Override
  protected DataResource getImage(final T option) {
    return null;
  }

  @Override
  protected String getLabel(final T option) {
    return nameConsumer.apply(option);
  }

  @Override
  protected void onSelect(final T option) {
    popup.onSelect(option);
  }
}
