package nl.overheid.aerius.wui.atlas.daemon.auth;

import java.util.stream.Stream;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

import nl.overheid.aerius.wui.config.EnvironmentConfiguration;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.domain.auth.UserCredentials;

public final class BasicAuthUtil {
  private static final String BASIC_AUTH_ENDPOINT_FALLBACK = "https://dev.aerius.nl/connect";

  private BasicAuthUtil() {}

  public static void handleBasicAuthLogin(final EnvironmentConfiguration cfg, final UserCredentials value) {
    String host = cfg.getBasicAuthEndpoint();
    if ("${aerius.monitor.config.service.basicAuth.endpoint}".equals(host)) {
      host = BASIC_AUTH_ENDPOINT_FALLBACK;
      GWTProd.warn("Falling back to hard-coded basic auth e. Please configure the 'aerius.monitor.config.service.basicAuth.endpoint' filter param.");
    }

    final String formattedCreds = value.getUsername() + ":" + value.getPassword();
    final String encodedCreds = b64encode(formattedCreds);

    Stream.of(host.split(",")).forEach(v -> handleBasicAuthLogin(v, encodedCreds));
  }

  private static void handleBasicAuthLogin(final String host, final String auth) {
    if (host.isEmpty()) {
      return;
    }

    final RequestBuilder bldr = new RequestBuilder(RequestBuilder.GET, host);
    bldr.setHeader("Authorization", "Basic " + auth);
    bldr.setCallback(new RequestCallback() {
      @Override
      public void onResponseReceived(final Request request, final Response response) {
        GWTProd.log("Completed basic auth setup.");
      }

      @Override
      public void onError(final Request request, final Throwable exception) {
        GWTProd.log("Failed to setup basic auth.");
      }
    });

    try {
      bldr.send();
    } catch (final RequestException e) {
      GWTProd.log("Could not send basic auth request: " + e.getMessage());
    }
  }

  public static void ensureBasicAuth() {
    // TODO Ensure basic auth still works (
    GWTProd.log("STUB", "ensureBasicAuth()");
  }

  private static native String b64encode(String a) /*-{
                                                   return window.btoa(a);
                                                   }-*/;
}
