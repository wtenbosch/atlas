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

import com.google.gwt.user.client.ui.AcceptsOneWidget;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.wui.component.ChapterWidgetComponentDelegate;
import nl.overheid.aerius.wui.component.ChapterWidgetErrorDelegate;
import nl.overheid.aerius.wui.component.ChapterWidgetLegacyComponentDelegate;
import nl.overheid.aerius.wui.component.ChapterWidgetTextDelegate;

public interface ChapterWidgetFactory {
  ChapterWidgetComponentDelegate getComponentChapter(AcceptsOneWidget panel, PanelContent config);

  ChapterWidgetLegacyComponentDelegate getLegacyComponentChapter(AcceptsOneWidget panel, PanelContent config);

  ChapterWidgetTextDelegate getTextChapter(AcceptsOneWidget panel, Chapter chapter, PanelContent config);

  ChapterWidgetErrorDelegate getErrorChapter(AcceptsOneWidget target, String txt);
}
