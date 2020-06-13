package nl.overheid.aerius.wui.atlas.service;

import java.util.Map;
import java.util.function.Function;

import com.google.gwt.user.client.rpc.AsyncCallback;

import elemental2.dom.FormData;
import elemental2.dom.XMLHttpRequest;

import nl.overheid.aerius.wui.config.EnvironmentConfiguration;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.util.TemplatedString;

public class RequestUtil {
  private static String rerouteCmsRequest;
  private static EnvironmentConfiguration cfg;

  public static void init(final EnvironmentConfiguration cfg) {
    RequestUtil.cfg = cfg;
  }

  public static void rerouteCmsRequests(final String uri) {
    GWTProd.warn("Rerouting CMS requests to: " + uri);
    rerouteCmsRequest = uri;
  }

  /** GET **/

  public static <T> void doGet(final String uri, final Function<AsyncCallback<T>, AsyncCallback<String>> parser, final AsyncCallback<T> callback) {
    doGet(uri, null, parser, callback);
  }

  public static <T> void doGet(final String uri, final Map<String, String> queryString,
      final Function<AsyncCallback<T>, AsyncCallback<String>> parser, final AsyncCallback<T> callback) {
    doGet(uri, queryString, parser.apply(callback));
  }

  public static <T> void doGet(final String uri, final AsyncCallback<String> callback) {
    doGet(uri, (Map<String, String>) null, callback);
  }

  public static <T> void doGet(final String uri, final Map<String, String> queryString, final AsyncCallback<String> callback) {
    doRequest("GET", uri + format(queryString), callback);
  }

  /** POST **/

  public static <T> void doPost(final String uri, final Function<AsyncCallback<T>, AsyncCallback<String>> parser, final AsyncCallback<T> callback) {
    doPost(uri, null, parser, callback);
  }

  public static void doPost(final String uri, final AsyncCallback<String> callback) {
    doPost(uri, (FormData) null, callback);
  }

  public static <T> void doPost(final String uri, final FormData payload, final Function<AsyncCallback<T>, AsyncCallback<String>> parser,
      final AsyncCallback<T> callback) {
    doPost(uri, payload, parser.apply(callback));
  }

  public static <T> void doPost(final String uri, final FormData payload, final AsyncCallback<String> callback) {
    doRequest("POST", uri, payload, callback);
  }

  /** DELETE **/

  public static <T> void doDelete(final String uri, final Function<AsyncCallback<T>, AsyncCallback<String>> parser, final AsyncCallback<T> callback) {
    doDelete(uri, null, parser, callback);
  }

  public static <T> void doDelete(final String uri, final FormData payload, final Function<AsyncCallback<T>, AsyncCallback<String>> parser,
      final AsyncCallback<T> callback) {
    doDelete(uri, payload, parser.apply(callback));
  }

  public static <T> void doDelete(final String uri, final FormData payload, final AsyncCallback<String> callback) {
    doRequest("DELETE", uri, callback);
  }

  /** REQUEST **/

  private static void doRequest(final String method, final String uri, final AsyncCallback<String> callback) {
    doRequest(method, uri, null, callback);
  }

  private static void doRequest(final String method, final String uri, final FormData payload, final AsyncCallback<String> callback) {
    final XMLHttpRequest req = new XMLHttpRequest();

    req.addEventListener("error", evt -> {
      handleError(callback, req.responseText);
    });
    req.addEventListener("load", evt -> {
      if (req.status != 200) {
        handleError(callback, req.responseText);
      } else {
        callback.onSuccess(req.responseText);
      }
    });

    final String host = cms();

    req.open(method, host + uri);
    req.send(payload);
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

  private static void handleError(final AsyncCallback<String> callback, final String responseText) {
    callback.onFailure(new HttpRequestException(responseText));
  }

  private static String format(final Map<String, String> queryString) {
    final StringBuilder bldr = new StringBuilder("?");
    if (queryString != null) {
      queryString.forEach((k, v) -> {
        bldr.append(k + "=" + v + "&");
      });
    }

    // Prune the last (either a & or ?)
    bldr.setLength(bldr.length() - 1);

    return bldr.toString();
  }

  public static String prepareUri(final String template, final String... args) {
    if (args.length % 2 != 0) {
      throw new RuntimeException("Template args are of incorrect size: " + args.length);
    }

    final TemplatedString bldr = new TemplatedString(template);
    for (int i = 0; i < args.length; i += 2) {
      bldr.replace(args[i], args[i + 1]);
    }

    return bldr.toString();
  }
}
