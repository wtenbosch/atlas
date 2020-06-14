package nl.overheid.aerius.wui.future;

import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class JsonParserCallback<T> implements AsyncCallback<JSONValue> {
  private final AsyncCallback<T> callback;

  public JsonParserCallback(final AsyncCallback<T> callback) {
    this.callback = callback;
  }

  @Override
  public void onSuccess(final JSONValue result) {
    callback.onSuccess(parse(result));
  }

  protected abstract T parse(JSONValue result);

  @Override
  public void onFailure(final Throwable caught) {
    callback.onFailure(caught);
  }
}
