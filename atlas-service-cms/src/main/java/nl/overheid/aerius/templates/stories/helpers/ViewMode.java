package nl.overheid.aerius.templates.stories.helpers;

/**
 * TODO: Refactor to not-an-enum
 */
public enum ViewMode {
  NATURA("natura"),
  LOCATION("location"),
  NATIONAL("national"),
  PAS("pas");

  private final String code;

  private ViewMode(final String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
