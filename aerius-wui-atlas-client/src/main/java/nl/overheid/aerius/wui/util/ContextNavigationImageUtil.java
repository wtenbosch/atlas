package nl.overheid.aerius.wui.util;

import com.google.gwt.resources.client.DataResource;

import nl.overheid.aerius.shared.domain.PanelType;
import nl.overheid.aerius.wui.resources.AdditionalNavigationResources;
import nl.overheid.aerius.wui.resources.R;

public final class ContextNavigationImageUtil {
  private ContextNavigationImageUtil() {}

  public static DataResource getImageResource(final PanelType option) {
    final AdditionalNavigationResources sir = R.images();
    final DataResource ir;

    switch (option) {
    case EXPORT:
      ir = sir.additionalNavigationExport();
      break;
    case INFO:
      ir = sir.additionalNavigationInfo();
      break;
    case META:
      ir = sir.additionalNavigationExplanation();
      break;
    case LAYERS:
      ir = sir.additionalNavigationMapLayersLegend();
      break;
    case LOCATION:
    case MAP:
      ir = sir.additionalNavigationLocation();
      break;
    case PREFERENCES:
      ir = sir.additionalNavigationPreferences();
      break;
    default:
      throw new IllegalArgumentException("Unsupported ContextNavigationControlOption.");
    }

    return ir;
  }
}
