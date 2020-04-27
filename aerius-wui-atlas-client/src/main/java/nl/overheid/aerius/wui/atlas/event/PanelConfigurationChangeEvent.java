package nl.overheid.aerius.wui.atlas.event;

import java.util.Map;

import nl.overheid.aerius.shared.domain.PanelConfiguration;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class PanelConfigurationChangeEvent extends SimpleGenericEvent<Map<PanelNames, PanelConfiguration>> {
  public PanelConfigurationChangeEvent() {}

  public PanelConfigurationChangeEvent(final Map<PanelNames, PanelConfiguration> value) {
    super(value);
  }
}
