package nl.overheid.aerius.wui.domain.map;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.HandlerRegistration;

import nl.overheid.aerius.geo.domain.IsMapCohort;
import nl.overheid.aerius.geo.wui.Map;

@Singleton
public class MapContextImpl implements MapContext {
  private final List<IsMapCohort> cohorts = new ArrayList<>();

  private Map activeMap;

  @Override
  public HandlerRegistration registerPrimaryMapCohort(final IsMapCohort cohort) {
    cohorts.add(cohort);
    return () -> cohorts.remove(cohort);
  }

  @Override
  public void claimMapPrimacy(final Map map) {
    activeMap = map;
    for (final IsMapCohort cohort : cohorts) {
      map.registerEventCohort(cohort);
    }
  }

  @Override
  public Map getActiveMap() {
    return activeMap;
  }
}
