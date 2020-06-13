package nl.overheid.aerius.wui.atlas.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;

public final class TimeUtil {
  public static final long WEEK = 7 * 24 * 60 * 60 * 1000;

  private static final String D_M_YYYY = "yyyy-MM-dd";

  private TimeUtil() {}

  public static Date fromDateString(final String date) {
    return DateTimeFormat.getFormat(D_M_YYYY ).parse(date);
  }

  public static Date fromISO8601String(final String date) {
    return DateTimeFormat.getFormat(PredefinedFormat.ISO_8601).parse(date);
  }
}
