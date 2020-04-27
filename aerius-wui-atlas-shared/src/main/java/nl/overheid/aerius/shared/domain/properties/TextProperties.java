package nl.overheid.aerius.shared.domain.properties;

import java.util.Map;

public class TextProperties extends Properties {
  private static final String KEY_TEXT = "text";
  private static final String KEY_VALUE = "value";

  public TextProperties(final Map<String, Object> map) {
    super(map);
  }

  public String getText() {
    return getProperties(KEY_TEXT).getRequiredString(KEY_VALUE);
  }
}
