package nl.aerius.domain;

import java.util.ArrayList;
import java.util.List;

public class BundledLayerConfiguration {
  private List<LayerConfiguration> layers = new ArrayList<>();

  public List<LayerConfiguration> getLayers() {
    return layers;
  }

  public void setLayers(List<LayerConfiguration> layers) {
    this.layers = layers;
  }

}
