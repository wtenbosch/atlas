package nl.overheid.aerius.wui.atlas.event;

import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class RequestConnectionFailureEvent extends SimpleGenericEvent<Boolean> {
  public RequestConnectionFailureEvent(final boolean value) {
    super(value);
  }
}
