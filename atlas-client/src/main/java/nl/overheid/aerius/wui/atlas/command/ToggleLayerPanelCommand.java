package nl.overheid.aerius.wui.atlas.command;

import nl.overheid.aerius.wui.atlas.event.ToggleLayerPanelEvent;
import nl.overheid.aerius.wui.command.SimpleGenericCommand;

public class ToggleLayerPanelCommand extends SimpleGenericCommand<Boolean, ToggleLayerPanelEvent> {
  public ToggleLayerPanelCommand(final boolean value) {
    super(value);
  }

  @Override
  protected ToggleLayerPanelEvent createEvent(final Boolean value) {
    return new ToggleLayerPanelEvent(value);
  }
}
