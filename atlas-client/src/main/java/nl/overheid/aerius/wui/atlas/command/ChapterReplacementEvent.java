package nl.overheid.aerius.wui.atlas.command;

import nl.overheid.aerius.shared.domain.Replacement;
import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class ChapterReplacementEvent extends SimpleGenericEvent<Replacement> {
  public ChapterReplacementEvent(final Replacement value) {
    super(value);
  }
}
