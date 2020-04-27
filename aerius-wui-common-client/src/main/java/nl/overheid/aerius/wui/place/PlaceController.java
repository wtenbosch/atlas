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
package nl.overheid.aerius.wui.place;

import com.google.inject.ImplementedBy;

@ImplementedBy(PlaceControllerImpl.class)
public interface PlaceController {
  ApplicationPlace getPreviousPlace();

  ApplicationPlace getPlace();

  void goTo(ApplicationPlace place);

  void goTo(ApplicationPlace place, boolean silent);

  void goToForced(ApplicationPlace place);
}
