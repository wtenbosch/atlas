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
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class VerticalMenuItemPopup<T> extends AbstractMenuItemPopup<T> {
  private static final SimpleMenuItemPopupUiBinder UI_BINDER = GWT.create(SimpleMenuItemPopupUiBinder.class);

  interface SimpleMenuItemPopupUiBinder extends UiBinder<Widget, VerticalMenuItemPopup<?>> {}

  @UiField Label title;
  @UiField Label description;

  private HandlerRegistration resizeHandler;
  private Function<T, String> nameConsumer;

  public VerticalMenuItemPopup() {
    this("", "");
  }

  public VerticalMenuItemPopup(final Function<T, String> nameConsumer) {
    this("", "", nameConsumer);
  }

  public VerticalMenuItemPopup(final String titleTxt, final String descriptionTxt) {
    this(titleTxt, descriptionTxt, v -> String.valueOf(v));
  }

  public VerticalMenuItemPopup(final String titleTxt, final String descriptionTxt, final Function<T, String> nameConsumer) {
    super();

    setWidget(UI_BINDER.createAndBindUi(this));

    setTitleText(titleTxt);
    setDescription(descriptionTxt);
    this.nameConsumer = nameConsumer;
  }

  @Override
  protected void config() {
    super.config();
    setAnimationEnabled(true);
    setAnimationType(AnimationType.ROLL_RIGHT);
  }

  @Override
  public void show() {
    resizeHandler = Window.addResizeHandler(e -> setPopupHeight(e.getHeight()));
    setPopupHeight(Window.getClientHeight());

    setPopupPosition(parent.getAbsoluteLeft() + parent.getOffsetWidth(), 0);

    super.show();
  }

  @Override
  public void hide() {
    super.hide();

    if (resizeHandler != null) {
      resizeHandler.removeHandler();
      resizeHandler = null;
    }
  }

  private void setPopupHeight(final int height) {
    getWidget().getElement().getStyle().setHeight(height, Unit.PX);
  }

  public void setTitleText(final String titleTxt) {
    title.setVisible(titleTxt != null && !titleTxt.isEmpty());
    title.setText(titleTxt);
  }

  public void setDescription(final String descriptionTxt) {
    description.setVisible(descriptionTxt != null && !descriptionTxt.isEmpty());
    description.setText(descriptionTxt);
  }

  @Override
  protected MaskedButton<T> createMaskedButton(final T item) {
    return new SimplePopupControl<T>(item, this, nameConsumer);
  }
}
