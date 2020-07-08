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
package nl.overheid.aerius.wui.util;

import javax.inject.Inject;

import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.wui.atlas.command.ChapterReplacementEvent;
import nl.overheid.aerius.wui.atlas.command.StoryReplacementEvent;
import nl.overheid.aerius.wui.atlas.event.ActivatorActiveEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorEvent;
import nl.overheid.aerius.wui.atlas.event.StoryLoadedEvent;

@Singleton
public class AtlasObservingReplacementAssistant extends SimpleObservingReplacementAssistant {
  @Inject
  public AtlasObservingReplacementAssistant(final ReplacementAssistant replacer, final EventBus eventBus) {
    super(replacer, eventBus);
  }

  @EventHandler
  public void onSelectorEvent(final SelectorEvent e) {
    scheduleUpdate();
  }

  @EventHandler
  public void onChapterReplacementEvent(final ChapterReplacementEvent e) {
    scheduleUpdate();
  }

  @EventHandler
  public void onStoryReplacementEvent(final StoryReplacementEvent e) {
    scheduleUpdate();
  }

  @EventHandler
  public void onStoryLoadedEvent(final StoryLoadedEvent e) {
    scheduleUpdate();
  }

  @EventHandler
  public void onActivatorChangeEvent(final ActivatorActiveEvent e) {
    scheduleUpdate();
  }
}
