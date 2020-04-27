package nl.overheid.aerius.wui.atlas.factories;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import nl.overheid.aerius.geo.domain.LayerInfo;
import nl.overheid.aerius.geo.domain.legend.ComponentLegendConfig;
import nl.overheid.aerius.geo.domain.legend.TextLegendConfig;

public class LayerLegendBuilderImpl implements LayerLegendBuilder {
  private final LayerLegendWidgetFactory factory;

  @Inject
  public LayerLegendBuilderImpl(final LayerLegendWidgetFactory factory) {
    this.factory = factory;
  }

  @Override
  public Widget getLegendWidget(final LayerInfo info) {
    if (info == null || info.getLegendConfig() == null) {
      return null;
    }

    final Widget legend;
    switch (info.getLegendConfig().getType()) {
    case TEXT:
      legend = factory.createLabelWidget(((TextLegendConfig) info.getLegendConfig()).getText());
      break;
    case COMPONENT:
      legend = factory.createComponentWidget(info.getSelectables(), (ComponentLegendConfig) info.getLegendConfig());
      break;
    default:
      legend = new Label("Geen legenda.");
    }

    return legend;
  }
}
