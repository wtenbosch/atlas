package nl.overheid.aerius.wui.atlas.daemon.search;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

import nl.overheid.aerius.shared.domain.SearchSuggestion;
import nl.overheid.aerius.wui.atlas.future.search.SearchOracle;
import nl.overheid.aerius.wui.event.BasicEventComponent;

public class SearchSuggestionDaemonImpl extends BasicEventComponent implements SearchSuggestionDaemon {
  private final SearchDaemonImplEventBinder EVENT_BINDER = GWT.create(SearchDaemonImplEventBinder.class);

  interface SearchDaemonImplEventBinder extends EventBinder<SearchSuggestionDaemonImpl> {}

  private final Map<String, Object> queries = new HashMap<>();

  private final SearchOracle oracle;

  @Inject
  public SearchSuggestionDaemonImpl(final SearchOracle oracle) {
    this.oracle = oracle;
  }

  @Override
  public void registerMapSearchQuery(final String uniqueKey, final String query, final Consumer<SearchSuggestion> consumer) {
    final Object handle = new Object();
    queries.put(uniqueKey, handle);

    oracle.searchQuery(query, v -> reportSearchResult(uniqueKey, consumer, handle, v));
  }

  private void reportSearchResult(final String uniqueKey, final Consumer<SearchSuggestion> consumer, final Object handle, final SearchSuggestion v) {
    if (queries.get(uniqueKey).equals(handle)) {
      consumer.accept(v);
    }
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}