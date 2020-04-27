package nl.overheid.aerius.wui.atlas.event;

import nl.overheid.aerius.shared.domain.SearchSuggestion;
import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class MapSearchSuggestionEvent extends SimpleGenericEvent<SearchSuggestion> {
  public MapSearchSuggestionEvent(final SearchSuggestion value) {
    super(value);
  }
}
