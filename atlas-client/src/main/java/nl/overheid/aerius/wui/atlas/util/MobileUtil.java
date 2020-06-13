package nl.overheid.aerius.wui.atlas.util;

import com.google.gwt.user.client.Window.Navigator;

public class MobileUtil {
  public static String[] MOBILE_USERAGENT_KEYS = new String[] {
      "iPad",
      "iPhone",
      "iPod",
      "Android",
      "Touch"
  };

  public static boolean isDeviceMobile() {
    final String userAgent = Navigator.getUserAgent();

    for (final String key : MOBILE_USERAGENT_KEYS) {
      if (userAgent.contains(key)) {
        return true;
      }
    }

    return false;
  }
}
