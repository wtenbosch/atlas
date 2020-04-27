package nl.overheid.aerius.wui.atlas.ui.context.info;

import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.wui.atlas.ui.context.MonitorUpPanel;
import nl.overheid.aerius.wui.widget.EventComposite;

public abstract class BasicEventPanelComposite extends EventComposite implements MonitorUpPanel {
  protected PanelContent content;

  @Override
  public void setPanelContent(final PanelContent content) {
    this.content = content;
  }

  @Override
  public boolean hasPanelContent() {
    return content != null;
  }
}
