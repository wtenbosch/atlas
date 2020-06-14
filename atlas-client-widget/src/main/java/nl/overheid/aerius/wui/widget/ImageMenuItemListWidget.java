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
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.wui.resources.AtlasR;
import nl.overheid.aerius.wui.util.SvgUtil;

public abstract class ImageMenuItemListWidget<P extends AbstractMenuItemPopup<T>, T> extends MenuItemListWidget<P, T> {
  private static final ImageMenuItemListWidgetUiBinder UI_BINDER = GWT.create(ImageMenuItemListWidgetUiBinder.class);

  interface ImageMenuItemListWidgetUiBinder extends UiBinder<Widget, ImageMenuItemListWidget<?, ?>> {}

  public @UiField SimplePanel imagePanel;

  public ImageMenuItemListWidget(final P popup) {
    super(popup);
  }

  public void initWidget() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @Override
  public void setSelected(final T selection) {
    super.setSelected(selection);
    if (selection == null) {
      return;
    }

    SvgUtil.I.setSvg(imagePanel, getImage(selection));
  }

  @Override
  protected void setEmpty() {
    SvgUtil.I.setSvg(imagePanel, AtlasR.images().filterIcon());
  }

  protected abstract DataResource getImage(final T selection);
}
