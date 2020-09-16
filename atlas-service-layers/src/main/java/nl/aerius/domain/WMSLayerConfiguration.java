package nl.aerius.domain;

import java.util.List;

import javax.annotation.Nullable;

import com.google.auto.value.AutoValue;

import nl.aerius.domain.AutoValue_WMSLayerConfiguration;

@AutoValue
public abstract class WMSLayerConfiguration implements IsLayerConfiguration {
  public static Builder builder() {
    return new AutoValue_WMSLayerConfiguration.Builder()
        .opacityEnabled(true);
  }

  public static LayerConfiguration.Builder configuration() {
    return LayerConfiguration.builder()
        .type(LayerType.WMS);
  }

  @Override
  public abstract LayerConfiguration conf();

  public abstract String url();

  public abstract String baseLayer();

  @Nullable
  public abstract String baseStyle();

  public abstract String formats();

  @Nullable
  public abstract List<ConditionedStyleActivator> activators();

  @Nullable
  public abstract String cqlFilter();

  @Nullable
  public abstract String viewParams();

  public abstract boolean opacityEnabled();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder conf(LayerConfiguration value);

    public abstract Builder url(String value);

    public abstract Builder baseLayer(String value);

    public abstract Builder baseStyle(String value);

    public abstract Builder formats(String value);

    public abstract Builder activators(List<ConditionedStyleActivator> value);

    public abstract Builder cqlFilter(String value);

    public abstract Builder viewParams(String value);

    public abstract Builder opacityEnabled(boolean value);

    public abstract WMSLayerConfiguration build();
  }

}
