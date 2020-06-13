package nl.overheid.aerius.wui.atlas.command;

import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class SelectionEvent extends SimpleGenericEvent<String> {
  public SelectionEvent(final String value) {
    super(value);
  }
}
