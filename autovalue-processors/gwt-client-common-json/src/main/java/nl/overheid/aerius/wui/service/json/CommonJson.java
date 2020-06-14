package nl.overheid.aerius.wui.service.json;

import com.google.gwt.json.client.JSONObject;

public class CommonJson {
  public static String getString(final JSONObject obj, final String key) {
    return new JSONObjectHandle(obj).getString(key);
  }

  public static JSONObjectHandle getObject(final JSONObject obj, final String key) {
    return new JSONObjectHandle(obj).getObject(key);
  }

  public static JSONArrayHandle getArray(final JSONObject obj, final String key) {
    return new JSONObjectHandle(obj).getArray(key);
  }

  public static String getStringOrDefault(final JSONObject obj, final String key, final String devault) {
    try {
      return new JSONObjectHandle(obj).getString(key);
    } catch (final IllegalStateException e) {
      return devault;
    }
  }
}
