package nl.aerius.domain;

import java.io.Serializable;

import com.google.auto.value.AutoValue;

import nl.aerius.domain.AutoValue_BboxContainer;

@AutoValue
public abstract class BboxContainer implements Serializable {
  public static Builder builder() {
    return new AutoValue_BboxContainer.Builder();
  }

  public abstract String bbox();

  public abstract String title();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder bbox(String value);

    public abstract Builder title(String value);

    public abstract BboxContainer build();
  }

}
