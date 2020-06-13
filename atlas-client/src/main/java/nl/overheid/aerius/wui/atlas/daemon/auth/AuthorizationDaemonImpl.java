package nl.overheid.aerius.wui.atlas.daemon.auth;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.wui.atlas.command.UserAuthorizationChangedCommand;
import nl.overheid.aerius.wui.config.EnvironmentConfiguration;
import nl.overheid.aerius.wui.domain.auth.AuthContext;
import nl.overheid.aerius.wui.domain.auth.AuthorizationInfo;
import nl.overheid.aerius.wui.domain.cache.CacheContext;
import nl.overheid.aerius.wui.domain.story.StoryContext;

public class AuthorizationDaemonImpl extends BasicAuthorizationDaemon implements AuthorizationDaemon {
  private static final boolean NO_MEMORY_MODE = true;

  @Inject
  public AuthorizationDaemonImpl(final EnvironmentConfiguration cfg, final AuthContext authContext, final StoryContext storyContext,
      final CacheContext cacheContext) {
    super(cfg, authContext, storyContext, cacheContext);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    restoreCookie();
  }

  private void restoreCookie() {
    // Don't restore from cookie
    if (NO_MEMORY_MODE) {
      return;
    }

    final AuthorizationInfo info = new AuthorizationInfo();

    final String accessToken = Cookies.getCookie(COOKIE_ACCESS_TOKEN);
    final String refreshToken = Cookies.getCookie(COOKIE_REFRESH_TOKEN);

    info.setAccessToken(accessToken);
    info.setRefreshToken(refreshToken);

    if (accessToken != null) {
      GWT.log("Restored OAuth session from cookie");
      Scheduler.get().scheduleDeferred(() -> {
        eventBus.fireEvent(new UserAuthorizationChangedCommand(info));
      });
    }
  }
}
