package nl.overheid.aerius.wui.bootstrapper;

import com.google.gwt.user.client.ui.IsWidget;

public interface BootstrapView extends IsWidget {
  void error();

  void onApplicationReady(Runnable finish);
}
