package nl.overheid.aerius.wui.atlas.event;

import nl.overheid.aerius.geo.domain.Point;
import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class MapCenterChangeEvent extends SimpleGenericEvent<Point> {
  public MapCenterChangeEvent(final Point value) {
    super(value);
  }
}