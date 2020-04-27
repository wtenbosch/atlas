package nl.overheid.aerius.wui.atlas.event;

import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class AdblockerDetectedEvent extends SimpleGenericEvent<Boolean> {
  public AdblockerDetectedEvent(final Boolean value) {
    super(value);
  }
}
