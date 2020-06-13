package nl.overheid.aerius.domain;

import java.io.Serializable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class LayerBehaviour implements Serializable {
  private static final long serialVersionUID = 1L;

  public static Builder builder() {
    return new AutoValue_LayerBehaviour.Builder();
  }

  /**
   * The radio group name for this layer, this behaviour will cause all layers
   * under this bundle group name to be grouped under one radio item.
   */
  public abstract String bundleGroup();

  /**
   * The cluster name for this layer; this behaviour will cause only one of the
   * layers within a cluster to be visible.
   */
  public abstract String clusterGroup();

  /**
   * A friendly, simple, short, but unspecific, name that will identify this layer
   * within a specific context.
   */
  public abstract String friendlyName();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder bundleGroup(String value);

    public abstract Builder clusterGroup(String value);

    public abstract Builder friendlyName(String value);

    public abstract LayerBehaviour build();
  }
}
