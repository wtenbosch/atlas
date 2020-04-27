package nl.overheid.aerius.wui.atlas.event;

import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class ToggleLayerPanelEvent extends SimpleGenericEvent<Boolean> {
  public ToggleLayerPanelEvent(final boolean open) {
    super(open);
  }
}
