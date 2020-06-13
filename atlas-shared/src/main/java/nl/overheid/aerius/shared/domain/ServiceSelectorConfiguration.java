package nl.overheid.aerius.shared.domain;

import java.io.Serializable;
import java.util.List;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ServiceSelectorConfiguration implements Serializable {
  private static final long serialVersionUID = -7187859046296122525L;

  public static Builder builder() {
    return new AutoValue_ServiceSelectorConfiguration.Builder();
  }

  public abstract String type();

  public abstract String title();

  public abstract String description();

  public abstract List<ServiceSelector> selectors();

  public abstract boolean multiselect();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder type(String value);

    public abstract Builder title(String value);

    public abstract Builder description(String value);

    public abstract Builder selectors(List<ServiceSelector> value);

    public abstract Builder multiselect(boolean value);

    public abstract ServiceSelectorConfiguration build();
  }
}
