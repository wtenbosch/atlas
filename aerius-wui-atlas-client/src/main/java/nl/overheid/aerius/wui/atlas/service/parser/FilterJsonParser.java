package nl.overheid.aerius.wui.atlas.service.parser;

import java.util.List;
import java.util.Map;

import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.NatureArea;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson.JSONObjectHandle;
import nl.overheid.aerius.wui.future.JsonAsyncCallback;

public final class FilterJsonParser implements AsyncCallback<JSONValue> {
  private final AsyncCallback<Map<AreaGroupType, List<NatureArea>>> callback;

  private FilterJsonParser(final AsyncCallback<Map<AreaGroupType, List<NatureArea>>> callback) {
    this.callback = callback;
  }

  @Override
  public void onSuccess(final JSONValue result) {
    parse(new JSONObjectHandle(result.isObject()));
  }

  private void parse(final JSONObjectHandle object) {
    final String version = object.getStringOptional("version").orElse("1");

    switch (version) {
    case "1":
      nl.overheid.aerius.wui.atlas.service.parser.v1.FilterJsonParser.parse(callback, object);
      break;
    case "2":
      nl.overheid.aerius.wui.atlas.service.parser.v2.FilterJsonParser.parse(callback, object);
      break;
      default:
        throw new RuntimeException("No parser found for result: " + object);
    }
  }

  @Override
  public void onFailure(final Throwable caught) {
    callback.onFailure(caught);
  }

  public static AsyncCallback<JSONValue> convert(final AsyncCallback<Map<AreaGroupType, List<NatureArea>>> callback) {
    return new FilterJsonParser(callback);
  }

  public static RequestCallback wrap(final AsyncCallback<Map<AreaGroupType, List<NatureArea>>> callback) {
    return JsonAsyncCallback.create(convert(callback));
  }
}
