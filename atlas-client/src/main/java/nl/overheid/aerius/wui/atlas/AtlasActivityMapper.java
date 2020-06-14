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
package nl.overheid.aerius.wui.atlas;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import nl.overheid.aerius.wui.activity.Activity;
import nl.overheid.aerius.wui.activity.ActivityMapper;
import nl.overheid.aerius.wui.atlas.factories.AtlasActivityFactory;
import nl.overheid.aerius.wui.atlas.place.MonitorStoryPlace;
import nl.overheid.aerius.wui.place.ApplicationPlace;
import nl.overheid.aerius.wui.place.DefaultPlace;
import nl.overheid.aerius.wui.place.Place;

public class AtlasActivityMapper<F extends AtlasActivityFactory> implements ActivityMapper<AcceptsOneWidget> {
  protected final F factory;

  @Inject
  public AtlasActivityMapper(@DefaultPlace final ApplicationPlace place, final F factory) {
    this.factory = factory;
  }

  @Override
  public Activity<?, AcceptsOneWidget> getActivity(final Place place) {
    Activity<?, AcceptsOneWidget> presenter = null;

    presenter = tryGetActivity(place);

    if (presenter == null) {
      throw new RuntimeException("Presenter is null: Place ends up nowhere: " + place);
    }

    return presenter;
  }

  protected Activity<?, AcceptsOneWidget> tryGetActivity(final Place place) {
    if (place instanceof MonitorStoryPlace) {
      return getStoryActivity(place);
    } else {
      return null;
    }
  }

  protected Activity<?, AcceptsOneWidget> getStoryActivity(final Place place) {
    return factory.createStoryPresenter((MonitorStoryPlace) place);
  }
}
