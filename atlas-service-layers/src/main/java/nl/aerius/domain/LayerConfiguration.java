package nl.aerius.domain;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.Nullable;

import com.google.auto.value.AutoValue;

import nl.aerius.domain.AutoValue_LayerConfiguration;

@AutoValue
public abstract class LayerConfiguration implements Serializable {
  private static final long serialVersionUID = 1L;

  public static Builder builder() {
    return new AutoValue_LayerConfiguration.Builder();
  }

  public abstract String name();

  public abstract String title();

  public abstract boolean visible();

  public abstract double opacity();

  public abstract LayerType type();

  @Nullable
  public abstract LayerBehaviour behaviour();

  @Nullable
  public abstract ArrayList<String> selectables();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder name(String value);

    public abstract Builder title(String value);

    public abstract Builder visible(boolean value);

    public abstract Builder opacity(double value);

    public abstract Builder type(LayerType value);

    public abstract Builder behaviour(LayerBehaviour value);

    public abstract Builder selectables(ArrayList<String> value);

    public abstract LayerConfiguration build();
  }
}
