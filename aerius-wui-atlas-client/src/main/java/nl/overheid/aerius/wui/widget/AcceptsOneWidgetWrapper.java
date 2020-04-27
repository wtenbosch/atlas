package nl.overheid.aerius.wui.widget;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;

public class AcceptsOneWidgetWrapper implements AcceptsOneWidget {
  private final Panel panel;

  public AcceptsOneWidgetWrapper(final Panel panel) {
    this.panel = panel;
  }

  public static AcceptsOneWidgetWrapper of(final Panel panel) {
    return new AcceptsOneWidgetWrapper(panel);
  }

  @Override
  public void setWidget(final IsWidget w) {
    panel.clear();
    panel.add(w);
  }
}
