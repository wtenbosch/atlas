package nl.overheid.aerius.templates;

import java.util.HashMap;
import java.util.Map;

public final class TextCache {
  private TextCache() {}

  private static final Map<String, String> texts = new HashMap<>();

  public static void addText(final String key, final String text) {
    texts.put(key, text);
  }

  public static String find(final String key) {
    return texts.get(key);
  }
}
