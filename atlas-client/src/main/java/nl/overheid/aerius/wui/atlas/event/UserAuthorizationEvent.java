package nl.overheid.aerius.wui.atlas.event;

import nl.overheid.aerius.wui.domain.auth.UserCredentials;
import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class UserAuthorizationEvent extends SimpleGenericEvent<UserCredentials> {
  public UserAuthorizationEvent(final UserCredentials value) {
    super(value);
  }
}
