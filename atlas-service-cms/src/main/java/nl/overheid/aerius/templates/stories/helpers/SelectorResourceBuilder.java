package nl.overheid.aerius.templates.stories.helpers;

import com.google.auto.value.AutoValue;

/**
 * An immutable builder for a builder. Don't ask.
 */
@AutoValue
public abstract class SelectorResourceBuilder {
  public static Builder builder() {
    return new AutoValue_SelectorResourceBuilder.Builder();
  }

  public abstract String type();

  public abstract String uri();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder type(String value);

    public abstract Builder uri(String value);

    public abstract SelectorResourceBuilder build();
  }
}
