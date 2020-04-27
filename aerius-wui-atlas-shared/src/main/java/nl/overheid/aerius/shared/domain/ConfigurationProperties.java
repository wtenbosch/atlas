package nl.overheid.aerius.shared.domain;

import java.util.HashMap;
import java.util.Map;

import nl.overheid.aerius.shared.domain.properties.Properties;

public class ConfigurationProperties extends Properties {
  private static final String PANEL_TYPE = "panelType";

  public ConfigurationProperties() {
    this(new HashMap<>());
  }

  public ConfigurationProperties(final Map<String, Object> map) {
    super(map);
  }

  public PanelType getPanelType() {
    return PanelType.valueOf(getRequiredString(PANEL_TYPE));
  }

  public void setPanelType(final PanelType type) {
    put(PANEL_TYPE, type.name());
  }

  public boolean isIndependent() {
    return false;
  }
}
