package nl.overheid.aerius.geo.wui.layers;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import ol.Collection;
import ol.Coordinate;
import ol.Feature;
import ol.FeatureOptions;
import ol.OLFactory;
import ol.format.Wkt;
import ol.format.WktReadOptions;
import ol.layer.Layer;
import ol.layer.VectorLayerOptions;
import ol.proj.Projection;
import ol.source.Vector;
import ol.source.VectorOptions;
import ol.style.Icon;
import ol.style.IconOptions;
import ol.style.Style;
import ol.style.StyleOptions;

import nl.overheid.aerius.geo.domain.InformationZoomLevel;
import nl.overheid.aerius.geo.domain.IsLayer;
import nl.overheid.aerius.geo.domain.Point;
import nl.overheid.aerius.geo.event.InfoLocationChangeEvent;
import nl.overheid.aerius.geo.util.HexagonUtil;
import nl.overheid.aerius.wui.dev.GWTProd;

public class InformationLayer implements IsLayer<Layer> {
  private static final String INFO_MARKER_SVG = "/res/location-icon.svg";
  private final InformationLayerEventBinder EVENT_BINDER = GWT.create(InformationLayerEventBinder.class);

  interface InformationLayerEventBinder extends EventBinder<InformationLayer> {}

  private final ol.layer.Vector vectorLayer;
  private final Projection projection;
  private final Style style;

  public InformationLayer(final Projection projection, final EventBus eventBus) {
    this.projection = projection;
    // create style
    final StyleOptions styleOptions = new StyleOptions();

    final IconOptions iconOptions = new IconOptions();
    iconOptions.setSrc(INFO_MARKER_SVG);
    iconOptions.setSnapToPixel(true);
    iconOptions.setAnchor(new double[] { 0.5, 1 });
    iconOptions.setImgSize(OLFactory.createSize(32, 32));
    final Icon icon = new Icon(iconOptions);
    styleOptions.setImage(icon);
    styleOptions.setStroke(OLFactory.createStroke(OLFactory.createColor(214, 51, 39, 1), 2));
    styleOptions.setFill(OLFactory.createFill(OLFactory.createColor(255, 255, 255, 0.4)));

    style = new Style(styleOptions);

    final VectorLayerOptions vectorLayerOptions = OLFactory.createOptions();
    vectorLayerOptions.setStyle(style);

    vectorLayer = new ol.layer.Vector(vectorLayerOptions);
    vectorLayer.setZIndex(10001);
    
    GWTProd.log("Initializing information layer: " + eventBus);

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @EventHandler
  public void onInformationLocationChangedEvent(final InfoLocationChangeEvent e) {
    GWTProd.log("Changing information layer: " + e.getValue().getId());
    
    // Create the final hexagon feature
    final Feature hexagon = createHexagon(e.getValue());

    // Create the marker
    final Feature marker = createMarker(e.getValue());

    // Push the features
    final Collection<Feature> features = new Collection<Feature>();
    features.push(marker);
    features.push(hexagon);

    // Create source
    final VectorOptions vectorSourceOptions = OLFactory.createOptions();
    vectorSourceOptions.setFeatures(features);
    final Vector vectorSource = new Vector(vectorSourceOptions);

    // Update layer with new source
    vectorLayer.setSource(vectorSource);
  }

  private Feature createMarker(final Point value) {
    final Coordinate coordinate = OLFactory.createCoordinate(value.getX(), value.getY());
    final ol.geom.Point point = new ol.geom.Point(coordinate);
    final FeatureOptions featureOptions = OLFactory.createOptions();
    featureOptions.setGeometry(point);
    return new Feature(featureOptions);
  }

  private Feature createHexagon(final Point value) {
    final String hexagonWKT = HexagonUtil.createHexagonWkt(value, InformationZoomLevel.get());
    final Wkt wkt = new Wkt();
    final WktReadOptions wktOptions = OLFactory.createOptions();
    wktOptions.setDataProjection(projection);
    wktOptions.setFeatureProjection(projection);
    return wkt.readFeature(hexagonWKT, wktOptions);
  }

  @Override
  public Layer asLayer() {
    return vectorLayer;
  }
}
