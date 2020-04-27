package nl.overheid.aerius.wui.atlas.service.parser;

import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.wui.atlas.service.parser.CommonJson.JSONObjectHandle;
import nl.overheid.aerius.wui.domain.auth.AuthorizationInfo;
import nl.overheid.aerius.wui.future.JsonAsyncCallback;

public class OAuthJsonParser implements AsyncCallback<JSONValue> {
  private final AsyncCallback<AuthorizationInfo> callback;

  private OAuthJsonParser(final AsyncCallback<AuthorizationInfo> callback) {
    this.callback = callback;
  }

  @Override
  public void onSuccess(final JSONValue result) {
    callback.onSuccess(parse(new JSONObjectHandle(result.isObject())));
  }

  private AuthorizationInfo parse(final JSONObjectHandle json) {
    final AuthorizationInfo info = new AuthorizationInfo();

    info.setTokenType(json.getString("token_type"));
    info.setExpiresIn(json.getLong("expires_in"));
    info.setAccessToken(json.getString("access_token"));
    info.setRefreshToken(json.getString("refresh_token"));

    return info;
  }

  @Override
  public void onFailure(final Throwable caught) {
    callback.onFailure(caught);
  }

  public static AsyncCallback<JSONValue> convert(final AsyncCallback<AuthorizationInfo> callback) {
    return new OAuthJsonParser(callback);
  }

  public static RequestCallback wrap(final AsyncCallback<AuthorizationInfo> callback) {
    return JsonAsyncCallback.create(convert(callback));
  }
}
