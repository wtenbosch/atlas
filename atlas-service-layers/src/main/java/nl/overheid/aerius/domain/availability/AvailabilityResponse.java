package nl.overheid.aerius.domain.availability;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AvailabilityResponse {
  public static Builder builder() {
    return new AutoValue_AvailabilityResponse.Builder();
  }

  public abstract boolean result();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder result(boolean value);

    public abstract AvailabilityResponse build();
  }
}
