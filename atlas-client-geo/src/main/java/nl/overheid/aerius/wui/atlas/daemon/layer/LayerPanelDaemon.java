package nl.overheid.aerius.wui.atlas.daemon.layer;

import com.google.inject.ImplementedBy;

import nl.overheid.aerius.wui.daemon.Daemon;

@ImplementedBy(LayerPanelDaemonImpl.class)
public interface LayerPanelDaemon extends Daemon {}
