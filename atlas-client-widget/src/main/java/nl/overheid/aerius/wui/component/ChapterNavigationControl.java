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

import com.google.gwt.resources.client.DataResource;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.wui.atlas.command.ChapterSelectionChangeCommand;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.atlas.util.HoverSelectionUtil;
import nl.overheid.aerius.wui.util.ChapterNavigationImageUtil;
import nl.overheid.aerius.wui.widget.SimpleMaskedButton;

public class ChapterNavigationControl extends SimpleMaskedButton<Chapter> {
  private final EventBus eventBus;

  public ChapterNavigationControl(final Chapter chapter, final EventBus eventBus) {
    super(chapter);

    this.eventBus = eventBus;
    button.ensureDebugId(AtlasTestIDs.BUTTON_CHAPTER + "-" + option.title());
  }

  @Override
  protected DataResource getImage(final Chapter chapter) {
    return ChapterNavigationImageUtil.getImageResource(chapter.icon());
  }

  @Override
  protected void onSelect(final Chapter chapter) {
    eventBus.fireEvent(new ChapterSelectionChangeCommand(chapter));
  }

  @Override
  protected void onHover(final Chapter option) {
    HoverSelectionUtil.displayRight(this, option.title());
  }
}
