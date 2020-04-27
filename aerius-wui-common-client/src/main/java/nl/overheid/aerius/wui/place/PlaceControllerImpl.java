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

import com.google.gwt.core.client.Scheduler;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.wui.command.PlaceChangeCommand;

@Singleton
public class PlaceControllerImpl implements PlaceController {
  private final EventBus eventBus;

  private ApplicationPlace previousPlace;
  private ApplicationPlace where;

  @Inject
  public PlaceControllerImpl(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public ApplicationPlace getPlace() {
    return where;
  }

  @Override
  public ApplicationPlace getPreviousPlace() {
    return previousPlace == null ? null : previousPlace;
  }

  @Override
  public void goTo(final ApplicationPlace place) {
    final ApplicationPlace current = getPlace();

    // Silent if
    final boolean silent = place != null && current != null && place.getClass().equals(current.getClass());

    goTo(place, silent);
  }

  @Override
  public void goTo(final ApplicationPlace place, final boolean silent) {
    this.previousPlace = getPlace();
    this.where = place;

    Scheduler.get().scheduleDeferred(() -> {
      final PlaceChangeCommand command = new PlaceChangeCommand(place, silent);
      eventBus.fireEvent(command);
    });
  }

  @Override
  public void goToForced(final ApplicationPlace place) {
    this.previousPlace = null;
    this.where = place;

    Scheduler.get().scheduleDeferred(() -> {
      final PlaceChangeCommand command = new PlaceChangeCommand(place, false);
      eventBus.fireEvent(command);
    });
  }
}
