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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

public class BootstrapNotSupportedBrowser implements EntryPoint {

  private static final String ID_LOADING_HEADER = "loadingHeader";
  private static final String ID_BROWSER_NOT_SUPPORTED_HEADER = "browserNotSupportedHeader";
  
  @Override
  public void onModuleLoad() {
    RootPanel.get().add(new BootstrapLoadingView());
    DOM.getElementById(ID_LOADING_HEADER).getStyle().setDisplay(Display.NONE);
    DOM.getElementById(ID_BROWSER_NOT_SUPPORTED_HEADER).getStyle().setDisplay(Display.BLOCK);
  }

}
