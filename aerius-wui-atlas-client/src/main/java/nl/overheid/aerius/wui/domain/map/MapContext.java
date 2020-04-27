package nl.overheid.aerius.wui.domain.map;

import com.google.inject.ImplementedBy;
import com.google.web.bindery.event.shared.HandlerRegistration;

import nl.overheid.aerius.geo.domain.IsMapCohort;
import nl.overheid.aerius.geo.wui.Map;

@ImplementedBy(MapContextImpl.class)
public interface MapContext {
  default HandlerRegistration onMap(final IsMapCohort cohort) {
    return registerPrimaryMapCohort(cohort);
  }

  HandlerRegistration registerPrimaryMapCohort(IsMapCohort cohort);

  void claimMapPrimacy(Map map);

  Map getActiveMap();
}
