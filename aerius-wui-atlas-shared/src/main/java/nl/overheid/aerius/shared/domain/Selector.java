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
package nl.overheid.aerius.shared.domain;

import java.util.Map;
import java.util.Optional;

public class Selector implements IsSelector, HasSelectorType, Comparable<Selector> {
  public static final String DEFAULT_TYPE_YEAR = "year";

  private String type;
  private String title;
  private Optional<String> value;

  private Map<String, String> tags;

  private boolean system;

  public Selector() {}

  public Selector(final String type, final String value) {
    this(type, Optional.ofNullable(value));
  }

  public Selector(final String type, final Optional<String> value) {
    this.type = type;
    this.value = value;
  }

  public Selector(final String type, final String value, final boolean system) {
    this(type, Optional.ofNullable(value), system);
  }

  public Selector(final String type, final Optional<String> value, final boolean system) {
    this.type = type;
    this.value = value;
    this.system = system;
  }

  public Selector(final String type, final String value, final String title) {
    this(type, Optional.ofNullable(value), title);
  }

  public Selector(final String type, final Optional<String> value, final String title) {
    this.type = type;
    this.value = value;
    this.title = title;
  }

  public Selector(final String type, final String value, final String title, final boolean system) {
    this(type, Optional.ofNullable(value), title, system);
  }

  public Selector(final String type, final Optional<String> value, final String title, final boolean system) {
    this.type = type;
    this.value = value;
    this.title = title;
    this.system = system;
  }

  public Optional<String> getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = Optional.ofNullable(value);
  }

  @Override
  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public Optional<String> getTitle() {
    return Optional.ofNullable(title);
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public Optional<Map<String, String>> getTags() {
    return Optional.ofNullable(tags);
  }

  public void setTags(final Map<String, String> tags) {
    this.tags = tags;
  }

  public boolean isSystem() {
    return system;
  }

  @Override
  public Selector getSelector() {
    return this;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (type == null ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Selector other = (Selector) obj;
    if (type == null) {
      if (other.type != null) {
        return false;
      }
    } else if (!type.equals(other.type)) {
      return false;
    } else if (!value.equals(other.value)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Selector [type=" + type + ", value=" + value + "]";
  }

  @Override
  public int compareTo(final Selector other) {
    final int i = type.compareTo(other.type);
    if (i != 0) {
      return i;
    } else {
      return value.isPresent() == other.value.isPresent() ? 0 : value.get().compareTo(other.value.orElse(null));
    }
  }
}
