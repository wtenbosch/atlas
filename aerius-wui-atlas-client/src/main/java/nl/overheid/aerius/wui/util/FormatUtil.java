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

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

import nl.overheid.aerius.wui.i18n.M;

public final class FormatUtil {
  private static final DateTimeFormat DATE_FORMATTER_DEFAULT = DateTimeFormat.getFormat(M.messages().dateFormat());
  private static final DateTimeFormat DATE_FORMATTER_SHORT = DateTimeFormat.getFormat("yyyy-MM-dd");

  private FormatUtil() {}

  public static String formatDate(final Date date) {
    return date == null ? M.messages().dateFormatNullDefault() : DATE_FORMATTER_DEFAULT.format(date);
  }

  public static String formatDateShort(final Date date) {
    return date == null ? "" : DATE_FORMATTER_SHORT.format(date);
  }

  public static String formatName(final String authorName) {
    if (authorName == null || authorName.isEmpty()) {
      return M.messages().nameNullDefault();
    }

    return authorName;
  }
}
