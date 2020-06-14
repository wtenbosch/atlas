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
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.resources.AtlasR;
import nl.overheid.aerius.wui.util.SvgUtil;

public class NavigationLabel extends Composite implements HasClickHandlers {
  private static final NavigationLabelUiBinder UI_BINDER = GWT.create(NavigationLabelUiBinder.class);

  interface NavigationLabelUiBinder extends UiBinder<Widget, NavigationLabel> {}

  @UiField FocusPanel backButton;

  @UiField SimplePanel flipButton;

  @UiField Label areaGroupName;

  public NavigationLabel() {
    initWidget(UI_BINDER.createAndBindUi(this));

    setText(null);

    SvgUtil.I.setSvg(flipButton, AtlasR.images().flipButton());
    backButton.ensureDebugId(AtlasTestIDs.BUTTON_BACK);
  }

  public void setText(final String text) {
    setVisible(text != null);
    areaGroupName.setText(text);
  }

  @Override
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    return backButton.addClickHandler(handler);
  }
}
