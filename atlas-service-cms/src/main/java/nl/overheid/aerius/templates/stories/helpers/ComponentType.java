package nl.overheid.aerius.templates.stories.helpers;

public class ComponentType {

  private final String name;
  private final String url;
  private final int version;

  public ComponentType(final String name, final int version) {
    this(name, name + "/" + name + ".js", version);
  }

  public ComponentType(final String name) {
    this(name, name + "/" + name + ".html", 1);
  }

  public ComponentType(final String name, final String url, final int version) {
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
