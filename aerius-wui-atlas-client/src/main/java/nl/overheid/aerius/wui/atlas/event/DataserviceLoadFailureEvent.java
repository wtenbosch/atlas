package nl.overheid.aerius.wui.atlas.event;

import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class DataserviceLoadFailureEvent extends SimpleGenericEvent<Boolean> {
  public DataserviceLoadFailureEvent(final boolean value) {
    super(value);
  }
}
