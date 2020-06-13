package nl.overheid.aerius.wui.service;

import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

public final class JSONCallback extends AbstractRequestCallback<JSONValue> {
  public JSONCallback(final AsyncCallback<JSONValue> callback) {
    super(callback);
  }

  @Override
  protected JSONValue processResponse(final String resp) {
    return JSONParser.parseStrict(resp);
  }
}