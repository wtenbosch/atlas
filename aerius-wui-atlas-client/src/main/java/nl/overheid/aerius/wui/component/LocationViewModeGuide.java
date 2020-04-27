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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.wui.widget.EventComposite;

public class LocationViewModeGuide extends EventComposite {
  private static final LocationViewModeGuideUiBinder UI_BINDER = GWT.create(LocationViewModeGuideUiBinder.class);

  interface LocationViewModeGuideUiBinder extends UiBinder<Widget, LocationViewModeGuide> {}

  public LocationViewModeGuide() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }
}
