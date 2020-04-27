package nl.overheid.aerius.wui.atlas.daemon.exception;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.wui.atlas.event.SelectorConfigurationClearEvent;
import nl.overheid.aerius.wui.daemon.Daemon;
import nl.overheid.aerius.wui.event.BasicEventComponent;

@Singleton
public class ExceptionDaemonImpl extends BasicEventComponent implements Daemon {
  private static final ExceptionDaemonImplEventBinder EVENT_BINDER = GWT.create(ExceptionDaemonImplEventBinder.class);

  interface ExceptionDaemonImplEventBinder extends EventBinder<ExceptionDaemonImpl> {}

  private final Set<String> reportedTypes = new HashSet<>();

//  @EventHandler
//  public void onSelectorLoadFailureEvent(final SelectorLoadFailureEvent e) {
//    if (reportedTypes.contains(e.getType())) {
//      return;
//    }
//
//    NotificationUtil.broadcastError(eventBus, M.messages().selectorLoadFailure(e.getType()));
//    reportedTypes.add(e.getType());
//  }

  @EventHandler
  public void onSelectorConfigurationClearEvent(final SelectorConfigurationClearEvent e) {
    reportedTypes.clear();
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
