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

import nl.overheid.aerius.shared.domain.StoryInformation;
import nl.overheid.aerius.wui.i18n.M;
import nl.overheid.aerius.wui.widget.MaskedButton;
import nl.overheid.aerius.wui.widget.VerticalMenuItemPopup;

public class LibraryMenuItemPopup extends VerticalMenuItemPopup<StoryInformation> {
  public LibraryMenuItemPopup() {
    super(M.messages().contextLibraryTitle(), M.messages().contextLibraryDescription());
  }

  @Override
  public Object getOptionKey(final StoryInformation obj) {
    return obj == null ? null : obj.uid();
  }

  @Override
  protected MaskedButton<StoryInformation> createMaskedButton(final StoryInformation item) {
    return new StoryNavigationControl(item, eventBus);
  }
}
