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

import nl.overheid.aerius.shared.util.HasParent;

public enum ContextNavigationOption implements HasParent<ContextNavigationOption> {
  INFO,

  META,

  MAP,

  LAYERS(MAP),

  PREFERENCES,

  EXPORT;

  public static ContextNavigationOption DEFAULT = INFO;

  private final ContextNavigationOption parent;

  private ContextNavigationOption() {
    this(null);
  }

  private ContextNavigationOption(final ContextNavigationOption parent) {
    this.parent = parent;
  }

  public static ContextNavigationOption safeValueOf(final String name) {
    try {
      return ContextNavigationOption.valueOf(name.toUpperCase());
    } catch (final IllegalArgumentException e) {
      return DEFAULT;
    }
  }

  public ContextNavigationOption getParent() {
    return parent;
  }
}
