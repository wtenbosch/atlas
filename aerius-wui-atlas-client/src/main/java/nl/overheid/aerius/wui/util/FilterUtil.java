/*
 * Copyright Dutch Ministry of Agriculture, Nature and Food Quality
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package nl.overheid.aerius.wui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;

import nl.overheid.aerius.shared.domain.Criterium;
import nl.overheid.aerius.shared.domain.LevelOption;
import nl.overheid.aerius.shared.domain.NatureArea;
import nl.overheid.aerius.wui.atlas.place.StoryPlace;

public class FilterUtil {
  public static final FilterUtil I = GWT.create(FilterUtil.class);

  public static final String RECEPTOR_ID = "rec";
  public static final String ASSESSMENT_AREA_ID = "gebied";
  public static final String LEVEL = "level";
  public static final String STORY_FILTER = "filter";

  public FilterUtil() {}

  public List<Criterium> constructNationalFilters() {
    final List<Criterium> lst = new ArrayList<>();
    lst.add(Criterium.builder()
        .name(LEVEL)
        .value(LevelOption.NATIONAL.getAlias())
        .build());
    return lst;
  }

  public Map<String, String> toMap(final List<Criterium> criteria) {
    return criteria.stream()
        .collect(Collectors.toMap(v -> v.name(), v -> v.value()));
  }

  public Map<String, String> constructLibraryFilters(final Map<String, String> filterMap) {
    final Map<String, String> sanitized = new LinkedHashMap<>();

    filterMap.computeIfPresent(FilterUtil.ASSESSMENT_AREA_ID, (k, v) -> {
      sanitized.put(LEVEL, LevelOption.NATURE.getAlias());
      return v;
    });

    filterMap.computeIfPresent("level", (k, v) -> {
      // Convert and back to ensure it exists
      sanitized.put(LEVEL, LevelOption.fromAlias(v).getAlias());
      return v;
    });

    return sanitized;
  }

  public Map<String, String> constructLibraryFilters(final List<Criterium> criteria) {
    return constructLibraryFilters(toMap(criteria));
  }

  public List<Criterium> constructLibraryFiltersFromPlace(final StoryPlace place) {
    final Map<String, String> filters = new HashMap<>();

    Optional.ofNullable(place.getFilters().get(FilterUtil.ASSESSMENT_AREA_ID))
        .ifPresent(area -> {
          filters.put(ASSESSMENT_AREA_ID, area);
        });

    return constructLibraryFiltersFromStory(filters);
  }

  public List<Criterium> constructLibraryFiltersFromStory(final Map<String, String> storyProperties) {
    final Map<String, String> sanitized = new LinkedHashMap<>();
    storyProperties.computeIfPresent(ASSESSMENT_AREA_ID, (k, v) -> {
      sanitized.put(ASSESSMENT_AREA_ID, v);
      return v;
    });

    return sanitized.entrySet().stream()
        .map(e -> Criterium.builder()
            .name(e.getKey())
            .value(e.getValue())
            .build())
        .collect(Collectors.toList());
  }

  protected String formatAlias(final String alias) {
    return alias;
  }

  public String getDisplayLabel(final Map<String, NatureArea> areas, final Map<String, String> filters) {
    if (filters.isEmpty()) {
      throw new RuntimeException("Trying to format filters while the list of filters is empty.");
    }

//    switch (criterium.name()) {
//    case RECEPTOR_ID:
//      label = "Receptor: " + criterium.value();
//      break;
//    case ASSESSMENT_AREA_ID:
//      label = areas.get(criterium.value()).getName();
//      break;
//    default:
//      label = "Nederland";
//    }

    return "WIP";
  }

  public String[] formatFilters(final List<Criterium> filters) {
    final String[] strfltr = new String[filters.size() * 2];

    for (int i = 0; i < filters.size(); i++) {
      final Criterium criterium = filters.get(i);
      strfltr[i * 2] = criterium.name();
      strfltr[i * 2 + 1] = criterium.value();
    }

    return strfltr;
  }

  public List<Criterium> constructNatureFilters(final String id) {
    final List<Criterium> lst = new ArrayList<>();

    lst.add(Criterium.builder()
        .name(ASSESSMENT_AREA_ID)
        .value(id)
        .build());

    return lst;
  }

  public List<Criterium> constructReceptorFilters(final int id) {
    final List<Criterium> lst = new ArrayList<>();

    lst.add(Criterium.builder()
        .name(RECEPTOR_ID)
        .value(String.valueOf(id))
        .build());

    return lst;
  }

  public List<Criterium> toCriteria(final Map<String, String> filters) {
    return filters.entrySet().stream()
        .map(v -> Criterium.builder()
            .name(v.getKey())
            .value(v.getValue())
            .build())
        .collect(Collectors.toList());
  }
}
