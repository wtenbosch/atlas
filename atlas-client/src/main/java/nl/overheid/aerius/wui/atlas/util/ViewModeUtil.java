package nl.overheid.aerius.wui.atlas.util;

import java.util.LinkedHashMap;
import java.util.Map;

import nl.overheid.aerius.shared.domain.ConfigurationProperties;
import nl.overheid.aerius.shared.domain.LevelOption;
import nl.overheid.aerius.shared.domain.PanelConfiguration;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.shared.domain.PanelType;
import nl.overheid.aerius.wui.atlas.place.MonitorStoryPlace;
import nl.overheid.aerius.wui.util.FilterAssistant;

public final class ViewModeUtil {
  public static final String LOCATION = "location";
  public static final String NATURE = "nature";
  public static final String NATURA = "natura";
  public static final String NATIONAL = "national";

  private ViewModeUtil() {}

  /**
   * These are hard-coded panel configurations that, via a short-cut view_mode
   * param in StoryFragment, are activated. Ideally these panel configs should be
   * encoded fully in JSON and parsed appropriately.
   */
  public static Map<PanelNames, PanelConfiguration> derivePanelConfiguration(final String viewMode) {
    final Map<PanelNames, PanelConfiguration> panels = new LinkedHashMap<PanelNames, PanelConfiguration>();

    final PanelConfiguration map = createMapPanel();
    final PanelConfiguration layersIntegrated = createLayerPanel();
    layersIntegrated.setParent(map);
    layersIntegrated.setIndependent(true);

    final PanelConfiguration layersStandalone = createLayerPanel();

    switch (viewMode) {
    case LOCATION:
      panels.put(PanelNames.PANEL_MAP, map);
      panels.put(PanelNames.PANEL_META, createMetaPanel());
      panels.put(PanelNames.PANEL_LAYER, layersIntegrated);
      panels.put(PanelNames.PANEL_EXPORT, createExportPanel());
      panels.put(PanelNames.PANEL_PREFERENCES, createPreferencesPanel());
      break;
    case NATURE:
    case NATURA:
//      panels.put(PanelNames.PANEL_INFO, createBeschouwingPanel());
      panels.put(PanelNames.PANEL_META, createMetaPanel());
      panels.put(PanelNames.PANEL_LAYER, layersStandalone);
      panels.put(PanelNames.PANEL_PREFERENCES, createPreferencesPanel());
      panels.put(PanelNames.PANEL_EXPORT, createExportPanel());
      break;
    case NATIONAL:
      panels.put(PanelNames.PANEL_META, createMetaPanel());
      panels.put(PanelNames.PANEL_LAYER, layersStandalone);
      panels.put(PanelNames.PANEL_PREFERENCES, createPreferencesPanel());
      panels.put(PanelNames.PANEL_EXPORT, createExportPanel());
      break;
    default:
      throw new RuntimeException("View mode not available: " + viewMode);
    }

    return panels;
  }

  public static Map<PanelNames, PanelConfiguration> getLocationPanelConfiguration() {
    final Map<PanelNames, PanelConfiguration> panels = new LinkedHashMap<PanelNames, PanelConfiguration>();
    final PanelConfiguration location = createLocationPanel();
    final PanelConfiguration layersIntegrated = createLayerPanel();
    layersIntegrated.setParent(location);
    layersIntegrated.setIndependent(true);

    panels.put(PanelNames.PANEL_LOCATION, location);
    panels.put(PanelNames.PANEL_LAYER, layersIntegrated);
    panels.put(PanelNames.PANEL_PREFERENCES, createPreferencesPanel());
    return panels;
  }

  public static Map<PanelNames, PanelConfiguration> findPanelConfiguration(final MonitorStoryPlace place) {
    final String level = place.getFilters().get(FilterAssistant.LEVEL);

    if (level != null) {
      final LevelOption lvl = LevelOption.fromAlias(level);
      if (lvl == null) {
        return getDefaultPanelConfiguration();
      }

      switch (lvl) {
      case TEST:
      case LOCATION:
        return getLocationPanelConfiguration();
      default:
        return getDefaultPanelConfiguration();

      }
    } else {
      return getDefaultPanelConfiguration();
    }
  }

  public static Map<PanelNames, PanelConfiguration> getDefaultPanelConfiguration() {
    final Map<PanelNames, PanelConfiguration> panels = new LinkedHashMap<PanelNames, PanelConfiguration>();
    panels.put(PanelNames.PANEL_PREFERENCES, createPreferencesPanel());
    return panels;
  }

  private static PanelConfiguration createLayerPanel() {
    final PanelConfiguration cont = new PanelConfiguration();
    cont.setName(PanelNames.PANEL_LAYER.getName());
    final ConfigurationProperties contProps = cont.asConfigurationProperties();
    contProps.setPanelType(PanelType.LAYERS);
    return cont;
  }

  private static PanelConfiguration createBeschouwingPanel() {
    final PanelConfiguration cont = new PanelConfiguration();
    cont.setName(PanelNames.PANEL_INFO.getName());
    final ConfigurationProperties contProps = cont.asConfigurationProperties();
    contProps.setPanelType(PanelType.INFO);
    return cont;
  }

  private static PanelConfiguration createMetaPanel() {
    final PanelConfiguration cont = new PanelConfiguration();
    cont.setName(PanelNames.PANEL_META.getName());
    final ConfigurationProperties contProps = cont.asConfigurationProperties();
    contProps.setPanelType(PanelType.META);
    return cont;
  }

  private static PanelConfiguration createLocationPanel() {
    final PanelConfiguration cont = new PanelConfiguration();
    cont.setName(PanelNames.PANEL_LOCATION.getName());
    final ConfigurationProperties contProps = cont.asConfigurationProperties();
    contProps.setPanelType(PanelType.LOCATION);
    return cont;
  }

  private static PanelConfiguration createMapPanel() {
    final PanelConfiguration cont = new PanelConfiguration();
    cont.setName(PanelNames.PANEL_MAP.getName());
    final ConfigurationProperties contProps = cont.asConfigurationProperties();
    contProps.setPanelType(PanelType.MAP);
    return cont;
  }

  private static PanelConfiguration createPreferencesPanel() {
    final PanelConfiguration cont = new PanelConfiguration();
    cont.setName(PanelNames.PANEL_PREFERENCES.getName());
    final ConfigurationProperties contProps = cont.asConfigurationProperties();
    contProps.setPanelType(PanelType.PREFERENCES);
    return cont;
  }

  private static PanelConfiguration createExportPanel() {
    final PanelConfiguration cont = new PanelConfiguration();
    cont.setName(PanelNames.PANEL_EXPORT.getName());
    final ConfigurationProperties contProps = cont.asConfigurationProperties();
    contProps.setPanelType(PanelType.EXPORT);
    return cont;
  }
}
