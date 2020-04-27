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
import nl.overheid.aerius.wui.atlas.event.MapActiveEvent;
import nl.overheid.aerius.wui.atlas.factories.ChapterWidgetFactory;
import nl.overheid.aerius.wui.util.SelectorUtil;
import nl.overheid.aerius.wui.widget.EventComposite;
import nl.overheid.aerius.wui.widget.HasEventBus;

public class MainPanelWidget extends EventComposite {
  private static final MainPanelWidgetUiBinder UI_BINDER = GWT.create(MainPanelWidgetUiBinder.class);

  interface MainPanelWidgetUiBinder extends UiBinder<Widget, MainPanelWidget> {}

  @UiField SimplePanel target;

  private PanelWidgetDelegate delegate;

  private final PanelContent config;

  private boolean mapActive;

  /**
   * @param config Must not be null.
   */
  public MainPanelWidget(final ChapterWidgetFactory chapterWidgetFactory, final Chapter chapter, final PanelContent config) {
    this.config = config;
    initWidget(UI_BINDER.createAndBindUi(this));

    final MainContentType contentType = config.asMainProperties().getContentType();
    if (contentType == null) {
      delegate = chapterWidgetFactory.getErrorChapter(target, "No valid main content type.");
      return;
    }

    switch (contentType) {
    case COMPONENT:
      switch (config.asMainComponentProperties().getVersion()) {
      case 3:
        delegate = chapterWidgetFactory.getComponentChapter(target, config);
        break;
      case 1:
      default:
        delegate = chapterWidgetFactory.getLegacyComponentChapter(target, config);
        break;
      }
      break;
    case MAP:
      mapActive = true;
      delegate = chapterWidgetFactory.getMapChapter(target, chapter, config);
      break;
    case TEXT:
    default:
      delegate = chapterWidgetFactory.getTextChapter(target, chapter, config);
      break;
    }
  }

  public void clear() {
    delegate.clear();
  }

  public void show() {
    delegate.show();

    eventBus.fireEvent(new MapActiveEvent(mapActive));
  }

  public void notifySelector(final Selector selector) {
    if (delegate == null) {
      return;
    }

    if (!SelectorUtil.matchesSmart(selector, config.selectables())) {
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
