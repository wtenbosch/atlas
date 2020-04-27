package nl.overheid.aerius.shared.domain;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SelectorResource {
  public static Builder builder() {
    return new AutoValue_SelectorResource.Builder();
  }

  public abstract String type();

  public abstract String url();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder type(String value);

    public abstract Builder url(String value);

    public abstract SelectorResource build();
  }
}
