package nl.overheid.aerius.wui.atlas.command;

import com.google.web.bindery.event.shared.binder.GenericEvent;

public class ConfigurationPropertyChangeCommand extends GenericEvent {
  private final String key;
  private final String value;

  public ConfigurationPropertyChangeCommand(final String key, final String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
