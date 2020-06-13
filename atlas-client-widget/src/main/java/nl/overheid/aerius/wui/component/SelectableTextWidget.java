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

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.ExtraSimpleHtmlSanitizer;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.wui.atlas.future.chapter.ChapterTextOracle;
import nl.overheid.aerius.wui.util.ObservingReplacementAssistant;
import nl.overheid.aerius.wui.util.ReplacementAssistant;
import nl.overheid.aerius.wui.widget.EventComposite;

public class SelectableTextWidget extends EventComposite {
  private static final SelectableTextWidgetUiBinder UI_BINDER = GWT.create(SelectableTextWidgetUiBinder.class);

  interface SelectableTextWidgetUiBinder extends UiBinder<Widget, SelectableTextWidget> {}

  private final SelectorCapable textWizard;

  @UiField HTML textField;

  private final ObservingReplacementAssistant observableReplacer;

  private final ChapterTextOracle oracle;

  @Inject
  public SelectableTextWidget(final ReplacementAssistant replacer, final ObservingReplacementAssistant observableReplacer,
      final ChapterTextOracle oracle) {
    this.observableReplacer = observableReplacer;
    this.oracle = oracle;
    initWidget(UI_BINDER.createAndBindUi(this));

    textWizard = new SmartSelectorCapable(s -> {
      String finalText = s;
      if (replacer != null) {
        finalText = replacer.replace(finalText);
      }

      final SafeHtml html = ExtraSimpleHtmlSanitizer.sanitizeHtml(finalText);

      textField.setHTML(html);
    });
  }

  public void setTypes(final List<String> types) {
    textWizard.setTypes(types);
  }

  public void setContentTextUrl(final String url) {
    observableReplacer.registerStrict(url, uri -> {
      if (uri == null) {
        return;
      }

      oracle.getChapterContent(uri, txt -> textWizard.setContentText(txt));
    });
  }

  public void setSelectors(final List<Selector> selectors) {
    textWizard.setSelectors(selectors);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);
  }

  public void clear() {
    textWizard.clear();
    textField.setHTML("");
    textField.getElement().setInnerHTML("");
  }
}
