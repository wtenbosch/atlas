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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.wui.atlas.command.NoStoryCommand;
import nl.overheid.aerius.wui.atlas.event.ChapterSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.event.StoryFragmentChangedEvent;
import nl.overheid.aerius.wui.widget.HasEventBus;
import nl.overheid.aerius.wui.widget.MaskedButtonPanel;

public class ChapterNavigation extends MaskedButtonPanel<Chapter, ChapterNavigationControl> implements HasEventBus {
  private final ChapterNavigationEventBinder EVENT_BINDER = GWT.create(ChapterNavigationEventBinder.class);

  interface ChapterNavigationEventBinder extends EventBinder<ChapterNavigation> {}

  private EventBus eventBus;

  @Override
  protected Object getControlKey(final Chapter option) {
    return option == null ? null : option.uid();
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    this.eventBus = eventBus;

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @EventHandler
  public void onChapterSelectionChangeEvent(final ChapterSelectionChangeEvent e) {
    selectOption(e.getValue());
  }

  @EventHandler
  public void onNoStoryCommand(final NoStoryCommand c) {
    setControls(new ArrayList<>());
    reset();
  }

  @EventHandler
  public void onOptionSelectionChanged(final StoryFragmentChangedEvent e) {
    final Map<String, Chapter> map = e.getValue().chapters();

    final ArrayList<Chapter> lst = new ArrayList<>(map.values());
    Collections.sort(lst);

    setControls(lst);
    reset();
  }

  @Override
  protected ChapterNavigationControl createControl(final Chapter chapter) {
    return new ChapterNavigationControl(chapter, eventBus);
  }
}
