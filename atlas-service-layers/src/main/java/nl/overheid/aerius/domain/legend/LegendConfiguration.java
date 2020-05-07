package nl.overheid.aerius.domain.legend;

public class LegendConfiguration {
  private LegendType type;

  public LegendConfiguration(final LegendType type) {
    this.type = type;
  }

  public LegendType getType() {
    return type;
  }

  public void setType(final LegendType type) {
    this.type = type;
  }
}
