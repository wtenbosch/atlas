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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.wui.atlas.command.CompactModeActivationCommand;
import nl.overheid.aerius.wui.atlas.command.CompactModeDeactivationCommand;
import nl.overheid.aerius.wui.atlas.event.MapActiveEvent;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.component.AdditionalNavigation;
import nl.overheid.aerius.wui.component.ChapterNavigationPanel;
import nl.overheid.aerius.wui.component.CompactAdditionalNavigation;
import nl.overheid.aerius.wui.component.ContentView;
import nl.overheid.aerius.wui.component.ContextViewImpl;
import nl.overheid.aerius.wui.component.PanelCollapseButton;
import nl.overheid.aerius.wui.widget.SwitchPanel;
import nl.overheid.aerius.wui.widget.ViewComposite;

public class StoryViewImpl extends ViewComposite<StoryPresenter> implements StoryView {
  private static final StoryViewImplUiBinder UI_BINDER = GWT.create(StoryViewImplUiBinder.class);

  interface StoryViewImplUiBinder extends UiBinder<Widget, StoryViewImpl> {}

  private static final StoryViewImplEventBinder EVENT_BINDER = GWT.create(StoryViewImplEventBinder.class);

  interface StoryViewImplEventBinder extends EventBinder<StoryViewImpl> {}

  public static interface CustomStyle extends CssResource {
    String mapAbsolute();

    String compact();
  }

  @UiField CustomStyle style;

  @UiField FlowPanel container;
  @UiField SwitchPanel navigationSwitcher;

  @UiField PanelCollapseButton collapseButton;

  @UiField(provided = true) AdditionalNavigation additionalNavigation;
  @UiField(provided = true) CompactAdditionalNavigation compactAdditionalNavigation;
  @UiField(provided = true) ContextViewImpl contextView;

  @UiField(provided = true) ChapterNavigationPanel chapterNavigation;

  @UiField(provided = true) ContentView contentView;

  @Inject
  public StoryViewImpl(final AdditionalNavigation additionalNavigation,
      final CompactAdditionalNavigation compactAdditionalNavigation,
      final ContextViewImpl contextView,
      final ChapterNavigationPanel chapterNavigation,
      final ContentView contentView) {
    this.additionalNavigation = additionalNavigation;
    this.compactAdditionalNavigation = compactAdditionalNavigation;
    this.contextView = contextView;
    this.chapterNavigation = chapterNavigation;
    this.contentView = contentView;

    initWidget(UI_BINDER.createAndBindUi(this));

    navigationSwitcher.showWidget(0);
    collapseButton.ensureDebugId(AtlasTestIDs.BUTTON_COLLAPSE);
  }

  @EventHandler
  public void onCompactModeActivationCommand(final CompactModeActivationCommand c) {
    container.setStyleName(style.compact(), true);
    navigationSwitcher.showWidget(1);
  }

  @EventHandler
  public void onCompactModeDeactivationCommand(final CompactModeDeactivationCommand c) {
    container.setStyleName(style.compact(), false);
    navigationSwitcher.showWidget(0);
  }

  @EventHandler
  public void onMapActiveEvent(final MapActiveEvent e) {
    contentView.setStyleName(style.mapAbsolute(), e.getValue());
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER, contextView, additionalNavigation, compactAdditionalNavigation, chapterNavigation, contentView,
        collapseButton);
  }
}
