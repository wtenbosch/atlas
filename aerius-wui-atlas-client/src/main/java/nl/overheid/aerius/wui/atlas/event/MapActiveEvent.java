package nl.overheid.aerius.wui.atlas.event;

import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class MapActiveEvent extends SimpleGenericEvent<Boolean> {
  public MapActiveEvent(final Boolean value) {
    super(value);
  }
}
