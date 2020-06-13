package nl.overheid.aerius.wui.atlas.daemon.mobile;

import com.google.inject.ImplementedBy;

import nl.overheid.aerius.wui.daemon.Daemon;

@ImplementedBy(MobileDaemonImpl.class)
public interface MobileDaemon extends Daemon {}
