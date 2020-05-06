package nl.overheid.aerius.templates.stories.helpers;

import java.util.Optional;

import nl.overheid.aerius.templates.TextCache;

public class TextBuilder {
  private String file;

  public TextBuilder() {}

  public TextBuilder file(final String file) {
    this.file = file;
    return this;
  }

  public String build() {
    return Optional.ofNullable(TextCache.find(file))
        .orElseThrow(() -> new IllegalStateException("Could not retrieve text key: " + file));
  }
}
