package nl.overheid.aerius.wui.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public final class SimpleTextCallback extends AbstractRequestCallback<String> {
  public SimpleTextCallback(final AsyncCallback<String> callback) {
    super(callback);
  }

  @Override
  protected String processResponse(final String resp) {
    return resp;
  }
}