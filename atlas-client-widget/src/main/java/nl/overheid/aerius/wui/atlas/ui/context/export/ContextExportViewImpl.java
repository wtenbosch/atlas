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
package nl.overheid.aerius.wui.atlas.ui.context.export;

import java.util.List;

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
import nl.overheid.aerius.shared.domain.DocumentResource;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.shared.domain.properties.ExportProperties;
import nl.overheid.aerius.wui.atlas.event.SelectorEvent;
import nl.overheid.aerius.wui.atlas.ui.context.info.BasicEventPanelComposite;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.domain.story.StoryContext;
import nl.overheid.aerius.wui.util.ObservingReplacementAssistant;
import nl.overheid.aerius.wui.util.SelectorWidgetUtil;
import nl.overheid.aerius.wui.util.StoryTitleProvider;
import nl.overheid.aerius.wui.widget.HeadingWidget;

public class ContextExportViewImpl extends BasicEventPanelComposite implements ContextExportView {
  private static final ContextExportViewImplUiBinder UI_BINDER = GWT.create(ContextExportViewImplUiBinder.class);

  interface ContextExportViewImplUiBinder extends UiBinder<Widget, ContextExportViewImpl> {}

  private final ContextExportViewImplEventBinder EVENT_BINDER = GWT.create(ContextExportViewImplEventBinder.class);

  interface ContextExportViewImplEventBinder extends EventBinder<ContextExportViewImpl> {}

  @UiField HeadingWidget titleField;
  @UiField HeadingWidget subtitleField;
  @UiField HeadingWidget chapterField;

  @UiField FlowPanel selectorContainer;

  @UiField HeadingWidget linksHeading;
  @UiField HTML html;
  @UiField FlowPanel links;

  private final StoryContext storyContext;
  private final ObservingReplacementAssistant replacer;
  private String metaText;
  private List<DocumentResource> resources;

  private final StoryTitleProvider titleProvider;
  private final ObservingReplacementAssistant observingReplacer;

  @Inject
  public ContextExportViewImpl(final StoryContext storyContext, final StoryTitleProvider titleProvider,
      final ObservingReplacementAssistant replacer, final ObservingReplacementAssistant observingReplacer) {
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

    final ExportProperties properties = content.asExportTextProperties();
    setHtmlText(properties.getExportText());
    setLinks(properties.getExportResources());
    updateHtmlText();
    updateLinks();

    SelectorWidgetUtil.populateSelectorWidget(selectorContainer, content.selectables(), eventBus);
  }

  private void setLinks(final List<DocumentResource> resources) {
    this.resources = resources;
  }

  private void updateHtmlText() {
    if (metaText != null) {
      replacer.register(metaText, v -> html.setHTML(ExtraSimpleHtmlSanitizer.sanitizeHtml(v)));
    } else {
      GWTProd.warn("Possible configuration error: no export text in chapter, while export panel is present.");
    }
  }

  private void setHtmlText(final String metaText) {
    this.metaText = metaText;
  }

  @EventHandler
  public void onSelectorEvent(final SelectorEvent e) {
    if (!hasPanelContent()) {
      return;
    }

    // This is no longer needed with the observable replacement assistant.
    // updateHtmlText();
    // updateLinks();
  }

  private void updateLinks() {
    linksHeading.setVisible(resources != null && !resources.isEmpty());
    if (resources == null) {
      return;
    }

    links.clear();
    for (final DocumentResource resource : resources) {
      final Anchor anchor = new Anchor();

      replacer.register(resource.name(), v -> anchor.setText(v));
      replacer.register(resource.url(), v -> anchor.setHref(v));

      anchor.setTarget("_blank");
      links.add(anchor);
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
