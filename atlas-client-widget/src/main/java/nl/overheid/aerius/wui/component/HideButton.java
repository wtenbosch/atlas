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
package nl.overheid.aerius.wui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.resources.AtlasR;
import nl.overheid.aerius.wui.util.SvgUtil;
import nl.overheid.aerius.wui.widget.EventComposite;

public class HideButton extends EventComposite {
  private static final HideButtonUiBinder UI_BINDER = GWT.create(HideButtonUiBinder.class);

  interface HideButtonUiBinder extends UiBinder<Widget, HideButton> {}

  @UiField FlowPanel control;
  @UiField SimplePanel button;

  public HideButton() {
    initWidget(UI_BINDER.createAndBindUi(this));
    ensureDebugId(AtlasTestIDs.HIDE_BUTTON);
  }

  @Override
  protected void initWidget(final Widget widget) {
    super.initWidget(widget);

    final DataResource image = getImage();
    if (image == null) {
      button.removeFromParent();
    } else {
      SvgUtil.I.setSvg(button, image);
    }

    control.addDomHandler(event -> onSelect(), ClickEvent.getType());
    control.addDomHandler(event -> onHover(), MouseOverEvent.getType());
  }

  protected DataResource getImage() {
    return AtlasR.images().additionalNavigationClose();
  }

  protected void onSelect() {}

  protected void onHover() {}
}
