package nl.overheid.aerius.shared.domain;

public enum PanelNames {
  PANEL_MAIN("panel_main", "hoofd"),
  PANEL_INFO("panel_info", "beschouwing"),
  PANEL_META("panel_meta", "toelichting"),
  PANEL_MAP("panel_map", "kaart"),
  PANEL_LAYER("panel_layer", "lagen"),
  PANEL_PREFERENCES("panel_preferences", "instellingen"),
  PANEL_EXPORT("panel_export", "exporteer"),
  PANEL_LOCATION("panel_location", "locatie"),
  PANEL_LEGEND("panel_legend", "legenda");

  private final String name;
  private final String title;

  private PanelNames(final String name, final String title) {
    this.name = name;
    this.title = title;
  }

  public String getName() {
    return name;
  }

  public String getTitle() {
    return title;
  }

  public static String nameToTitle(final String name) {
    if (name == null) {
      return null;
    }

    final PanelNames panelName = fromName(name);
    if (panelName == null) {
      return null;
    }

    return panelName.getTitle();
  }

  public static String titleToName(final String title) {
    if (title == null) {
      return null;
    }

    final PanelNames panelName = fromTitle(title);
    if (panelName == null) {
      return null;
    }

    return panelName.getName();
  }

  public static PanelNames fromTitle(final String title) {
    for (final PanelNames panelName : values()) {
      if (title.equals(panelName.getTitle())) {
        return panelName;
      }
    }

    return null;
  }

  public static PanelNames fromName(final String name) {
    for (final PanelNames panelName : values()) {
      if (name.equals(panelName.getName())) {
        return panelName;
      }
    }

    return null;
  }
}
