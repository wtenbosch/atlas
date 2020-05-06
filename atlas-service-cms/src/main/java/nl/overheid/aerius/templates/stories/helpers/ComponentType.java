package nl.overheid.aerius.templates.stories.helpers;

public enum ComponentType {
  AERIUS_DEBUGGER("aerius-debugger"),
  AERIUS_DEBUGGER_2("aerius-debugger-2", 3),
  AERIUS_DECLINE_DIFFERENCE("aerius-decline-difference"),
  AERIUS_DEPOSITION_INFO("aerius-deposition-info"),
  AERIUS_DEPOSITION_SPACE_DEMAND_RANKING_PERMIT_THRESHOLD_INFO("aerius-deposition-space-demand-ranking-permit-threshold-info"),
  AERIUS_DEPOSITION_SPACE_DEMAND_RANKING_PRIORITY_PROJECTS_INFO("aerius-deposition-space-demand-ranking-priority-projects-info"),
  AERIUS_DEPOSITION_SPACE_DEMAND_RANKING_PRIORITY_SUBPROJECTS_INFO("aerius-deposition-space-demand-ranking-priority-subprojects-info"),
  AERIUS_DEPOSITION_SPACE_DEMAND_RANKING_PROJECTS_INFO("aerius-deposition-space-demand-ranking-projects-info"),
  AERIUS_DEVELOPMENT_SPACES("aerius-development-spaces"),
  AERIUS_DEVELOPMENT_SPACES_PER_SEGMENT("aerius-development-spaces-per-segment"),
  AERIUS_EXCESS_INDEX_PER_HABITAT_TYPE("aerius-excess-index-per-habitat-type"),
  AERIUS_HABITAT_TYPE_DEPOSITION_INFO("aerius-habitat-type-deposition-info"),
  AERIUS_HABITAT_TYPE_INFO("aerius-habitat-type-info"),
  AERIUS_HABITAT_TYPE_NITROGEN_LOAD_CLASSIFICATION_INFO("aerius-habitat-type-nitrogen-load-classification-info"),
  AERIUS_HABITAT_TYPE_NITROGEN_LOAD_DEPOSITION_INDEX_INFO("aerius-habitat-type-nitrogen-load-deposition-index-info"),
  AERIUS_HEXAGON_DEPOSITION_BREAKDOWN_INFO("aerius-hexagon-deposition-breakdown-info"),
  AERIUS_HEXAGON_INFO("aerius-hexagon-info"),
  AERIUS_HEXAGON_MONITOR_DEPOSITION_SPACE_INFO("aerius-hexagon-monitor-deposition-space-info"),
  AERIUS_HEXAGON_NITROGEN_LOAD_TYPE_INFO("aerius-hexagon-nitrogen-load-type-info"),
  AERIUS_HEXAGON_REGISTER_DEPOSITION_SPACE_INFO("aerius-hexagon-register-deposition-space-info"),
  AERIUS_LEGEND_EXAMPLE("aerius-legend-example"),
  AERIUS_LEGEND_COLOR("aerius-legend-color"),
  AERIUS_MONITOR_DEPOSITION_SPACE_INFO("aerius-monitor-deposition-space-info"),
  AERIUS_NATURE_2000_AREA_IMAGE("aerius-natura2000-area-image"),
  AERIUS_NATURA_2000_AREA_INFO("aerius-natura2000-area-info"),
  AERIUS_NITROGEN_DEPOSITION_DEVELOPMENT("aerius-nitrogen-deposition-development"),
  AERIUS_NITROGEN_DEPOSITION_TO_SURFACE("aerius-nitrogen-deposition-to-surface"),
  AERIUS_PP_GRANTED_PROJECT_SECTOR("aerius-pp-granted-project-sector"),
  AERIUS_PP_UTILIZATION_MARKED_HEXAGONS("aerius-pp-utilization-marked-hexagons"),
  AERIUS_PP_UTILIZATION_PER_AREA("aerius-pp-utilization-per-area"),
  AERIUS_PP_UTILIZATION_PER_PERIOD("aerius-pp-utilization-per-period"),
  AERIUS_REGISTER_DEPOSITION_SPACE_EXCEEDING_INFO("aerius-register-deposition-space-exceeding-info"),
  AERIUS_REGISTER_DEPOSITION_SPACE_NON_EXCEEDING_INFO("aerius-register-deposition-space-non-exceeding-info"),
  AERIUS_REHABILITATION_PROGRESS("aerius-rehabilitation-progress"),
  AERIUS_REHABILITATION_STRATEGY_INFO("aerius-rehabilitation-strategy-info"),
  AERIUS_REHABILITATION_STRATEGY_PROGRESS_INFO("aerius-rehabilitation-strategy-progress-info"),
  AERIUS_SPECIES_INFO("aerius-species-info"),
  AERIUS_WEEKLY_BARCHART("aerius-weekly-barchart");

  private final String name;
  private final String url;
  private int version;

  private ComponentType(final String name, final int version) {
    this(name, name + "/" + name + ".js", version);
  }

  private ComponentType(final String name) {
    this(name, name + "/" + name + ".html", 1);
  }

  private ComponentType(final String name, final String url, final int version) {
    this.name = name;
    this.url = url;
    this.version = version;
  }

  public String getName() {
    return name;
  }

  public String getSource() {
    return url;
  }

  public int getVersion() {
    return version;
  }
}
