package nl.overheid.aerius.wui.atlas.ui.context.info;

import com.google.gwt.user.client.ui.Composite;

import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.wui.atlas.ui.context.MonitorUpPanel;

public abstract class BasicPanelComposite extends Composite implements MonitorUpPanel {
  private boolean hasPanelContent;

  @Override
  public void setPanelContent(final PanelContent content) {
    setHasPanelContent(content != null);
  }

  protected void setHasPanelContent(final boolean hasPanelContent) {
    this.hasPanelContent = hasPanelContent;
  }

  @Override
  public boolean hasPanelContent() {
    return hasPanelContent;
  }
}
