package nl.overheid.aerius.wui.atlas.event;

import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class ContextCompositionChangeEvent extends SimpleGenericEvent<Integer> {
  public ContextCompositionChangeEvent(final Integer value) {
    super(value);
  }
}