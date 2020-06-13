package nl.overheid.aerius.wui.atlas.daemon.selector;

import com.google.inject.ImplementedBy;

import nl.overheid.aerius.wui.daemon.Daemon;

@ImplementedBy(SelectorDaemonImpl.class)
public interface SelectorDaemon extends Daemon {}
