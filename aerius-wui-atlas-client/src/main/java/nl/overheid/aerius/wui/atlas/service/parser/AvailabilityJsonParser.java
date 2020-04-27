package nl.overheid.aerius.wui.atlas.service.parser;

import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.wui.future.JsonAsyncCallback;

public class AvailabilityJsonParser extends CommonJson implements AsyncCallback<JSONValue> {
  private final AsyncCallback<Boolean> callback;

  private AvailabilityJsonParser(final AsyncCallback<Boolean> callback) {
    this.callback = callback;
  }

  @Override
  public void onSuccess(final JSONValue result) {
    callback.onSuccess(parse(result.isObject()));
  }

  private Boolean parse(final JSONObject object) {
    return new JSONObjectHandle(object).getBoolean("result");
  }

  @Override
  public void onFailure(final Throwable caught) {
    callback.onFailure(caught);
  }

  public static AsyncCallback<JSONValue> convert(final AsyncCallback<Boolean> callback) {
    return new AvailabilityJsonParser(callback);
  }

  public static RequestCallback wrap(final AsyncCallback<Boolean> callback) {
    return JsonAsyncCallback.create(convert(callback));
  }
}
