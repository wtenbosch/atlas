package nl.overheid.aerius.shared.domain.properties;

import java.util.HashMap;
import java.util.Map;

public class BasicParameterizedProperties extends Properties implements ParameterizedProperties {
  public static final String PARAMETERS = "params";

  public BasicParameterizedProperties(final Map<String, Object> map) {
    super(map);

    if (!map.containsKey(PARAMETERS)) {
      addEmptyParameters();
    }
  }

  private void addEmptyParameters() {
    put(PARAMETERS, new HashMap<String, String>());
  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, String> getParameters() {
    return (Map<String, String>) getRequired(PARAMETERS);
  }

  public void setParameter(final String key, final String value) {
    getParameters().put(key, value);
  }
}
