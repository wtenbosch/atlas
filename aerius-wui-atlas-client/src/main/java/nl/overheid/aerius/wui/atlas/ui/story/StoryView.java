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

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import nl.overheid.aerius.wui.activity.View;
import nl.overheid.aerius.wui.atlas.ui.story.StoryView.Presenter;
import nl.overheid.aerius.wui.command.HasCommandRouter;
import nl.overheid.aerius.wui.widget.HasEventBus;

@ImplementedBy(StoryViewImpl.class)
public interface StoryView extends IsWidget, HasEventBus, View<Presenter> {
  public interface Presenter extends HasCommandRouter {

  }
}
