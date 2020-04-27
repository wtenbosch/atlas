package nl.overheid.aerius.wui.atlas.service.parser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ProxiedCallback<T> implements AsyncCallback<T> {
  private final AsyncCallback<T> parent;

  private final Runnable run;

  public ProxiedCallback(final AsyncCallback<T> parent, final Runnable run) {
    this.parent = parent;
    this.run = run;
  }

  public static <T> AsyncCallback<T> wrap(final AsyncCallback<T> parent, final Runnable run) {
    return new ProxiedCallback<T>(parent, run);
  }

  @Override
  public void onSuccess(final T result) {
    parent.onSuccess(result);
    run.run();
  }

  @Override
  public void onFailure(final Throwable caught) {
    parent.onFailure(caught);
    run.run();
  }
}
