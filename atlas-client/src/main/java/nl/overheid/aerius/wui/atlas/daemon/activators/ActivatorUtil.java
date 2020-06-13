package nl.overheid.aerius.wui.atlas.daemon.activators;

import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.wui.atlas.event.ActivatorActiveEvent;
import nl.overheid.aerius.wui.atlas.event.ActivatorInactiveEvent;

public final class ActivatorUtil {
  private ActivatorUtil() {}

  public static void fireActive(final EventBus eventBus, final String name, final boolean active) {
    if (active) {
      eventBus.fireEvent(new ActivatorActiveEvent(name));
    } else {
      eventBus.fireEvent(new ActivatorInactiveEvent(name));
    }
  }
}
