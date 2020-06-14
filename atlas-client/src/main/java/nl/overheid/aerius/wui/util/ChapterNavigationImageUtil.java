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
package nl.overheid.aerius.wui.util;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.DataResource;

import nl.overheid.aerius.shared.domain.ChapterIcon;
import nl.overheid.aerius.wui.resources.ChapterNavigationResources;
import nl.overheid.aerius.wui.resources.AtlasR;

public class ChapterNavigationImageUtil {
  public static DataResource getImageResource(final ChapterIcon option) {
    final ChapterNavigationResources sir = AtlasR.images();
    DataResource ir;

    if (option == null) {
      GWT.log("WARN: Null for chapter icon.");
      ir = sir.chapterTextPage();
    } else {
      switch (option) {
      case DIAGRAM:
        ir = sir.chapterDiagram();
        break;
      case MAP:
        ir = sir.chapterMap();
        break;
      case TABLE:
        ir = sir.chapterTable();
        break;
      default:
      case TEXTPAGE:
        ir = sir.chapterTextPage();
        break;
      }
    }

    return ir;
  }
}
