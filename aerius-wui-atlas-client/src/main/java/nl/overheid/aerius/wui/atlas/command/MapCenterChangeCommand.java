package nl.overheid.aerius.wui.atlas.command;

import com.google.gwt.dom.client.Element;

import nl.overheid.aerius.geo.BBox;
import nl.overheid.aerius.geo.domain.Point;
import nl.overheid.aerius.wui.atlas.event.MapCenterChangeEvent;
import nl.overheid.aerius.wui.command.SimpleGenericCommand;

public class MapCenterChangeCommand extends SimpleGenericCommand<Point, MapCenterChangeEvent> {
  private final Element viewport;
  private final BBox bbox;

  public MapCenterChangeCommand(final Point value, final Element viewport, final BBox bbox) {
    super(value);
    this.viewport = viewport;
    this.bbox = bbox;
  }

  @Override
  protected MapCenterChangeEvent createEvent(final Point value) {
    return new MapCenterChangeEvent(value);
  }

  public BBox getBbox() {
    return bbox;
  }

  public Element getViewport() {
    return viewport;
  }
}