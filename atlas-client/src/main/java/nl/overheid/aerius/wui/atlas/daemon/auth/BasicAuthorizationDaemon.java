package nl.overheid.aerius.wui.atlas.daemon.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.wui.atlas.command.StorySelectionChangeCommand;
import nl.overheid.aerius.wui.atlas.command.UserAuthorizationChangedCommand;
import nl.overheid.aerius.wui.atlas.command.UserAuthorizationCommand;
import nl.overheid.aerius.wui.atlas.event.UserAuthorizationChangedEvent;
import nl.overheid.aerius.wui.atlas.service.LegacyRequestUtil;
import nl.overheid.aerius.wui.atlas.service.parser.OAuthJsonParser;
import nl.overheid.aerius.wui.atlas.util.TimeUtil;
import nl.overheid.aerius.wui.config.EnvironmentConfiguration;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.domain.auth.AuthContext;
import nl.overheid.aerius.wui.domain.auth.AuthorizationInfo;
import nl.overheid.aerius.wui.domain.auth.UserCredentials;
import nl.overheid.aerius.wui.domain.cache.CacheContext;
import nl.overheid.aerius.wui.domain.story.StoryContext;
import nl.overheid.aerius.wui.event.BasicEventComponent;
import nl.overheid.aerius.wui.future.AppAsyncCallback;
import nl.overheid.aerius.wui.i18n.AtlasM;

public class BasicAuthorizationDaemon extends BasicEventComponent implements AuthorizationDaemon {
  protected static final String COOKIE_ACCESS_TOKEN = "aerius_oauth_access_token";
  protected static final String COOKIE_REFRESH_TOKEN = "aerius_oauth_refresh_token";

  interface AuthorizationDaemonImplEventBinder extends EventBinder<BasicAuthorizationDaemon> {}

  private final AuthorizationDaemonImplEventBinder EVENT_BINDER = GWT.create(AuthorizationDaemonImplEventBinder.class);

  private final AuthContext context;
  private final StoryContext storyContext;
  private final CacheContext cacheContext;

  private final EnvironmentConfiguration cfg;

  @Inject
  public BasicAuthorizationDaemon(final EnvironmentConfiguration cfg, final AuthContext authContext, final StoryContext storyContext,
      final CacheContext cacheContext) {
    this.cfg = cfg;
    this.context = authContext;
    this.storyContext = storyContext;
    this.cacheContext = cacheContext;
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }

  @EventHandler
  public void onAuthorizeUserCommand(final UserAuthorizationCommand c) {
    handleOauth(c.getValue());
    handleLoginCookie(c.getValue());
  }

  @EventHandler
  public void onUserAuthorizationChangedCommand(final UserAuthorizationChangedCommand c) {
    final AuthorizationInfo info = c.getValue();

    setAuthInfo(info);
    handleCookie(info);
  }

  @EventHandler
  public void onUserAuthorizationChangedEvent(final UserAuthorizationChangedEvent e) {
    if (e.getValue() != null) {
      refreshStory();
    }
  }

  private void handleLoginCookie(final UserCredentials c) {
    forgetLoginCookie();

    final Map<String, String> payload = new HashMap<>();
    payload.put("name", c.getUsername());
    payload.put("pass", c.getPassword());

    final RequestBuilder bldr = new RequestBuilder(RequestBuilder.POST, cfg.getLoginEndpoint());
    bldr.setHeader("Accept", "application/json");
    bldr.setHeader("Content-Type", "application/json");
    bldr.setRequestData(LegacyRequestUtil.formatJsonPayload(payload));
    bldr.setCallback(new RequestCallback() {
      @Override
      public void onResponseReceived(final Request request, final Response response) {
        GWT.log("Received login cookie successfully.");
      }

      @Override
      public void onError(final Request request, final Throwable exception) {
        GWT.log("Received login cookie unsuccessfully.");
      }
    });

    try {
      bldr.send();
    } catch (final RequestException e) {
      fail(e, AtlasM.messages().loginClientError());
    }
  }

  private void handleOauth(final UserCredentials c) {
    final Map<String, String> payload = new HashMap<>();
    payload.put("grant_type", "password");
    payload.put("client_id", cfg.getAuthenticationClientId());
    payload.put("client_secret", cfg.getAuthenticationClientSecret());
    payload.put("username", c.getUsername());
    payload.put("password", c.getPassword());
    payload.put("scope", "technical_editor editor organisation_member");

    // final RequestBuilder bldr = new RequestBuilder(RequestBuilder.POST, ServiceEndPoint.I.format("oauth/token"));
    final RequestBuilder bldr = new RequestBuilder(RequestBuilder.POST, cfg.getAuthenticationEndpoint());
    bldr.setHeader("Content-Type", "application/x-www-form-urlencoded");
    bldr.setRequestData(LegacyRequestUtil.formatQueryStringPayload(payload));

    bldr.setCallback(
        OAuthJsonParser.wrap(AppAsyncCallback.create((final AuthorizationInfo i) -> succeed(i), e -> fail(e, AtlasM.messages().loginWrongAuth()))));

    try {
      bldr.send();
    } catch (final RequestException e) {
      GWTProd.log(e.getMessage());
      fail(e, AtlasM.messages().loginClientError());
    }
  }

  private void succeed(final AuthorizationInfo i) {
    eventBus.fireEvent(new UserAuthorizationChangedCommand(i));
  }

  private void refreshStory() {
    final Story story = storyContext.getStory();

    final StorySelectionChangeCommand cmd = new StorySelectionChangeCommand(story);

    eventBus.fireEvent(cmd);
  }

  private void fail(final Throwable e, final String str) {
    eventBus.fireEvent(new UserAuthorizationChangedEvent(null));
  }

  private void handleCookie(final AuthorizationInfo info) {
    if (info == null) {
      forgetCookieInfo();
    } else {
      persistCookieInfo(info);
    }
  }

  private void persistCookieInfo(final AuthorizationInfo info) {
    if (info.getExpiresIn() == 0) {
      return;
    }

    final Date now = new Date();
    final long future = now.getTime() + info.getExpiresIn() * 1000;
    final Date then = new Date();
    then.setTime(future);

    Cookies.setCookie(COOKIE_ACCESS_TOKEN, info.getAccessToken(), then);
    Cookies.setCookie(COOKIE_REFRESH_TOKEN, info.getRefreshToken(), then);

    Cookies.setCookie(AuthContext.AERIUS_LOGGED_IN_RECENTLY, "true", new Date(System.currentTimeMillis() + TimeUtil.WEEK));

  }

  private void forgetCookieInfo() {
    Cookies.removeCookie(COOKIE_ACCESS_TOKEN);
    Cookies.removeCookie(COOKIE_REFRESH_TOKEN);

    @Deprecated
    final String endpoint = cfg.getLogoutEndpoint().isEmpty()
        ? "https://test-monitor.aerius.nl/user/logout"
        : cfg.getLogoutEndpoint();

    final RequestBuilder bldr = new RequestBuilder(RequestBuilder.GET, endpoint);
    bldr.setCallback(new RequestCallback() {
      @Override
      public void onResponseReceived(final Request request, final Response response) {
        GWTProd.log("Logout call successful.");
        GWTProd.log("Performing post-logout checks.");
        finalLogoutChecks();
        GWTProd.log("Logout complete.");
      }

      @Override
      public void onError(final Request request, final Throwable exception) {
        GWTProd.log("Could not log out: " + exception);
        GWTProd.log("Attempting contingencies.");
        finalLogoutChecks();
      }
    });
    try {
      bldr.send();
    } catch (final RequestException e) {
      GWTProd.log("Failed sending logout request.");
      GWTProd.log("Attempting contingencies.");
      finalLogoutChecks();
    }
  }

  private void finalLogoutChecks() {
    forgetLoginCookie();

    Window.Location.reload();
  }

  private void forgetLoginCookie() {
    for (final String s : Cookies.getCookieNames()) {
      if (s.startsWith("SESS")) {
        GWTProd.log("Warning: Session cookies present after logging out: " + s + " (forcefully forgetting)");
        Cookies.removeCookie(s);
      }
    }
  }

  private void setAuthInfo(final AuthorizationInfo info) {
    if (info == null || info.getAccessToken() == null) {
      context.setAuthorized(false);
      context.setAuthInfo(null);
      cacheContext.setCaching(true);
    } else {
      context.setAuthorized(true);
      context.setAuthInfo(info);
      cacheContext.setCaching(false);
    }
  }
}
