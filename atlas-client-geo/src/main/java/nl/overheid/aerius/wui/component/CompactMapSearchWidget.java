package nl.overheid.aerius.wui.component;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import nl.overheid.aerius.geo.domain.IsMapCohort;
import nl.overheid.aerius.wui.widget.HasEventBus;

@ImplementedBy(CompactMapSearchWidgetImpl.class)
public interface CompactMapSearchWidget extends IsWidget, HasEventBus, IsMapCohort {}
