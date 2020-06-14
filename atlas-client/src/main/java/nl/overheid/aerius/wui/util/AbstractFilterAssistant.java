package nl.overheid.aerius.wui.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.overheid.aerius.shared.domain.Criterium;
import nl.overheid.aerius.wui.atlas.place.MonitorStoryPlace;

public abstract class AbstractFilterAssistant implements FilterAssistant {
  @Override
  public Map<String, String> toMap(final List<Criterium> criteria) {
    return criteria.stream()
        .collect(Collectors.toMap(v -> v.name(), v -> v.value()));
  }

  @Override
  public String[] formatFilters(final List<Criterium> filters) {
    final String[] strfltr = new String[filters.size() * 2];

    for (int i = 0; i < filters.size(); i++) {
      final Criterium criterium = filters.get(i);
      strfltr[i * 2] = criterium.name();
      strfltr[i * 2 + 1] = criterium.value();
    }

    return strfltr;
  }

  public Map<String, String> constructLibraryFilters(final List<Criterium> criteria) {
    return constructLibraryFilters(toMap(criteria));
  }

  @Override
  public Map<String, String> constructLibraryFilters(final Map<String, String> filterMap) {
    final Map<String, String> sanitized = new LinkedHashMap<>();

    constructLibraryFilters(filterMap, sanitized);

    return sanitized;
  }

  protected void constructLibraryFilters(final Map<String, String> filterMap, final Map<String, String> sanitized) {}

  @Override
  public List<Criterium> constructLibraryFiltersFromPlace(final MonitorStoryPlace place) {
    final Map<String, String> filters = new HashMap<>();

    constructLibraryFiltersFromPlace(filters, place);

    return constructLibraryFiltersFromStory(filters);
  }

  protected void constructLibraryFiltersFromPlace(final Map<String, String> filters, final MonitorStoryPlace place) {}

  @Override
  public List<Criterium> constructLibraryFiltersFromStory(final Map<String, String> filters) {
    final Map<String, String> sanitized = new LinkedHashMap<>();

    constructLibraryFiltersFromStory(sanitized, filters);

    return sanitized.entrySet().stream()
        .map(e -> Criterium.builder()
            .name(e.getKey())
            .value(e.getValue())
            .build())
        .collect(Collectors.toList());
  }

  protected void constructLibraryFiltersFromStory(final Map<String, String> sanitized, final Map<String, String> filters) {}

  @Override
  public List<Criterium> toCriteria(final Map<String, String> filters) {
    return filters.entrySet().stream()
        .map(v -> Criterium.builder()
            .name(v.getKey())
            .value(v.getValue())
            .build())
        .collect(Collectors.toList());
  }
}
