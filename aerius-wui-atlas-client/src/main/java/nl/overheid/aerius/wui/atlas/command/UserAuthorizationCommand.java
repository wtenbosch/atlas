package nl.overheid.aerius.wui.atlas.command;

import nl.overheid.aerius.wui.atlas.event.UserAuthorizationEvent;
import nl.overheid.aerius.wui.command.SimpleGenericCommand;
import nl.overheid.aerius.wui.domain.auth.UserCredentials;

public class UserAuthorizationCommand extends SimpleGenericCommand<UserCredentials, UserAuthorizationEvent> {
  public UserAuthorizationCommand(final UserCredentials value) {
    super(value);
  }

  @Override
  protected UserAuthorizationEvent createEvent(final UserCredentials value) {
    return new UserAuthorizationEvent(value);
  }

}
