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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.wui.atlas.command.CompactModeActivationCommand;
import nl.overheid.aerius.wui.atlas.command.CompactModeDeactivationCommand;
import nl.overheid.aerius.wui.atlas.command.NoStoryCommand;
import nl.overheid.aerius.wui.atlas.daemon.library.LibraryStatusChangedEvent;
import nl.overheid.aerius.wui.atlas.event.ContextPanelCollapseEvent;
import nl.overheid.aerius.wui.atlas.event.ContextPanelOpenEvent;
import nl.overheid.aerius.wui.atlas.event.StoryLoadedEvent;
import nl.overheid.aerius.wui.atlas.event.StoryLoadingEvent;
import nl.overheid.aerius.wui.widget.EventComposite;
import nl.overheid.aerius.wui.widget.SwitchPanel;

@Singleton
public class ContentViewImpl extends EventComposite implements ContentView {
  private final ContentViewEventBinder EVENT_BINDER = GWT.create(ContentViewEventBinder.class);

  interface ContentViewEventBinder extends EventBinder<ContentViewImpl> {}

  private static final ContentViewUiBinder UI_BINDER = GWT.create(ContentViewUiBinder.class);

  interface ContentViewUiBinder extends UiBinder<Widget, ContentViewImpl> {}

  @UiField SwitchPanel contentPanel;

  @UiField(provided = true) LegendViewer legendViewer;
  @UiField(provided = true) ChapterViewer chapterViewer;

  @UiField(provided = true) BigLibraryView libraryViewer;

  private boolean contextOpen = true;
  private boolean mobileMode = false;

  private Story selectedStory;

  @Inject
  public ContentViewImpl(final LegendViewer legendViewer, final ChapterViewer chapterViewer, final BigLibraryView libraryViewer) {
    this.legendViewer = legendViewer;
    this.chapterViewer = chapterViewer;
    this.libraryViewer = libraryViewer;

    initWidget(UI_BINDER.createAndBindUi(this));

    contentPanel.showWidget(0);
  }

  @EventHandler
  public void onLibraryStatusChangedEvent(final LibraryStatusChangedEvent e) {
    if (selectedStory == null) {
      return;
    }

    if (!e.getValue().uid().equals(selectedStory.uid())) {
      return;
    }

    if (e.isAvailable()) {
      contentPanel.showWidget(1);
    } else if (e.isComplete()) {
      contentPanel.showWidget(3);
    } else {
      contentPanel.showWidget(2);
    }
  }

  @EventHandler
  public void onNoStoryCommand(final NoStoryCommand e) {
    selectedStory = null;

    contentPanel.showWidget(2);
  }

  @EventHandler
  public void onContextPanelOpenEvent(final ContextPanelOpenEvent e) {
    contextOpen = true;
    setPanelOpen(!contextOpen || !mobileMode);
  }

  @EventHandler
  public void onContextPanelCollapseEvent(final ContextPanelCollapseEvent e) {
    contextOpen = false;
    setPanelOpen(!mobileMode || !contextOpen);
  }

  @EventHandler
  public void onMobileModeActivationCommand(final CompactModeActivationCommand c) {
    mobileMode = true;
    setPanelOpen(!contextOpen);
  }

  @EventHandler
  public void onMobileModeDeactivationCommand(final CompactModeDeactivationCommand c) {
    mobileMode = false;
    setPanelOpen(true);
  }

  private void setPanelOpen(final boolean open) {
    setVisible(open);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    EVENT_BINDER.bindEventHandlers(this, eventBus);

    super.setEventBus(eventBus, legendViewer, chapterViewer, libraryViewer);
  }

  @EventHandler
  public void onStoryLoadingEvent(final StoryLoadingEvent e) {
    contentPanel.showWidget(0);
  }

  @EventHandler
  public void onStoryChangedEvent(final StoryLoadedEvent e) {
    selectedStory = e.getValue();

    contentPanel.showWidget(1);
  }
}
