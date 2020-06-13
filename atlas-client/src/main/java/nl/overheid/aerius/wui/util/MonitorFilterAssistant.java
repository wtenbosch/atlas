package nl.overheid.aerius.wui.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.inject.Singleton;

import nl.overheid.aerius.shared.domain.Criterium;
import nl.overheid.aerius.shared.domain.LevelOption;
import nl.overheid.aerius.wui.atlas.place.StoryPlace;

@Singleton
public class MonitorFilterAssistant extends AbstractFilterAssistant {
  public static final String LEVEL = "level";
  public static final String ASSESSMENT_AREA_ID = "gebied";
  public static final String RECEPTOR_ID = "rec";

  public List<Criterium> constructNatureFilters(final String id) {
    final List<Criterium> lst = new ArrayList<>();

    lst.add(Criterium.builder()
        .name(ASSESSMENT_AREA_ID)
        .value(id)
        .build());

    return lst;
  }

  @Override
  protected void constructLibraryFilters(final Map<String, String> filterMap, final Map<String, String> sanitized) {
    filterMap.computeIfPresent(ASSESSMENT_AREA_ID, (k, v) -> {
      sanitized.put(LEVEL, LevelOption.NATURE.getAlias());
      return v;
    });

    filterMap.computeIfPresent(LEVEL, (k, v) -> {
      // Convert and back to ensure it exists
      sanitized.put(LEVEL, LevelOption.fromAlias(v).getAlias());
      return v;
    });
  }

  @Override
  protected void constructLibraryFiltersFromPlace(final Map<String, String> filters, final StoryPlace place) {
    Optional.ofNullable(place.getFilters().get(ASSESSMENT_AREA_ID))
        .ifPresent(area -> {
          filters.put(ASSESSMENT_AREA_ID, area);
        });
  }

  @Override
  protected void constructLibraryFiltersFromStory(final Map<String, String> sanitized, final Map<String, String> filters) {
    filters.computeIfPresent(ASSESSMENT_AREA_ID, (k, area) -> {
      sanitized.put(ASSESSMENT_AREA_ID, area);
      return area;
    });
  }

  public List<Criterium> constructReceptorFilters(final int id) {
    final List<Criterium> lst = new ArrayList<>();

    lst.add(Criterium.builder()
        .name(RECEPTOR_ID)
        .value(String.valueOf(id))
        .build());

    return lst;
  }
}
