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

import nl.overheid.aerius.shared.domain.Selector;

public class SelectorPopupControl<T extends Selector> extends SimplePopupControl<T> {
  public SelectorPopupControl(final T item, final AbstractMenuItemPopup<T> popup) {
    super(item, popup);
  }

  @Override
  protected String getLabel(final T option) {
    return option.getValue().orElse("");
  }
}
