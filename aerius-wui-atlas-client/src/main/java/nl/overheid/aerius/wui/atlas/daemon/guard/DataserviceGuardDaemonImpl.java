package nl.overheid.aerius.wui.atlas.daemon.guard;

import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.wui.event.BasicEventComponent;

public class DataserviceGuardDaemonImpl extends BasicEventComponent implements DataserviceGuardDaemon {
  @Override
  public void setEventBus(final EventBus eventBus) {
    // Disabled. Refactor into daemon that periodically and autonomously checks.

    //    super.setEventBus(eventBus);

    //    RequestUtil.doGet(ApplicationConfig.SERVICE_ENDPOINT_HEALTHCHECKS,
    //        FakeCallback.create(""),
    //        AppAsyncCallback.create(v -> {
    //          eventBus.fireEvent(new DataserviceLoadFailureEvent(false));
    //        },
    //            e -> {
    //              if (e instanceof RequestException) {
    //                eventBus.fireEvent(new DataserviceLoadFailureEvent(true));
    //              }
    //            }));
  }
}
