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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.ResettableEventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.wui.atlas.event.ChapterSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorEvent;
import nl.overheid.aerius.wui.atlas.event.StoryFragmentChangedEvent;
import nl.overheid.aerius.wui.resources.AtlasR;
import nl.overheid.aerius.wui.widget.SwitchPanel;

public class SimpleChapterViewer extends SwitchPanel implements ChapterViewer {
  interface SimpleChapterViewerEventBinder extends EventBinder<SimpleChapterViewer> {}

  private final SimpleChapterViewerEventBinder EVENT_BINDER = GWT.create(SimpleChapterViewerEventBinder.class);

  private final Map<String, Integer> chapters = new HashMap<>();

  private MainPanelWidget activeChapter;

  @Inject private ChapterWidgetBuilder chapterWidgetBuilder;

  private ResettableEventBus eventBus;

  @Inject
  public SimpleChapterViewer() {
    addStyleName(AtlasR.css().flex());
    addStyleName(AtlasR.css().grow());
    addStyleName(AtlasR.css().overflow());
  }

  @Override
  public void clear() {
    if (activeChapter != null) {
      activeChapter.clear();
    }

    super.clear();
    eventBus.removeHandlers();
    chapters.clear();
  }

  @EventHandler
  public void onStoryFragmentChangedEvent(final StoryFragmentChangedEvent e) {
    clear();

    int i = 0;
    for (final Entry<String, Chapter> c : e.getValue().chapters().entrySet()) {
      chapters.put(c.getKey(), i++);
      addChapter(c.getValue());
    }
  }

  @EventHandler
  public void onChapterSelectionChangedEvent(final ChapterSelectionChangeEvent e) {
    // Sanity failure
    if (!chapters.containsKey(e.getValue().uid())) {
      return;
    }

    final int widgetIdx = chapters.get(e.getValue().uid());

    if (widgetIdx >= getWidgetCount()) {
      throw new IllegalStateException("Chapter count mismatch.");
    }

    showWidget(widgetIdx);

    if (activeChapter != null) {
      activeChapter.clear();
    }
    activeChapter = (MainPanelWidget) getWidget(widgetIdx);
    activeChapter.show();
  }

  @EventHandler
  public void onSelectorChangedEvent(final SelectorEvent e) {
    if (activeChapter == null) {
      return;
    }

    Scheduler.get().scheduleDeferred(() -> activeChapter.notifySelector(e.getValue()));
  }

  private void addChapter(final Chapter chapter) {
    if (chapter.getMainPanel() == null) {
      throw new IllegalStateException("No main panel in chapter: " + chapter);
    }

    final MainPanelWidget chapterWidget = new MainPanelWidget(chapterWidgetBuilder, chapter, chapter.getMainPanel());
    chapterWidget.setEventBus(eventBus);
    add(chapterWidget);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    this.eventBus = new ResettableEventBus(eventBus);
    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }
}
