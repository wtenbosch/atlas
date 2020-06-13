package nl.overheid.aerius.shared.domain;

import com.google.auto.value.AutoValue;

/**
 * Separate out the label from the autovalue, because the label should not be included in any equality or hashcode operations.
 */
@AutoValue
public abstract class DatasetConfiguration {
  private String label;

  public static Builder builder() {
    return new AutoValue_DatasetConfiguration.Builder();
  }

  public DatasetConfiguration label(final String label) {
    this.label = label;
    return this;
  }

  public String label() {
    return label;
  }

  public abstract String code();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder code(String value);

    public abstract DatasetConfiguration build();
  }
}
