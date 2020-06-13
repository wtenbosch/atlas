package nl.overheid.aerius.wui.atlas.command;

import nl.overheid.aerius.wui.atlas.event.LegendHiddenEvent;
import nl.overheid.aerius.wui.command.SimpleCommand;

public class LegendHiddenCommand extends SimpleCommand<LegendHiddenEvent> {
  @Override
  protected LegendHiddenEvent createEvent() {
    return new LegendHiddenEvent();
  }
}
