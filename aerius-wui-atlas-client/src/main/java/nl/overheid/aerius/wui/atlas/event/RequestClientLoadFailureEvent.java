package nl.overheid.aerius.wui.atlas.event;

import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class RequestClientLoadFailureEvent extends SimpleGenericEvent<Boolean> {
  public RequestClientLoadFailureEvent(final boolean value) {
    super(value);
  }
}
