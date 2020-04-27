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

import com.google.inject.Inject;

import nl.overheid.aerius.wui.activity.EventActivity;
import nl.overheid.aerius.wui.atlas.ui.story.StoryView.Presenter;

public class StoryActivity extends EventActivity<Presenter, StoryView> implements Presenter {
  private final StoryCommandRouter commandRouter;

  @Inject
  public StoryActivity(final StoryView view, final StoryCommandRouter commandRouter, final StoryPlaceTracker placeTracker) {
    super(view, commandRouter, placeTracker);
    this.commandRouter = commandRouter;
  }

  @Override
  public void onStart() {}

  @Override
  public void onStop() {
    commandRouter.retire();
  }

  @Override
  public Presenter getPresenter() {
    return this;
  }
}
