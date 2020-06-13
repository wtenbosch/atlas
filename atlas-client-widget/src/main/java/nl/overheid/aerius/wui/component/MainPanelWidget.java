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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.MainContentType;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.wui.widget.EventComposite;
import nl.overheid.aerius.wui.widget.HasEventBus;

public class MainPanelWidget extends EventComposite {
  private static final MainPanelWidgetUiBinder UI_BINDER = GWT.create(MainPanelWidgetUiBinder.class);

  interface MainPanelWidgetUiBinder extends UiBinder<Widget, MainPanelWidget> {}

  @UiField SimplePanel target;

  private final PanelWidgetDelegate delegate;

  private final PanelContent config;

  /**
   * @param config Must not be null.
   */
  public MainPanelWidget(final ChapterWidgetBuilder builder, final Chapter chapter, final PanelContent config) {
    this.config = config;
    initWidget(UI_BINDER.createAndBindUi(this));

    final MainContentType contentType = config.asMainProperties().getContentType();

    delegate = builder.createChapterWidget(contentType, chapter, config, target);
  }

  public void clear() {
    delegate.clear();
  }

  public void show() {
    delegate.show();

  }

  public void notifySelector(final Selector selector) {
    if (delegate == null) {
      return;
    }

    delegate.notifySelector(selector);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    if (delegate instanceof HasEventBus) {
      ((HasEventBus) delegate).setEventBus(eventBus);
    }
  }
}
