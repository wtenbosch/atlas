package nl.overheid.aerius.wui.atlas.event;

import nl.overheid.aerius.wui.domain.auth.AuthorizationInfo;
import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class UserAuthorizationChangedEvent extends SimpleGenericEvent<AuthorizationInfo> {
  public UserAuthorizationChangedEvent(final AuthorizationInfo value) {
    super(value);
  }
}
