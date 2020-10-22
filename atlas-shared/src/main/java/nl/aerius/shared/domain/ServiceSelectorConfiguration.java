package nl.aerius.shared.domain;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ServiceSelectorConfiguration implements Serializable {
  private static final long serialVersionUID = 1L;

  public static Builder builder() {
    return new AutoValue_ServiceSelectorConfiguration.Builder()
        .multiselect(false);
  }

  public abstract String type();

  @Nullable
  public abstract String singular();

  public abstract String title();

  public abstract String description();

  /**
   * The number of default values.
   * 
   * This value exists so it doesn't need to be calculated.
   */
  @Nullable
  public abstract Integer defaultNum();

  public abstract List<ServiceSelector> selectors();

  public abstract boolean multiselect();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder type(String value);

    public abstract Builder singular(String value);

    public abstract Builder title(String value);

    public abstract Builder description(String value);

    public abstract Builder defaultNum(Integer value);

    public abstract Builder selectors(List<ServiceSelector> value);

    public abstract Builder multiselect(boolean value);

    public abstract ServiceSelectorConfiguration build();
  }
}
