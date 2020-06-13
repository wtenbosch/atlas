package nl.overheid.aerius.wui.atlas.event;

import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class RequestServerLoadFailureEvent extends SimpleGenericEvent<Boolean> {
  public RequestServerLoadFailureEvent(final boolean value) {
    super(value);
  }
}
