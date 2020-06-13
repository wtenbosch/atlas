package nl.overheid.aerius.wui.component;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import nl.overheid.aerius.wui.widget.HasEventBus;

@ImplementedBy(BigLibraryViewImpl.class)
public interface BigLibraryView extends IsWidget, HasEventBus {}
