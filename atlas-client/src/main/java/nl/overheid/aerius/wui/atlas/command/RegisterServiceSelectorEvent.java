package nl.overheid.aerius.wui.atlas.command;

import nl.overheid.aerius.shared.domain.ServiceSelectorConfiguration;
import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class RegisterServiceSelectorEvent extends SimpleGenericEvent<ServiceSelectorConfiguration> {
  public RegisterServiceSelectorEvent(final ServiceSelectorConfiguration value) {
    super(value);
  }
}
