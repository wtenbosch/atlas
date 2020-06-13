package nl.overheid.aerius.wui.component;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import nl.overheid.aerius.geo.domain.IsMapCohort;
import nl.overheid.aerius.wui.widget.HasEventBus;

@ImplementedBy(MapSearchWidgetImpl.class)
public interface MapSearchWidget extends IsWidget, HasEventBus, IsMapCohort {
  public static final int MIN_SEARCH_LENGTH = 3;
}
