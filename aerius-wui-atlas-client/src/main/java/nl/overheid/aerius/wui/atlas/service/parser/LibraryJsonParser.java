package nl.overheid.aerius.wui.atlas.service.parser;

import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.shared.domain.NarrowLibrary;
import nl.overheid.aerius.wui.future.JsonAsyncCallback;

public final class LibraryJsonParser extends CommonJson implements AsyncCallback<JSONValue> {
  private final AsyncCallback<NarrowLibrary> callback;

  private LibraryJsonParser(final AsyncCallback<NarrowLibrary> callback) {
    this.callback = callback;
  }

  @Override
  public void onSuccess(final JSONValue result) {

    parse(new JSONObjectHandle(result.isObject()));
  }

  private void parse(final JSONObjectHandle object) {
    final int version = object.getIntegerOptional("version").orElse(1);

    switch (version) {
    case 1:
      nl.overheid.aerius.wui.atlas.service.parser.v1.LibraryJsonParser.parse(callback, object);
      break;
    case 2:
      nl.overheid.aerius.wui.atlas.service.parser.v2.LibraryJsonParser.parse(callback, object);
      break;
      default:
        throw new RuntimeException("No parser found for result: " + object);
    }
  }

  @Override
  public void onFailure(final Throwable caught) {
    callback.onFailure(caught);
  }

  public static AsyncCallback<JSONValue> convert(final AsyncCallback<NarrowLibrary> callback) {
    return new LibraryJsonParser(callback);
  }

  public static RequestCallback wrap(final AsyncCallback<NarrowLibrary> callback) {
    return JsonAsyncCallback.create(convert(callback));
  }
}
