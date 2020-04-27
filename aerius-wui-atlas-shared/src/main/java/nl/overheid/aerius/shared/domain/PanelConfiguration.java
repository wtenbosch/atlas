package nl.overheid.aerius.shared.domain;

import java.util.HashMap;
import java.util.Map;

import nl.overheid.aerius.shared.util.HasParent;

public class PanelConfiguration implements HasParent<PanelConfiguration>, HasIndependence {
  private String name;

  private Map<String, Object> properties;

  private PanelConfiguration parent;

  private boolean independent;

  public PanelConfiguration() {
    this(new HashMap<>());
  }

  public PanelConfiguration(final HashMap<String, Object> properties) {
    this.properties = properties;
  }

  public Map<String, Object> getProperties() {
    return properties;
  }

  public void setProperties(final Map<String, Object> properties) {
    this.properties = properties;
  }

  public ConfigurationProperties asConfigurationProperties() {
    return new ConfigurationProperties(properties);
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public PanelConfiguration getParent() {
    return parent;
  }

  public void setParent(final PanelConfiguration parent) {
    this.parent = parent;
  }

  @Override
  public boolean isIndependent() {
    return independent;
  }

  public void setIndependent(final boolean independent) {
    this.independent = independent;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
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
    final PanelConfiguration other = (PanelConfiguration) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "PanelConfiguration [properties=" + properties + "]";
  }
}
