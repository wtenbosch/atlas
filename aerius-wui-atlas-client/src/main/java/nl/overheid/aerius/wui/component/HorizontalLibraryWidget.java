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
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.shared.domain.StoryInformation;
import nl.overheid.aerius.wui.atlas.command.NoStoryCommand;
import nl.overheid.aerius.wui.atlas.event.LibraryChangeEvent;
import nl.overheid.aerius.wui.atlas.event.StoryLoadedEvent;
import nl.overheid.aerius.wui.atlas.event.StorySelectionChangeEvent;
import nl.overheid.aerius.wui.util.StoryNavigationImageUtil;
import nl.overheid.aerius.wui.widget.HorizontalImageMenuItemListWidget;

public class HorizontalLibraryWidget extends HorizontalImageMenuItemListWidget<HorizontalLibraryMenuItemPopup, StoryInformation> {
  private static final HorizontalLibraryWidgetEventBinder EVENT_BINDER = GWT.create(HorizontalLibraryWidgetEventBinder.class);

  interface HorizontalLibraryWidgetEventBinder extends EventBinder<HorizontalLibraryWidget> {}

  private Story lastKnownStory;

  public HorizontalLibraryWidget() {
    super(new HorizontalLibraryMenuItemPopup());

    initWidget();
  }

  @EventHandler
  public void onLibraryChangeEvent(final LibraryChangeEvent e) {
    final List<StoryInformation> lst = new ArrayList<>(e.getValue().values());

    Collections.sort(lst);

    setList(lst);

    // Select if we know about a story
    setSelected(lastKnownStory == null ? null : lastKnownStory.info());
  }

  @EventHandler
  public void onNoStoryCommand(final NoStoryCommand e) {
    popup.hide();
  }

  @EventHandler
  public void onStoryLoadedEvent(final StoryLoadedEvent e) {
    lastKnownStory = e.getValue();
    trySelectStory(e.getValue());
  }

  @EventHandler
  public void onStoryChangeEvent(final StorySelectionChangeEvent e) {
    trySelectStory(e.getValue());
  }

  private void trySelectStory(final Story story) {
    if (items == null || items.isEmpty()) {
      GWT.log("TODO: Library empty or null.");
      return;
    }

    setSelected(story == null ? null : story.info());
  }

  @Override
  public void onSelect(final StoryInformation option) {
    throw new RuntimeException("This function cannot be called for this component. [Story change in CompactLibraryWidget]");
  }

  @Override
  protected String getName(final StoryInformation selection) {
    return selection.name();
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER, popup);
  }

  @Override
  protected DataResource getImage(final StoryInformation story) {
    return StoryNavigationImageUtil.getImageResource(story.icon());
  }
}
