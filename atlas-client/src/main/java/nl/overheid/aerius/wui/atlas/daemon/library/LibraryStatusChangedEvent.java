package nl.overheid.aerius.wui.atlas.daemon.library;

import nl.overheid.aerius.shared.domain.StoryInformation;
import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class LibraryStatusChangedEvent extends SimpleGenericEvent<StoryInformation> {
  private final boolean available;
  private final boolean complete;

  public LibraryStatusChangedEvent(final StoryInformation story, final boolean available, final boolean complete) {
    super(story);
    this.available = available;
    this.complete = complete;
  }

  public boolean isAvailable() {
    return available;
  }

  public boolean isComplete() {
    return complete;
  }
}
