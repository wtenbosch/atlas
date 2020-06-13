package nl.overheid.aerius.wui.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.DataResource.MimeType;

public interface AdditionalNavigationResources extends ClientBundle {
  @Source("images/navigation/an-preferences.svg")
  @MimeType("image/svg+xml")
  DataResource additionalNavigationPreferences();

  @Source("images/navigation/an-info.svg")
  @MimeType("image/svg+xml")
  DataResource additionalNavigationInfo();

  @Source("images/navigation/an-meta.svg")
  @MimeType("image/svg+xml")
  DataResource additionalNavigationMeta();

  @Source("images/navigation/an-maplayers-legend.svg")
  @MimeType("image/svg+xml")
  DataResource additionalNavigationMapLayersLegend();

  @Source("images/navigation/an-location.svg")
  @MimeType("image/svg+xml")
  DataResource additionalNavigationLocation();

  @Source("images/navigation/an-export.svg")
  @MimeType("image/svg+xml")
  DataResource additionalNavigationExport();

  @Source("images/navigation/an-explanation.svg")
  @MimeType("image/svg+xml")
  DataResource additionalNavigationExplanation();

  @Source("images/navigation/an-legend.svg")
  @MimeType("image/svg+xml")
  DataResource additionalNavigationLegend();

  @Source("images/navigation/an-close.svg")
  @MimeType("image/svg+xml")
  DataResource additionalNavigationClose();
}
