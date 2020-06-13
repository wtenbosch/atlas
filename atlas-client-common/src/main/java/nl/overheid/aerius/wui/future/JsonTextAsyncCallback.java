package nl.overheid.aerius.wui.future;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class JsonTextAsyncCallback extends SimpleDebuggableRequest implements RequestCallback {
  private final AsyncCallback<String> callback;

  private JsonTextAsyncCallback(final AsyncCallback<String> callback) {
    this.callback = callback;
  }

  @Override
  public void onResponseReceived(final Request request, final Response response) {
    if (validateResponse(request, response)) {
      return;
    }

    try {
      try {
        callback.onSuccess(response.getText());
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

  public static RequestCallback create(final AsyncCallback<String> callback) {
    return new JsonTextAsyncCallback(callback);
  }
}
