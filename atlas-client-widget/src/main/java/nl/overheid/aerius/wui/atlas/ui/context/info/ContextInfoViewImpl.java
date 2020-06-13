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
package nl.overheid.aerius.wui.atlas.ui.context.info;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.PanelConfiguration;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.wui.atlas.factories.SelectableTextWidgetFactory;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.component.SelectableTextWidget;
import nl.overheid.aerius.wui.domain.story.StoryContext;
import nl.overheid.aerius.wui.util.ObservingReplacementAssistant;
import nl.overheid.aerius.wui.util.ReplacementAssistant;
import nl.overheid.aerius.wui.util.SelectorWidgetUtil;
import nl.overheid.aerius.wui.util.StoryTitleProvider;
import nl.overheid.aerius.wui.widget.HeadingWidget;

public class ContextInfoViewImpl extends BasicEventPanelComposite implements ContextInfoView {
  interface ContextInfoViewImplEventBinder extends EventBinder<ContextInfoViewImpl> {}

  private final ContextInfoViewImplEventBinder EVENT_BINDER = GWT.create(ContextInfoViewImplEventBinder.class);

  private static final ContextInfoViewImplUiBinder UI_BINDER = GWT.create(ContextInfoViewImplUiBinder.class);

  interface ContextInfoViewImplUiBinder extends UiBinder<Widget, ContextInfoViewImpl> {}

  @UiField FlowPanel ambiance;
  @UiField FlowPanel contentPanel;

  @UiField HeadingWidget titleField;
  @UiField HeadingWidget subtitleField;
  @UiField HeadingWidget titleFieldOverlay;
  @UiField HeadingWidget subtitleFieldOverlay;
  @UiField HeadingWidget chapterField;
  @UiField HeadingWidget chapterFieldOverlay;

  @UiField FlowPanel infoContent;

  @UiField Image image;

  @UiField FlowPanel selectorContainer;

  @UiField(provided = true) SelectableTextWidget selectableText;

  private final StoryContext storyContext;
  private final ReplacementAssistant replacer;
  private final StoryTitleProvider titleProvider;
  private final ObservingReplacementAssistant observingReplacer;

  @Inject
  public ContextInfoViewImpl(@Assisted final PanelConfiguration config, final StoryTitleProvider titleProvider, final StoryContext storyContext,
      final ReplacementAssistant replacer, final ObservingReplacementAssistant observingReplacer, final SelectableTextWidgetFactory fact) {
    this.titleProvider = titleProvider;
    this.storyContext = storyContext;
    this.replacer = replacer;
    this.observingReplacer = observingReplacer;
    selectableText = fact.createSelectableTextWidget();
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @Override
  public void setPanelContent(final PanelContent content) {
    super.setPanelContent(content);

    // Set fragment information
    final Story story = storyContext.getStory();
    subtitleField.setText(story.info().name());
    subtitleFieldOverlay.setText(story.info().name());

    observingReplacer.register(titleProvider.apply(story), txt -> {
      titleField.setText(txt);
      titleFieldOverlay.setText(txt);
    });

    final Chapter chapter = storyContext.getChapter();
    chapterField.setText(chapter.title());
    chapterFieldOverlay.setText(chapter.title());

    // Clear existing content
    selectorContainer.clear();
    selectableText.clear();

    // If no new content, go away
    if (content == null) {
      return;
    }

    final String contentTextUrl = content.asInfoProperties().getContentTextUrl();
    if (contentTextUrl == null) {
      displayAmbientBackground(content);
    } else {
      displaySelectableInfo(contentTextUrl, content);
    }
  }

  private void displayAmbientBackground(final PanelContent content) {
    infoContent.setVisible(false);
    ambiance.setVisible(true);
    contentPanel.setVisible(false);

    String imageSource = replacer.replace(UglyBoilerPlate.fixAreaId(content.asInfoProperties().getImageSource()));
    // TODO Remove
    if (imageSource == null) {
      imageSource = "res/bg_stub3.jpeg";
    }

    if (!image.getUrl().equals(imageSource)) {
      image.setUrl(imageSource);
      image.setVisible(true);
    }
  }

  private void displaySelectableInfo(final String url, final PanelContent content) {
    infoContent.setVisible(true);
    ambiance.setVisible(false);
    contentPanel.setVisible(true);

    image.setUrl("");
    image.setVisible(false);

    SelectorWidgetUtil.populateSelectorWidget(selectorContainer, content.selectables(), eventBus);

    selectableText.setTypes(content.selectables());
    selectableText.setContentTextUrl(url);
  }

  @Override
  public boolean hasPanelContent() {
    // Default to there not being panel content by default
    return false;
  }

  @Override
  public void show() {

  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, selectableText);

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }
}
