package nl.overheid.aerius.wui.atlas.daemon.library;

import com.google.inject.ImplementedBy;

import nl.overheid.aerius.wui.daemon.Daemon;

@ImplementedBy(LibraryDaemonImpl.class)
public interface LibraryDaemon extends Daemon {}
