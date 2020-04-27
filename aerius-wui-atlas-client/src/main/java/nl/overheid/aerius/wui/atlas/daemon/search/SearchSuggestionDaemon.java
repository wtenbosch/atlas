package nl.overheid.aerius.wui.atlas.daemon.search;

import java.util.function.Consumer;

import com.google.inject.ImplementedBy;

import nl.overheid.aerius.shared.domain.SearchSuggestion;
import nl.overheid.aerius.wui.daemon.Daemon;

@ImplementedBy(SearchSuggestionDaemonImpl.class)
public interface SearchSuggestionDaemon extends Daemon {
  void registerMapSearchQuery(String uniqueKey, String query, Consumer<SearchSuggestion> consumer);
}
