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

public class LegendComponentProperties extends LegendProperties implements ComponentProperties {
  private static final String COMPONENT_SOURCE = "component_source";
  private static final String COMPONENT_NAME = "component_name";
  private static final String PARAMETERS = "params";
  private static final String URL = "url";

  public LegendComponentProperties() {
    this(new HashMap<>());

    addEmptyParameters();
  }

  public LegendComponentProperties(final Map<String, Object> map) {
    super(map);

    if (!map.containsKey(PARAMETERS)) {
      addEmptyParameters();
    }
  }

  private void addEmptyParameters() {
    put(PARAMETERS, new HashMap<String, String>());
  }

  @Override
  public String getComponentSource() {
    return getRequiredString(COMPONENT_SOURCE);
  }

  public void setComponentSource(final String componentSource) {
    put(COMPONENT_SOURCE, componentSource);
  }

  @Override
  public String getComponentName() {
    return getRequiredString(COMPONENT_NAME);
  }

  public void setComponentName(final String componentName) {
    put(COMPONENT_NAME, componentName);
  }

  @Override
  public String getUrl() {
    return getParameters().get(URL);
  }

  public void setUrl(final String url) {
    setParameter(URL, url);
  }

  public void setParameter(final String key, final String value) {
    getParameters().put(key, value);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, String> getParameters() {
    return (Map<String, String>) getRequired(PARAMETERS);
  }
}
