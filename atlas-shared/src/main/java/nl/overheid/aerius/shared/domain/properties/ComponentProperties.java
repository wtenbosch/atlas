package nl.overheid.aerius.shared.domain.properties;

import java.util.Map;

public interface ComponentProperties {

  String getComponentSource();

  String getComponentName();

  String getUrl();

  Map<String, String> getParameters();

}
