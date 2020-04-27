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

/**
 * An assessment area, like a N2000 area.
 */
public class NatureArea implements Comparable<NatureArea> {
  private String areaId;
  private String name;
  private String extent;
  private String centroid;
  
  private AreaGroupType authority;

  public String getId() {
    return areaId;
  }

  public void setId(final String id) {
    this.areaId = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public int compareTo(final NatureArea o) {
    return name.compareTo(o.getName());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (areaId == null ? 0 : areaId.hashCode());
    result = prime * result + (name == null ? 0 : name.hashCode());
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
    final NatureArea other = (NatureArea) obj;
    if (areaId == null) {
      if (other.areaId != null) {
        return false;
      }
    } else if (!areaId.equals(other.areaId)) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

  public String getExtent() {
    return extent;
  }

  public void setExtent(final String extent) {
    this.extent = extent;
  }

  public AreaGroupType getAuthority() {
    return authority;
  }

  public void setAuthority(final AreaGroupType authority) {
    this.authority = authority;
  }

  public String getCenterOfMass() {
    return centroid;
  }

  public void setCenterOfMass(final String centroid) {
    this.centroid = centroid;
  }

  @Override
  public String toString() {
    return "AssessmentArea [name=" + name + "]";
  }
}