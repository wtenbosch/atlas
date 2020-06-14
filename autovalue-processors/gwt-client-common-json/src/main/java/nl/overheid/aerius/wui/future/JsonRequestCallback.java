package nl.overheid.aerius.wui.future;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class JsonRequestCallback extends SimpleDebuggableRequest implements RequestCallback {
  private final AsyncCallback<JSONValue> callback;

  private JsonRequestCallback(final AsyncCallback<JSONValue> callback) {
    this.callback = callback;
  }

  @Override
  public void onResponseReceived(final Request request, final Response response) {
    if (validateResponse(request, response)) {
      return;
    }

    try {
      final JSONValue parsed = JSONParser.parseStrict(response.getText());
      try {
        callback.onSuccess(parsed);
      } catch (final Exception e) {
        callback.onFailure(e);
      }
    } catch (final IllegalArgumentException e) {
      callback.onFailure(new RuntimeException("Failure while parsing json: " + origin));
    }
  }

  @Override
  public void onError(final Request request, final Throwable exception) {
    callback.onFailure(exception);
  }

  public static RequestCallback create(final AsyncCallback<JSONValue> callback) {
    return new JsonRequestCallback(callback);
  }
}
