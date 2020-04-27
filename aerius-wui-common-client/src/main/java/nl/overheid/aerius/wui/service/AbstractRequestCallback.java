package nl.overheid.aerius.wui.service;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractRequestCallback<T> extends BaseRequestCallback {
  private final AsyncCallback<T> callback;

  public AbstractRequestCallback(final AsyncCallback<T> callback) {
    this.callback = callback;
  }

  @Override
  public void onResponseReceived(final Request request, final Response response) {
    if (validateResponse(request, response)) {
      return;
    }

    callback.onSuccess(processResponse(response.getText()));
  }

  @Override
  public void onError(final Request request, final Throwable exception) {
    callback.onFailure(exception);
  }

  protected abstract T processResponse(String resp);
}