package nl.overheid.aerius.domain.legend;

public class TextLegendConfiguration extends LegendConfiguration {
  private String text;

  public TextLegendConfiguration() {
    super(LegendType.TEXT);
  }

  public String getText() {
    return text;
  }

  public void setText(final String text) {
    this.text = text;
  }
}
