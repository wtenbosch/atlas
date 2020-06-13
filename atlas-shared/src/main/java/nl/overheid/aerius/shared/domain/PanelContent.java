package nl.overheid.aerius.shared.domain;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.auto.value.AutoValue;

import nl.overheid.aerius.shared.domain.properties.ExportProperties;
import nl.overheid.aerius.shared.domain.properties.InfoProperties;
import nl.overheid.aerius.shared.domain.properties.LayerProperties;
import nl.overheid.aerius.shared.domain.properties.LegendComponentProperties;
import nl.overheid.aerius.shared.domain.properties.LegendProperties;
import nl.overheid.aerius.shared.domain.properties.LegendTextProperties;
import nl.overheid.aerius.shared.domain.properties.MainComponentProperties;
import nl.overheid.aerius.shared.domain.properties.MainProperties;
import nl.overheid.aerius.shared.domain.properties.MainTextProperties;
import nl.overheid.aerius.shared.domain.properties.MetaProperties;
import nl.overheid.aerius.shared.domain.properties.TextProperties;

@AutoValue
public abstract class PanelContent {
  public static Builder builder() {
    return new AutoValue_PanelContent.Builder();
  }

  public abstract List<String> selectables();

  public abstract Map<String, Object> properties();

  @Nullable
  public abstract EditableContentConfigCollection editableContentConfig();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder selectables(List<String> value);

    public abstract Builder properties(Map<String, Object> value);

    @Nullable
    public abstract Builder editableContentConfig(EditableContentConfigCollection value);

    public abstract PanelContent build();
  }

  public MainProperties asMainProperties() {
    return new MainProperties(properties());
  }

  public MainComponentProperties asMainComponentProperties() {
    return new MainComponentProperties(properties());
  }

  public MainTextProperties asMainTextProperties() {
    return new MainTextProperties(properties());
  }

  public LegendProperties asLegendProperties() {
    return new LegendProperties(properties());
  }

  public LegendComponentProperties asLegendComponentProperties() {
    return new LegendComponentProperties(properties());
  }

  public LegendTextProperties asLegendTextProperties() {
    return new LegendTextProperties(properties());
  }

  public InfoProperties asInfoProperties() {
    return new InfoProperties(properties());
  }

  public TextProperties asTextProperties() {
    return new TextProperties(properties());
  }

  public MetaProperties asMetaTextProperties() {
    return new MetaProperties(properties());
  }

  public ExportProperties asExportTextProperties() {
    return new ExportProperties(properties());
  }

  public LayerProperties asLayerProperties() {
    return new LayerProperties(properties());
  }
}
