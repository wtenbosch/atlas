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
package nl.overheid.aerius.wui.atlas.ui.story;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.event.InfoLocationChangeEvent;
import nl.overheid.aerius.wui.atlas.place.StoryPlace;
import nl.overheid.aerius.wui.domain.story.StoryContext;
import nl.overheid.aerius.wui.event.BasicEventComponent;
import nl.overheid.aerius.wui.place.ApplicationPlace;
import nl.overheid.aerius.wui.place.PlaceController;
import nl.overheid.aerius.wui.util.FilterAssistant;

/**
 * PlaceTracker for StoryActivity, responsible for keeping the Place and the
 * present context in sync.
 */
public class GeoStoryPlaceTrackerImpl extends BasicEventComponent implements StoryPlaceTracker {
  interface StoryPlaceTrackerImplEventBinder extends EventBinder<GeoStoryPlaceTrackerImpl> {}

  private final StoryPlaceTrackerImplEventBinder EVENT_BINDER = GWT.create(StoryPlaceTrackerImplEventBinder.class);

  @Inject FilterAssistant filterAssistant;
  @Inject PlaceController placeController;
  @Inject StoryContext context;

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);
    bindEventHandlers(this, EVENT_BINDER);
  }

  @EventHandler
  public void onReceptorLocationChangeEvent(final InfoLocationChangeEvent e) {
    final StoryPlace place = getStoryPlace(placeController.getPlace());
    if (place.getReceptorId() != null && String.valueOf(place.getReceptorId()).equals(String.valueOf(e.getValue().getId()))) {
      return;
    }

    place.setReceptorId(String.valueOf(e.getValue().getId()));
    placeController.goTo(place);
  }

  private StoryPlace getStoryPlace(final ApplicationPlace place) {
    if (place instanceof StoryPlace) {
      return (StoryPlace) place;
    } else {
      throw new RuntimeException("Unreachable state attained. PlaceChangeEvent of type " + place.getClass().getSimpleName()
          + " received while not supposed to be able to receive this type.");
    }
  }
}
