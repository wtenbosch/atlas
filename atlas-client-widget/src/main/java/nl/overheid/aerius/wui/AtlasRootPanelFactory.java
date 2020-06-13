package nl.overheid.aerius.wui;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

public class AtlasRootPanelFactory implements RootPanelFactory {
  @Override
  public Panel getPanel() {
    return RootPanel.get("root");
  }
}
