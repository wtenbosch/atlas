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
package nl.overheid.aerius.wui.activity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.ResettableEventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.wui.command.HasCommandRouter;
import nl.overheid.aerius.wui.command.PlaceChangeCommand;
import nl.overheid.aerius.wui.place.ApplicationPlace;
import nl.overheid.aerius.wui.place.PlaceController;
import nl.overheid.aerius.wui.widget.HasEventBus;

public class SimpleActivityManager implements ActivityManager {
  private final ActivityManagerImplEventBinder EVENT_BINDER = GWT.create(ActivityManagerImplEventBinder.class);

  interface ActivityManagerImplEventBinder extends EventBinder<SimpleActivityManager> {}

  private final ActivityMapper mapper;
  private final PlaceController placeController;

  private AcceptsOneWidget panel;

  private final ResettableEventBus activityEventBus;

  private Activity<?> currentActivity;

  @Inject
  public SimpleActivityManager(final EventBus globalEventBus, final PlaceController placeController, final ActivityMapper mapper) {
    this.placeController = placeController;
    this.mapper = mapper;

    activityEventBus = new ResettableEventBus(globalEventBus);

    EVENT_BINDER.bindEventHandlers(this, globalEventBus);
  }

  @Override
  public void setPanel(final AcceptsOneWidget panel) {
    this.panel = panel;
  }

  @EventHandler
  public void onPlaceChangeCommand(final PlaceChangeCommand c) {
    final ApplicationPlace previousPlace = placeController.getPreviousPlace();
    final ApplicationPlace place = c.getValue();

    if (previousPlace != null && previousPlace.getClass().equals(place.getClass())) {
      return;
    }

    // Suspend previous activity
    suspendActivity(currentActivity);

    // Remove event handlers
    activityEventBus.removeHandlers();

    // Start next activity
    final Activity<?> activity = mapper.getActivity(place);
    if (activity instanceof HasEventBus) {
      ((HasEventBus) activity).setEventBus(activityEventBus);
    }

    currentActivity = activity;

    activity.onStart(panel);
    if (activity instanceof HasCommandRouter) {
      ((HasCommandRouter) activity).onStart();
    }
  }

  private static void suspendActivity(final Activity<?> currentActivity) {
    if (currentActivity == null) {
      return;
    }

    final String stop = currentActivity.mayStop();
    if (stop != null) {
      Window.alert(stop);
      return;
    }

    currentActivity.onStop();
  }
}
