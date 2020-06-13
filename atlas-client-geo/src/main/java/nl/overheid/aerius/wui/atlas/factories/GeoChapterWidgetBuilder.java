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
package nl.overheid.aerius.wui.atlas.factories;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.MainContentType;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.wui.component.ChapterWidgetBuilder;
import nl.overheid.aerius.wui.component.PanelWidgetDelegate;

public class GeoChapterWidgetBuilder implements ChapterWidgetBuilder {
  @Inject private ChapterWidgetFactory factory;
  @Inject private GeoChapterWidgetFactory geoFactory;

  @Override
  public PanelWidgetDelegate createChapterWidget(final MainContentType contentType, final Chapter chapter, final PanelContent config,
      final SimplePanel target) {
    PanelWidgetDelegate delegate;

    if (contentType == null) {
      delegate = factory.getErrorChapter(target, "No valid main content type.");
    } else {
      switch (contentType) {
      case COMPONENT:
        switch (config.asMainComponentProperties().getVersion()) {
        case 3:
          delegate = factory.getComponentChapter(target, config);
          break;
        case 1:
        default:
          delegate = factory.getLegacyComponentChapter(target, config);
          break;
        }
        break;
      case MAP:
        delegate = geoFactory.getMapChapter(target, chapter, config);
        break;
      case TEXT:
      default:
        delegate = factory.getTextChapter(target, chapter, config);
        break;
      }
    }

    return delegate;
  }
}
