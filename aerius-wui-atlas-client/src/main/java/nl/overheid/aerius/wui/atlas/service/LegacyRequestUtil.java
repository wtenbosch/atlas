package nl.overheid.aerius.wui.atlas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.wui.config.EnvironmentConfiguration;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.domain.auth.AuthorizationInfo;
import nl.overheid.aerius.wui.future.DebuggableRequest;
import nl.overheid.aerius.wui.util.TemplatedString;

public class LegacyRequestUtil {
  private static final int CLIENT_TIMEOUT = 30000;

  private static String rerouteCmsRequest;

  private static EnvironmentConfiguration cfg;

  public static void init(final EnvironmentConfiguration cfg) {
    LegacyRequestUtil.cfg = cfg;
  }

  public static void rerouteCmsRequests(final String uri) {
    GWTProd.warn("Rerouting CMS requests to: " + uri);
    rerouteCmsRequest = uri;
  }

  public static String getRerouter() {
    return rerouteCmsRequest;
  }

  public static <T> void doMethodGet(final String methodName, final Function<AsyncCallback<T>, RequestCallback> parser,
      final AsyncCallback<T> callback, final String... keyvalues) {
    doAuthenticatedMethodGet(null, methodName, parser, callback, keyvalues);
  }

  public static <T> void doAuthenticatedMethodGet(final AuthorizationInfo authInfo, final String methodName,
      final Function<AsyncCallback<T>, RequestCallback> parser, final AsyncCallback<T> callback, final String... keyvalues) {
    if (authInfo != null) {
      throw new RuntimeException("Authorization information present however this is not longer supported.");
    }

    final StringBuilder bldr = new StringBuilder("?");

    for (int i = 0; i < keyvalues.length; i += 2) {
      if (i != 0) {
        bldr.append("&");
      }

      bldr.append(keyvalues[i]);

      if (keyvalues.length > i + 1) {
        bldr.append("=");
        bldr.append(keyvalues[i + 1]);
      }
    }

    doCmsMethodGet(methodName + bldr.toString(), parser, callback);
  }

  public static <T> void doCmsMethodGet(final String methodName,
      final Function<AsyncCallback<T>, RequestCallback> parser, final AsyncCallback<T> callback) {
    final RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, cms() + methodName);
    if (cfg.getBasicAuthCredentials() != null) {
      builder.setHeader("Authorization", "Basic " + cfg.getBasicAuthCredentials());
    }

    final RequestCallback rawCallback = parser.apply(callback);
    installDebugging(builder.getUrl(), rawCallback);

    builder.setTimeoutMillis(CLIENT_TIMEOUT);
    builder.setCallback(rawCallback);
    try {
      builder.send();
    } catch (final RequestException e) {
      callback.onFailure(e);
    }
  }

  private static String cms() {
    if (rerouteCmsRequest != null) {
      return rerouteCmsRequest;
    } else {
      if (cfg == null) {
        throw new RuntimeException("RequestUtil not initialised with environment configuration.");
      }
      return cfg.getCmsEndpoint();
    }
  }

  private static void installDebugging(final String url, final RequestCallback rawCallback) {
    if (rawCallback instanceof DebuggableRequest) {
      ((DebuggableRequest) rawCallback).setRequestOrigin(url);
    }
  }

  public static <T> void doGet(final String url, final Function<AsyncCallback<T>, RequestCallback> parser, final AsyncCallback<T> callback) {
    final RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
    if (cfg.getBasicAuthCredentials() != null) {
      builder.setHeader("Authorization", "Basic " + cfg.getBasicAuthCredentials());
    }

    final RequestCallback rawCallback = parser.apply(callback);
    installDebugging(builder.getUrl(), rawCallback);

    builder.setTimeoutMillis(CLIENT_TIMEOUT);
    builder.setCallback(rawCallback);
    try {
      builder.send();
    } catch (final RequestException e) {
      callback.onFailure(e);
    }
  }

  public static String formatQueryStringPayload(final Map<String, String> map) {
    final List<String> parts = new ArrayList<>();

    for (final Entry<String, String> entry : map.entrySet()) {
      final StringBuilder bldr = new StringBuilder();
      bldr.append(entry.getKey());
      bldr.append("=");
      bldr.append(entry.getValue());
      parts.add(bldr.toString());
    }

    return parts.stream().collect(Collectors.joining("&"));
  }

  public static String formatJsonPayload(final Map<String, String> map) {
    final StringBuilder bldr = new StringBuilder();
    bldr.append("{");

    final List<String> parts = new ArrayList<>();
    for (final Entry<String, String> entry : map.entrySet()) {
      final StringBuilder inner = new StringBuilder();
      inner.append("\"");
      inner.append(entry.getKey());
      inner.append("\"");
      inner.append(":");
      inner.append("\"");
      inner.append(entry.getValue());
      inner.append("\"");

      parts.add(inner.toString());
    }

    bldr.append(parts.stream().collect(Collectors.joining(",")));

    bldr.append("}");

    return bldr.toString();
  }

  public static String prepareUrl(final String host, final String template, final String... args) {
    if (args.length % 2 != 0) {
      throw new RuntimeException("Template args are of incorrect size: " + args.length);
    }

    final TemplatedString bldr = new TemplatedString(host + template);
    for (int i = 0; i < args.length; i += 2) {
      bldr.replace(args[i], args[i + 1]);
    }

    return bldr.toString();
  }
}
