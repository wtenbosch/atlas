package nl.overheid.aerius.wui.atlas.daemon.compatibility;

import com.google.inject.ImplementedBy;

import nl.overheid.aerius.wui.widget.HasEventBus;

@ImplementedBy(CompatibilityDaemonImpl.class)
public interface CompatibilityDaemon extends HasEventBus {}
