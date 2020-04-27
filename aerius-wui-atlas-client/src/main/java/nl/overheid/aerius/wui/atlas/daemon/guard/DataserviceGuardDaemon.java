package nl.overheid.aerius.wui.atlas.daemon.guard;

import com.google.inject.ImplementedBy;

import nl.overheid.aerius.wui.widget.HasEventBus;

@ImplementedBy(DataserviceGuardDaemonImpl.class)
public interface DataserviceGuardDaemon extends HasEventBus {}
