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

public enum AreaGroupType {
  DRENTHE("drenthe"),
  FLEVOLAND("flevoland"),
  FRIESLAND("friesland"),
  GELDERLAND("gelderland"),
  GRONINGEN("groningen"),
  LIMBURG("limburg"),
  NOORDBRABANT("noord-brabant"),
  NOORDHOLLAND("noord-holland"),
  OVERIJSSEL("overijssel"),
  RIJKSOVERHEID("rijksoverheid"),
  UTRECHT("utrecht"),
  ZEELAND("zeeland"),
  ZUIDHOLLAND("zuid-holland");

  private String name;

  private AreaGroupType(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static AreaGroupType fromName(final String name) {
    for (final AreaGroupType option : values()) {
      if (option.getName().equals(name)) {
        return option;
      }
    }

    throw new IllegalArgumentException("Unknown name: " + name);
  }
}
