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
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Properties {
  protected final Map<String, Object> map;

  public Properties() {
    this(new HashMap<>());
  }

  public Properties(final Map<String, Object> map) {
    this.map = map;
  }

  public Object get(final String key) {
    return map.get(key);
  }

  public Optional<String> getOptionalString(final String key) {
    return Optional.ofNullable(getString(key));
  }

  public Optional<Properties> getOptionalProperties(final String key) {
    return Optional.ofNullable(getProperties(key));
  }

  public String getString(final String key) {
    return (String) map.get(key);
  }

  public String getStringOrDefault(final String key, final String devault) {
    final String str = getString(key);
    return str == null ? devault : str;
  }

  @SuppressWarnings("unchecked")
  public List<String> getArrayListOfString(final String key) {
    return (List<String>) get(key);
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getArrayListOfMap(final String key) {
    return (List<Map<String, Object>>) get(key);
  }

  public void setArrayList(final String key, final List<String> values) {
    map.put(key, values);
  }

  public Object getRequired(final String key) {
    checkRequired(key);

    return get(key);
  }

  public String getRequiredString(final String key) {
    checkRequired(key);

    return getString(key);
  }

  @SuppressWarnings("unchecked")
  protected Properties getProperties(final String key) {
    final Map<String, Object> inner = (Map<String, Object>) map.get(key);
    return inner != null ? new Properties(inner) : null;
  }

  private void checkRequired(final String key) {
    if (!map.containsKey(key) || map.get(key) == null) {
      throw new RuntimeException("Attempted to retrieve required key (" + key + " ) while it does not exist. Properties: " + map.toString());
    }
  }

  public Object put(final String key, final Object value) {
    return map.put(key, value);
  }

  public Map<String, Object> getMap() {
    return map;
  }
}
