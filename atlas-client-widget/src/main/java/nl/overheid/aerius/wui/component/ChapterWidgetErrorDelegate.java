package nl.overheid.aerius.wui.component;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import nl.overheid.aerius.shared.domain.Selector;

public class ChapterWidgetErrorDelegate implements PanelWidgetDelegate {
  @Inject
  public ChapterWidgetErrorDelegate(final @Assisted AcceptsOneWidget panel, @Assisted final String errorText) {
    panel.setWidget(new Label(errorText));
  }

  @Override
  public void notifySelector(final Selector selector) {}
}
