package nl.overheid.aerius.domain;

public class ConditionedStyleActivator {
  private final String activator;
  private final String style;
  private final String layer;

  public ConditionedStyleActivator(final String activator, final String layer, final String style) {
    this.activator = activator;
    this.layer = layer;
    this.style = style;
  }

  public String getActivator() {
    return activator;
  }

  public String getStyle() {
    return style;
  }

  public String getLayer() {
    return layer;
  }

  public static ConditionedStyleActivator create(final String activator, final String layer, final String style) {
    return new ConditionedStyleActivator(activator, layer, style);
  }
}
