package nl.overheid.aerius.wui.atlas.daemon.map;

import com.google.inject.ImplementedBy;

import nl.overheid.aerius.wui.daemon.Daemon;

@ImplementedBy(MapDaemonImpl.class)
public interface MapDaemon extends Daemon {}
