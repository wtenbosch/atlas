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
package nl.overheid.aerius.service.domain;

import java.util.Map;

public class ServiceSelector {
  private String value;
  private String name;

  private Map<String, String> tags;

  public ServiceSelector() {}

  public ServiceSelector(final String value, final String name) {
    this.value = value;
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public Map<String, String> getTags() {
    return tags;
  }

  public void setTags(final Map<String, String> meta) {
    this.tags = meta;
  }

  @Override
  public String toString() {
    return "Selector [value=" + value + ", name=" + getName() + "]";
  }
}
