package nl.overheid.aerius.wui.atlas.service.parser;

import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.shared.domain.Story;

public class StoryJsonParser implements AsyncCallback<String> {
  private final AsyncCallback<Story> callback;

  private StoryJsonParser(final AsyncCallback<Story> callback) {
    this.callback = callback;
  }

  @Override
  public void onSuccess(final String result) {
    parse(JSONObjectHandle.fromText(result));
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

  public static AsyncCallback<String> convert(final AsyncCallback<Story> callback) {
    return new StoryJsonParser(callback);
  }
}
