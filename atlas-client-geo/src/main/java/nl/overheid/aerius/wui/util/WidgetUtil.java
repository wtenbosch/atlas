package nl.overheid.aerius.wui.util;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.geo.wui.Map;

public final class WidgetUtil {
  private WidgetUtil() {}

  public static Widget asWidgetIfWidget(final Map map) {
    return map instanceof IsWidget ? ((IsWidget) map).asWidget() : null;
  }
}
