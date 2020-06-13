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
package nl.overheid.aerius.wui.atlas.ui.context.meta;

import java.util.Optional;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.ExtraSimpleHtmlSanitizer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.shared.domain.properties.MetaProperties;
import nl.overheid.aerius.wui.atlas.event.SelectorEvent;
import nl.overheid.aerius.wui.atlas.ui.context.info.BasicEventPanelComposite;
import nl.overheid.aerius.wui.domain.story.StoryContext;
import nl.overheid.aerius.wui.util.ObservingReplacementAssistant;
import nl.overheid.aerius.wui.util.ReplacementAssistant;
import nl.overheid.aerius.wui.util.SelectorWidgetUtil;
import nl.overheid.aerius.wui.util.StoryTitleProvider;
import nl.overheid.aerius.wui.widget.HeadingWidget;

public class ContextMetaViewImpl extends BasicEventPanelComposite implements ContextMetaView {
  private static final ContextMetaViewImplUiBinder UI_BINDER = GWT.create(ContextMetaViewImplUiBinder.class);

  interface ContextMetaViewImplUiBinder extends UiBinder<Widget, ContextMetaViewImpl> {}

  private final ContextMetaViewImplEventBinder EVENT_BINDER = GWT.create(ContextMetaViewImplEventBinder.class);

  interface ContextMetaViewImplEventBinder extends EventBinder<ContextMetaViewImpl> {}

  @UiField HeadingWidget titleField;
  @UiField HeadingWidget subtitleField;
  @UiField HeadingWidget chapterField;

  @UiField FlowPanel selectorContainer;

  @UiField HTML html;
  @UiField HTML explanation;
  @UiField Anchor link;

  private final StoryContext storyContext;
  private final ReplacementAssistant replacer;
  private String metaText;
  private Optional<String> dataServiceUrl;
  private Optional<String> explanationDatasetText;
  private final StoryTitleProvider titleProvider;
  private final ObservingReplacementAssistant observingReplacer;

  @Inject
  public ContextMetaViewImpl(final StoryContext storyContext, final StoryTitleProvider titleProvider, final ReplacementAssistant replacer, final ObservingReplacementAssistant observingReplacer) {
    this.storyContext = storyContext;
    this.titleProvider = titleProvider;
    this.replacer = replacer;
    this.observingReplacer = observingReplacer;
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @Override
  public void setPanelContent(final PanelContent content) {
    super.setPanelContent(content);
    // Set fragment information
    final Story story = storyContext.getStory();
    subtitleField.setText(story.info().name());
    observingReplacer.register(titleProvider.apply(story), txt -> titleField.setText(txt));
    final Chapter chapter = storyContext.getChapter();
    chapterField.setText(chapter.title());

    selectorContainer.clear();

    if (content == null) {
      html.setHTML("");
      return;
    }

    final MetaProperties props = content.asMetaTextProperties();
    setHtmlText(props.getText());
    setExplanationText(props.getExplanationDataset());
    setDataServiceUrl(props.getDataService());
    updateHtmlText();
    updateExplanationText();
    updateLink();

    SelectorWidgetUtil.populateSelectorWidget(selectorContainer, content.selectables(), eventBus);
  }

  private void setDataServiceUrl(final Optional<String> dataServiceUrl) {
    this.dataServiceUrl = dataServiceUrl;
  }

  private void updateHtmlText() {
    final String unsanitizedHtml = replacer.replace(metaText);

    html.setHTML(ExtraSimpleHtmlSanitizer.sanitizeHtml(unsanitizedHtml));
  }

  private void setHtmlText(final String metaText) {
    this.metaText = metaText;
  }

  private void updateExplanationText() {
    explanation.setText("");
    explanationDatasetText.map(v -> ExtraSimpleHtmlSanitizer.sanitizeHtml(replacer.replace(v))).ifPresent(explanation::setHTML);
  }

  private void setExplanationText(final Optional<String> optional) {
    this.explanationDatasetText = optional;
  }

  @EventHandler
  public void onSelectorEvent(final SelectorEvent e) {
    if (!hasPanelContent()) {
      return;
    }

    updateHtmlText();
    updateExplanationText();
    updateLink();
  }

  private void updateLink() {
    if (dataServiceUrl.isPresent()) {
      link.setVisible(true);
      link.setHref(dataServiceUrl.get());
      link.setText(dataServiceUrl.get());
    } else {
      link.setVisible(false);
    }
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }

  @Override
  public void show() {

  }
}
