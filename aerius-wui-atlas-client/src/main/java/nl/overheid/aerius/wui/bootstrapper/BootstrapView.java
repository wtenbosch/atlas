package nl.overheid.aerius.wui.bootstrapper;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(FancyBootstrapLoadingView.class)
public interface BootstrapView extends IsWidget {
  void error();

  void onApplicationReady(Runnable finish);
}
