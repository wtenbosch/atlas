package nl.overheid.aerius.wui.atlas.command;

import nl.overheid.aerius.shared.domain.Replacement;
import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class StoryReplacementEvent extends SimpleGenericEvent<Replacement> {
  public StoryReplacementEvent(final Replacement value) {
    super(value);
  }
}
