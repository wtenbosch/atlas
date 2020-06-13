package nl.overheid.aerius.wui.atlas.service.parser;

import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.wui.future.JsonAsyncCallback;

public class ChapterTextJsonParser extends CommonJson implements AsyncCallback<JSONValue> {
  private final AsyncCallback<String> callback;

  private ChapterTextJsonParser(final AsyncCallback<String> callback) {
    this.callback = callback;
  }

  @Override
  public void onSuccess(final JSONValue result) {
    parse(new JSONObjectHandle(result.isObject()));
  }

  private void parse(final JSONObjectHandle object) {
    final int version = object.getIntegerOptional("version").orElse(1);

    final String chapterText;

    switch (version) {
    case 3:
      chapterText = nl.overheid.aerius.wui.atlas.service.parser.v3.ChapterTextJsonParser.parse(object);
      break;
    default:
      throw new RuntimeException("No parser found for version: " + version);
    }

    callback.onSuccess(chapterText);
  }

  @Override
  public void onFailure(final Throwable caught) {
    callback.onFailure(caught);
  }

  public static AsyncCallback<JSONValue> convert(final AsyncCallback<String> callback) {
    return new ChapterTextJsonParser(callback);
  }

  public static RequestCallback wrap(final AsyncCallback<String> callback) {
    return JsonAsyncCallback.create(convert(callback));
  }
}
