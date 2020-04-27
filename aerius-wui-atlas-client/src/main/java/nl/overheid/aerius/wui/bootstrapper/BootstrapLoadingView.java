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
package nl.overheid.aerius.wui.bootstrapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;

@Singleton
public class BootstrapLoadingView extends Composite implements BootstrapView {
  private static final int MAX_HEIGHT = 244;
  private static final int MIN_HEIGHT = 197;

  private static final BootstrapLoadingViewUiBinder UI_BINDER = GWT.create(BootstrapLoadingViewUiBinder.class);

  interface BootstrapLoadingViewUiBinder extends UiBinder<Widget, BootstrapLoadingView> {}

  @UiField DivElement loadingHeader;
  @UiField ImageElement loadingElement;
  @UiField ParagraphElement loadingText;

  @UiField DivElement loginPanel;

  @UiField InputElement userField;
  @UiField InputElement passField;
  @UiField ButtonElement submitButton;

  @UiField ParagraphElement errorField;

  public BootstrapLoadingView() {
    initWidget(UI_BINDER.createAndBindUi(this));

    loginPanel.getStyle().setHeight(0, Unit.PX);

    ensureDebugId(userField, AtlasTestIDs.INPUT_USER_NAME);
    ensureDebugId(passField, AtlasTestIDs.INPUT_PASSWORD);
    ensureDebugId(submitButton, AtlasTestIDs.BUTTON_SUBMIT_LOGIN);
  }

  @Override
  public void error() {
    loginPanel.getStyle().setHeight(MAX_HEIGHT, Unit.PX);
    errorField.getStyle().setDisplay(Display.BLOCK);
  }

  public void clear() {
    loginPanel.getStyle().setHeight(MIN_HEIGHT, Unit.PX);
    errorField.getStyle().setDisplay(Display.NONE);
  }

  @Override
  public void onApplicationReady(final Runnable finish) {
    finish.run();
  }
}
