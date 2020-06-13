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

import com.google.gwt.resources.client.DataResource;

import nl.overheid.aerius.wui.atlas.command.LegendDisplayCommand;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.atlas.util.HoverSelectionUtil;
import nl.overheid.aerius.wui.i18n.AtlasM;
import nl.overheid.aerius.wui.resources.R;

public class LegendButton extends SVGButton {
  @Override
  protected DataResource getImage() {
    return R.images().additionalNavigationLegend();
  }

  @Override
  protected void onEnsureDebugId(final String baseID) {
    super.onEnsureDebugId(baseID);
    button.ensureDebugId(baseID + "_" + AtlasTestIDs.BUTTON_LEGEND);
  }

  @Override
  protected void onSelect() {
    eventBus.fireEvent(new LegendDisplayCommand());
  }

  @Override
  protected void onHover() {
    HoverSelectionUtil.displayBottom(this, AtlasM.messages().legendTitle());
  }
}
