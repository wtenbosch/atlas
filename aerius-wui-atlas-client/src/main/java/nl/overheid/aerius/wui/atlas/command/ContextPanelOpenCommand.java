package nl.overheid.aerius.wui.atlas.command;

import nl.overheid.aerius.wui.atlas.event.ContextPanelOpenEvent;
import nl.overheid.aerius.wui.command.SimpleCommand;

public class ContextPanelOpenCommand extends SimpleCommand<ContextPanelOpenEvent> {
  @Override
  protected ContextPanelOpenEvent createEvent() {
    return new ContextPanelOpenEvent();
  }
}
