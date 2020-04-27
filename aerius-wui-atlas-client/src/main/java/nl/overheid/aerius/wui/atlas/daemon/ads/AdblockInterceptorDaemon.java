package nl.overheid.aerius.wui.atlas.daemon.ads;

import com.google.inject.ImplementedBy;

import nl.overheid.aerius.wui.widget.HasEventBus;

@ImplementedBy(AdblockInterceptorDaemonImpl.class)
public interface AdblockInterceptorDaemon extends HasEventBus {}
