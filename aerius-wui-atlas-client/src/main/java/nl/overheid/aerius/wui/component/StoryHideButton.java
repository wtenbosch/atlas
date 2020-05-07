/*
 * Copyright Dutch Ministry of Economic Affairs
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

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

import nl.overheid.aerius.wui.atlas.command.StoryExitCommand;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.atlas.util.HoverSelectionUtil;
import nl.overheid.aerius.wui.i18n.AtlasM;

public class StoryHideButton extends HideButton {
  private Runnable killer;

  @Inject
  public StoryHideButton() {
    ensureDebugId(AtlasTestIDs.BUTTON_EXIT_STORY);
  }

  @Override
  protected void onHover() {
    killer = HoverSelectionUtil.displayRight(this, AtlasM.messages().exitStory());
  }

  @Override
  protected void onSelect() {
    if (Window.confirm(AtlasM.messages().exitStoryConfirm())) {
      eventBus.fireEvent(new StoryExitCommand());
    }

    if (killer != null) {
      killer.run();
    }
  }
}
