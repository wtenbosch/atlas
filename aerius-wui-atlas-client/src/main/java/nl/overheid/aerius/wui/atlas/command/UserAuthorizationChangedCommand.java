package nl.overheid.aerius.wui.atlas.command;

import nl.overheid.aerius.wui.atlas.event.UserAuthorizationChangedEvent;
import nl.overheid.aerius.wui.command.SimpleGenericCommand;
import nl.overheid.aerius.wui.domain.auth.AuthorizationInfo;

public class UserAuthorizationChangedCommand extends SimpleGenericCommand<AuthorizationInfo, UserAuthorizationChangedEvent> {
  public UserAuthorizationChangedCommand(final AuthorizationInfo value) {
    super(value);
  }

  @Override
  protected UserAuthorizationChangedEvent createEvent(final AuthorizationInfo value) {
    return new UserAuthorizationChangedEvent(value);
  }
}
