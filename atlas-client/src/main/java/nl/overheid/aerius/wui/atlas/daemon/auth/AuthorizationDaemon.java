package nl.overheid.aerius.wui.atlas.daemon.auth;

import com.google.inject.ImplementedBy;

import nl.overheid.aerius.wui.daemon.Daemon;

@ImplementedBy(AuthorizationDaemonImpl.class)
public interface AuthorizationDaemon extends Daemon {}
