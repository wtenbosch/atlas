package nl.overheid.aerius.wui.atlas.command;

import nl.overheid.aerius.wui.atlas.event.ContextPanelCollapseEvent;
import nl.overheid.aerius.wui.command.SimpleCommand;

public class ContextPanelCollapseCommand extends SimpleCommand<ContextPanelCollapseEvent> {
  @Override
  protected ContextPanelCollapseEvent createEvent() {
    return new ContextPanelCollapseEvent();
  }
}
