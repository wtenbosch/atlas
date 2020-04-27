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
package nl.overheid.aerius.shared.domain.properties;

import java.util.HashMap;
import java.util.Map;

public class InfoProperties extends Properties {
  public static final String CONTENT_TEXT = "content_text_url";
  private static final String IMAGE_SOURCE = "image_source";

  public InfoProperties(final Map<String, Object> map) {
    super(map);
  }

  public InfoProperties() {
    this(new HashMap<>());
  }

  public String getContentTextUrl() {
    return getString(CONTENT_TEXT);
  }

  public String getImageSource() {
    return getStringOrDefault(IMAGE_SOURCE, "");
  }

  public void setImageSource(final String string) {
    put(IMAGE_SOURCE, string);
  }
}
