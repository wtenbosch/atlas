package nl.overheid.aerius.wui.atlas.daemon.compatibility;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.wui.util.NotificationUtil;

public class CompatibilityDaemonImpl implements CompatibilityDaemon {
  private static final FullCompatibility COMPATIBILITY_NOTICE = GWT.create(FullCompatibility.class);

  @Override
  public void setEventBus(final EventBus eventBus) {
    final String message = COMPATIBILITY_NOTICE.getMessage();
    if (message != null) {
      Scheduler.get().scheduleDeferred(() -> {
        NotificationUtil.broadcastMessage(eventBus, message);
      });
    }
  }
}
