package nl.overheid.aerius.wui.atlas.service.parser;

import java.util.Optional;

import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.wui.atlas.service.parser.CommonJson.JSONObjectHandle;
import nl.overheid.aerius.wui.future.JsonAsyncCallback;

public final class BboxJsonParser implements AsyncCallback<JSONValue> {
  private final AsyncCallback<BboxContainer> callback;

  public BboxJsonParser(final AsyncCallback<BboxContainer> callback) {
    this.callback = callback;
  }

  @Override
  public void onSuccess(final JSONValue result) {
    parse(new JSONObjectHandle(result.isObject()));
  }

  private void parse(final JSONObjectHandle object) {
    final Optional<String> str = object.getStringOptional("bbox");

    if (str.isPresent()) {
      
      final BboxContainer cont = new BboxContainer();
      cont.setBbox(str.get());
      cont.setTitle(object.getString("title"));
      
      callback.onSuccess(cont);
    } else {
      callback.onFailure(new RuntimeException("Could not parse area bbox."));
    }
  }

  @Override
  public void onFailure(final Throwable caught) {
    callback.onFailure(caught);
  }

  public static AsyncCallback<JSONValue> convert(final AsyncCallback<BboxContainer> callback) {
    return new BboxJsonParser(callback);
  }

  public static RequestCallback wrap(final AsyncCallback<BboxContainer> callback) {
    return JsonAsyncCallback.create(convert(callback));
  }
}
