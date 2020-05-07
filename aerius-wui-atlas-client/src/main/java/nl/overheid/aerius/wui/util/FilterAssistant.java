package nl.overheid.aerius.wui.util;

import java.util.List;
import java.util.Map;

import nl.overheid.aerius.shared.domain.Criterium;
import nl.overheid.aerius.wui.atlas.place.StoryPlace;

public interface FilterAssistant {
  public static final String LEVEL = "level";

  Map<String, String> constructLibraryFilters(Map<String, String> filters);

  List<Criterium> constructLibraryFiltersFromStory(Map<String, String> properties);

  List<Criterium> constructLibraryFiltersFromPlace(StoryPlace currentPlace);

  Map<String, String> toMap(List<Criterium> criteria);

  String[] formatFilters(List<Criterium> filters);

  List<Criterium> toCriteria(Map<String, String> filters);
}
