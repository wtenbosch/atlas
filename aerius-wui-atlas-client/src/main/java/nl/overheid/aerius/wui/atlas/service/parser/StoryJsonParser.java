package nl.overheid.aerius.wui.atlas.service.parser;

import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.wui.future.JsonAsyncCallback;

public class StoryJsonParser extends CommonJson implements AsyncCallback<JSONValue> {
  private final AsyncCallback<Story> callback;

  private StoryJsonParser(final AsyncCallback<Story> callback) {
    this.callback = callback;
  }

  @Override
  public void onSuccess(final JSONValue result) {
    parse(new JSONObjectHandle(result.isObject()));
  }

  private void parse(final JSONObjectHandle object) {
    final int version = object.getIntegerOptional("version").orElse(1);

    Story story;

    switch (version) {
    case 1:
      story = nl.overheid.aerius.wui.atlas.service.parser.v1.StoryJsonParser.parse(object);
      break;
    case 2:
      story = nl.overheid.aerius.wui.atlas.service.parser.v2.StoryJsonParser.parse(object);
      break;
    default:
      throw new RuntimeException("No parser found for result: " + object);
    }

    callback.onSuccess(story);
  }

  @Override
  public void onFailure(final Throwable caught) {
    callback.onFailure(caught);
  }

  public static AsyncCallback<JSONValue> convert(final AsyncCallback<Story> callback) {
    return new StoryJsonParser(callback);
  }

  public static RequestCallback wrap(final AsyncCallback<Story> callback) {
    return JsonAsyncCallback.create(convert(callback));
  }
}
