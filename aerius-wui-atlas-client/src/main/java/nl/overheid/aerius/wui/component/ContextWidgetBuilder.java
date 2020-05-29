package nl.overheid.aerius.wui.component;

import com.google.inject.ImplementedBy;

import nl.overheid.aerius.shared.domain.PanelConfiguration;
import nl.overheid.aerius.wui.atlas.ui.context.MonitorUpPanel;

@ImplementedBy(ContextWidgetBuilderImpl.class)
public interface ContextWidgetBuilder {
  MonitorUpPanel createContextPanel(PanelConfiguration conf);
}
