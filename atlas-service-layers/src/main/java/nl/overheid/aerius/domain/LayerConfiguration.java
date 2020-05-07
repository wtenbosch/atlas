package nl.overheid.aerius.domain;

import java.util.List;
import java.util.function.Consumer;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.auto.value.AutoValue;

import nl.overheid.aerius.domain.legend.LegendConfiguration;

@AutoValue
public abstract class LayerConfiguration {
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

  public abstract LegendConfiguration legend();

  @Nullable
  public abstract List<String> selectables();

  @AutoValue.Builder
  public abstract static class Builder {
    private LayerBehaviour behaviour;

    public abstract Builder name(String value);

    public abstract Builder title(String value);

    public abstract Builder visible(boolean value);

    public abstract Builder opacity(double value);

    public abstract Builder type(LayerType value);

    public abstract Builder behaviour(LayerBehaviour value);

    public abstract Builder legend(LegendConfiguration value);

    public abstract Builder selectables(List<String> value);

    public abstract LayerConfiguration build();

    public Builder addBehaviour(final Consumer<LayerBehaviour> consumer) {
      if (behaviour == null) {
        behaviour = new LayerBehaviour();
        behaviour(behaviour);
      }

      consumer.accept(behaviour);
      return this;
    }
  }
}
