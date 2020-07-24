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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.overheid.aerius.shared.domain.DocumentResource;

public class ExportProperties extends Properties {
  private static final String EXPORT_TEXT = "export_text";
  private static final String EXPORT_VALUE = "value";

  public ExportProperties(final Map<String, Object> map) {
    super(map);
  }

  public ExportProperties() {
    this(new HashMap<>());
  }

  public String getExportText() {
    return getProperties(EXPORT_TEXT).getString(EXPORT_VALUE);
  }

  public List<DocumentResource> getExportResources() {
    final List<Map<String, Object>> linksLst = getArrayListOfMap("links");
    final List<DocumentResource> lst = new ArrayList<>();
    if (linksLst != null) {
      for (final Map<String, Object> link : linksLst) {
        final DocumentResource.Builder res = DocumentResource.builder()
            .name(String.valueOf(link.get("name")))
            .url(String.valueOf(link.get("url")));

        if (link.containsKey("date") && link.get("data") != null) {
          res.date(new Date(Long.parseLong(String.valueOf(link.get("date")))));
        }

        lst.add(res.build());
      }
    }

    return lst;
  }
}
