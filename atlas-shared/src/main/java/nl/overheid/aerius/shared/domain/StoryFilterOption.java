package nl.overheid.aerius.shared.domain;

public enum StoryFilterOption {
  ALL("", ""),
  AREA_ANALYSIS("area_analysis", "area_analysis"),
  AREA_REPORTING("area_reporting", "area_reporting");

  private final String libraryAlias;
  private final String storyAlias;

  private StoryFilterOption(final String libraryAlias, final String storyAlias) {
    this.libraryAlias = libraryAlias;
    this.storyAlias = storyAlias;
  }

  public String getLibraryAlias() {
    return libraryAlias;
  }

  public static StoryFilterOption fromAlias(final String alias) {
    for (final StoryFilterOption option : values()) {
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
