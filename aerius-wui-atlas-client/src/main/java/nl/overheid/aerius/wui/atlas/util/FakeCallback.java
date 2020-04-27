package nl.overheid.aerius.wui.atlas.util;

import java.util.function.Function;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.wui.service.BaseRequestCallback;

public class FakeCallback<T> implements Function<AsyncCallback<T>, RequestCallback> {
  private final T ret;

  public FakeCallback(final T ret) {
    this.ret = ret;
  }

  public static <T> FakeCallback<T> create(final T ret) {
    return new FakeCallback<T>(ret);
  }

  @Override
  public RequestCallback apply(final AsyncCallback<T> callback) {
    return new BaseRequestCallback() {
      @Override
      public void onResponseReceived(final Request request, final Response response) {
        if (validateResponse(request, response)) {
          return;
        }

        callback.onSuccess(ret);
      }

      @Override
      public void onError(final Request request, final Throwable exception) {
        callback.onFailure(exception);
      }
    };
  }
}
