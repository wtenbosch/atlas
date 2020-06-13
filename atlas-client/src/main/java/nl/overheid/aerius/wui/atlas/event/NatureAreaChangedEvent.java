package nl.overheid.aerius.wui.atlas.event;

import nl.overheid.aerius.shared.domain.SearchSuggestion;
import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class NatureAreaChangedEvent extends SimpleGenericEvent<String> {
  private SearchSuggestion sug;

  public NatureAreaChangedEvent(final String areaId) {
    super(areaId);
  }

  public NatureAreaChangedEvent(final String areaId, final SearchSuggestion sug) {
    super(areaId);
    this.setSug(sug);
  }

  public SearchSuggestion getSuggestion() {
    return sug;
  }

  public void setSug(SearchSuggestion sug) {
    this.sug = sug;
  }
}
