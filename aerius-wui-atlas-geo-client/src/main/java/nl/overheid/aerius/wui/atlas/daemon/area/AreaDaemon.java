package nl.overheid.aerius.wui.atlas.daemon.area;

import com.google.inject.ImplementedBy;

import nl.overheid.aerius.wui.daemon.Daemon;

@ImplementedBy(AreaDaemonImpl.class)
public interface AreaDaemon extends Daemon {}
