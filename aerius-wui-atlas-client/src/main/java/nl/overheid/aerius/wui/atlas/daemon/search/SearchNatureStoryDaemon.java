package nl.overheid.aerius.wui.atlas.daemon.search;

import com.google.inject.ImplementedBy;

import nl.overheid.aerius.wui.daemon.Daemon;

@ImplementedBy(SearchNatureStoryDaemonImpl.class)
public interface SearchNatureStoryDaemon extends Daemon {}
