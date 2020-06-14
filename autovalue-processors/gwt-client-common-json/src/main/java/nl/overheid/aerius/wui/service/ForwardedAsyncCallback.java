package nl.overheid.aerius.wui.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class ForwardedAsyncCallback<F, C> implements AsyncCallback<C> {
  protected AsyncCallback<F> callback;

  public ForwardedAsyncCallback(final AsyncCallback<F> callback) {
    this.callback = callback;
  }

  @Override
  public void onFailure(final Throwable caught) {
    callback.onFailure(caught);
  }

  @Override
  public void onSuccess(final C result) {
    callback.onSuccess(convert(result));
  }

  public abstract F convert(C content);
}
