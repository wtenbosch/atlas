package nl.overheid.aerius.wui.atlas.command;

import nl.overheid.aerius.wui.command.SimpleCommand;

public class ReloadChapterCommand extends SimpleCommand<ReloadChapterEvent> {
  @Override
  protected ReloadChapterEvent createEvent() {
    return new ReloadChapterEvent();
  }
}
