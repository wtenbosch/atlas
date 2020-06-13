package nl.overheid.aerius.wui.atlas.future;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Simple wrapper around a callback providing callback obsolescence via a concurrency token.
 */
public class HardenedConcurrentCallback<T> implements AsyncCallback<T> {
  private final String callToken;
  private final ProvidesConcurrencyToken tokenProvider;
  private final AsyncCallback<T> callback;
  private final String identifier;

  public HardenedConcurrentCallback(final String callToken, final ProvidesConcurrencyToken tokenProvider,
      final AsyncCallback<T> callback) {
    this(null, callToken, tokenProvider, callback);
  }

  public HardenedConcurrentCallback(final String identifier, final String callToken, final ProvidesConcurrencyToken tokenProvider,
      final AsyncCallback<T> callback) {
    this.identifier = identifier;
    this.callToken = callToken;
    this.tokenProvider = tokenProvider;
    this.callback = callback;
  }

  @Override
  public void onSuccess(final T result) {
    if (!callToken.equals(tokenProvider.getStateToken(identifier))) {
      return;
    }

    callback.onSuccess(result);
  }
  
  @Override
    public void onFailure(final Throwable caught) {
      callback.onFailure(caught);
    }

  public static <T> AsyncCallback<T> wrap(final String identifier, final String callToken, final ProvidesConcurrencyToken tokenProvider,
      final AsyncCallback<T> c) {
    return new HardenedConcurrentCallback<T>(identifier, callToken, tokenProvider, c);
  }

  public static <T> AsyncCallback<T> wrap(final String callToken, final ProvidesConcurrencyToken tokenProvider, final AsyncCallback<T> c) {
    return new HardenedConcurrentCallback<T>(callToken, tokenProvider, c);
  }
}
