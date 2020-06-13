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
package nl.overheid.aerius.wui.atlas.ui.context.layer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.geo.domain.IsMapCohort;
import nl.overheid.aerius.geo.event.MapEventBus;
import nl.overheid.aerius.geo.wui.Map;
import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.wui.atlas.ui.context.info.BasicEventPanelComposite;
import nl.overheid.aerius.wui.component.LayerPanelSwitcher;
import nl.overheid.aerius.wui.domain.map.MapContext;
import nl.overheid.aerius.wui.domain.story.StoryContext;
import nl.overheid.aerius.wui.event.BundledRegistration;
import nl.overheid.aerius.wui.util.ObservingReplacementAssistant;
import nl.overheid.aerius.wui.util.SelectorWidgetUtil;
import nl.overheid.aerius.wui.util.StoryTitleProvider;
import nl.overheid.aerius.wui.widget.HeadingWidget;

public class ContextLayerViewImpl extends BasicEventPanelComposite implements ContextLayerView, IsMapCohort {
  private static final ContextLayerViewImplUiBinder UI_BINDER = GWT.create(ContextLayerViewImplUiBinder.class);

  interface ContextLayerViewImplUiBinder extends UiBinder<Widget, ContextLayerViewImpl> {}

  @UiField HeadingWidget titleField;
  @UiField HeadingWidget subtitleField;
  @UiField HeadingWidget chapterField;

  @UiField FlowPanel selectorContainer;

  @UiField(provided = true) LayerPanelSwitcher layerPanel;

  private final StoryContext storyContext;

  private EventBus wrappingEventBus;

  private final BundledRegistration registrations = new BundledRegistration();

  private final StoryTitleProvider titleProvider;
  private final ObservingReplacementAssistant observingReplacer;

  @Inject
  public ContextLayerViewImpl(final LayerPanelSwitcher layerPanel, final StoryTitleProvider titleProvider, final StoryContext storyContext,
      final MapContext mapContext, final ObservingReplacementAssistant observingReplacer) {
    this.layerPanel = layerPanel;
    this.titleProvider = titleProvider;
    this.storyContext = storyContext;
    this.observingReplacer = observingReplacer;

    initWidget(UI_BINDER.createAndBindUi(this));

    registrations.add(mapContext.registerPrimaryMapCohort(this));
  }

  @Override
  public void retire() {
    registrations.retire();
  }

  @Override
  public void setPanelContent(final PanelContent content) {
    super.setPanelContent(content);

    // Set fragment information
    final Story story = storyContext.getStory();
    observingReplacer.register(titleProvider.apply(story), txt -> titleField.setText(txt));
    subtitleField.setText(story.info().name());
    final Chapter chapter = storyContext.getChapter();
    chapterField.setText(chapter.title());

    // Clear existing content
    selectorContainer.clear();

    // If no new content, go away
    if (content == null) {
      return;
    }

    if (content.selectables() == null) {
      return;
    }

    SelectorWidgetUtil.populateSelectorWidget(selectorContainer, content.selectables(), wrappingEventBus);
  }

  @Override
  public void show() {

  }

  @Override
  public void notifyMap(final Map map, final MapEventBus mapEventBus) {
    setEventBus(mapEventBus);
    layerPanel.notifyMap(map, mapEventBus);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    wrappingEventBus = eventBus;
    layerPanel.setEventBus(eventBus);
    // Ignoring. We're going to let the Map set our eventBus.
  }

  public void setEventBus(final MapEventBus eventBus) {
    super.setEventBus(eventBus);
  }
}
