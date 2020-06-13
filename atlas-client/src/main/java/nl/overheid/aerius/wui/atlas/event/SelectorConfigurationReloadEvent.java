package nl.overheid.aerius.wui.atlas.event;

import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class SelectorConfigurationReloadEvent extends SimpleGenericEvent<String> {
  public SelectorConfigurationReloadEvent(final String type) {
    super(type);
  }
}
