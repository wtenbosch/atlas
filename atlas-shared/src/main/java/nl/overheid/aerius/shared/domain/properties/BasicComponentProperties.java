package nl.overheid.aerius.shared.domain.properties;

import java.util.Map;

public class BasicComponentProperties extends BasicParameterizedProperties implements ComponentProperties {
  private static final String COMPONENT_SOURCE = "component_source";
  private static final String COMPONENT_NAME = "component_name";
  private static final String VERSION = "component_version";

  private static final String URL = "url";

  public BasicComponentProperties(final Map<String, Object> map) {
    super(map);
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
    final String str = getRequiredString(COMPONENT_SOURCE);

    final int start = str.lastIndexOf("/") + 1;
    final int end = str.lastIndexOf(".");

    return str.substring(start, end);
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

  public int getVersion() {
    final Object vers = getRequired(VERSION);

    return (int) (double) vers;
  }
}
