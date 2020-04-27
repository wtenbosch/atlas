package nl.overheid.aerius.wui.atlas.daemon.exception;

import com.google.inject.ImplementedBy;

import nl.overheid.aerius.wui.daemon.Daemon;

@ImplementedBy(ExceptionDaemonImpl.class)
public interface ExceptionDaemon extends Daemon {}
