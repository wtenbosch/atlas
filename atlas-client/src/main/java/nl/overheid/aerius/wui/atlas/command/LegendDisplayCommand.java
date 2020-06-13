package nl.overheid.aerius.wui.atlas.command;

import nl.overheid.aerius.wui.atlas.event.LegendDisplayEvent;
import nl.overheid.aerius.wui.command.SimpleCommand;

public class LegendDisplayCommand extends SimpleCommand<LegendDisplayEvent> {
  @Override
  protected LegendDisplayEvent createEvent() {
    return new LegendDisplayEvent();
  }
}
