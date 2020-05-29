package nl.overheid.aerius.wui.component;

import com.google.gwt.user.client.ui.IsWidget;

import nl.overheid.aerius.geo.domain.IsLayer;

public interface LayerPanelItem extends IsWidget {
  void setLayerVisible(IsLayer<?> layer, boolean visible);

  void setLayerOpacity(double opacity);

  void remove(IsLayer<?> isLayer);

  void setClustered(boolean clustered);
}
