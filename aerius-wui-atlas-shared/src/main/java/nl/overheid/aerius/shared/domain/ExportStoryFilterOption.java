package nl.overheid.aerius.shared.domain;

public enum ExportStoryFilterOption {
  ALL("", ""),
  AREA_ANALYSIS("area_analysis_print", "area_analysis_print"),
  AREA_REPORTING("area_reporting_print", "area_reporting_print");

  private final String libraryAlias;
  private final String storyAlias;

  private ExportStoryFilterOption(final String libraryAlias, final String storyAlias) {
    this.libraryAlias = libraryAlias;
    this.storyAlias = storyAlias;
  }

  public String getLibraryAlias() {
    return libraryAlias;
  }

  public static ExportStoryFilterOption fromAlias(final String alias) {
    for (final ExportStoryFilterOption option : values()) {
      if (option.getLibraryAlias().equals(alias)) {
        return option;
      }
    }

    throw new IllegalArgumentException("Unknown Level alias.");
  }

  public String getStoryAlias() {
    return storyAlias;
  }
}
