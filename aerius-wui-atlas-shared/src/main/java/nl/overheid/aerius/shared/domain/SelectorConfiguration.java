package nl.overheid.aerius.shared.domain;

import java.util.List;

public class SelectorConfiguration implements HasSelectorType {
  private String title;
  private String type;
  private String description;

  private List<Selector> options;

  public SelectorConfiguration() {}

  @Override
  public String getType() {
    return type;
  }

  public void setType(final String name) {
    this.type = name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (description == null ? 0 : description.hashCode());
    result = prime * result + (options == null ? 0 : options.hashCode());
    result = prime * result + (title == null ? 0 : title.hashCode());
    result = prime * result + (type == null ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final SelectorConfiguration other = (SelectorConfiguration) obj;
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }
    if (options == null) {
      if (other.options != null) {
        return false;
      }
    } else if (!options.equals(other.options)) {
      return false;
    }
    if (title == null) {
      if (other.title != null) {
        return false;
      }
    } else if (!title.equals(other.title)) {
      return false;
    }
    if (type == null) {
      if (other.type != null) {
        return false;
      }
    } else if (!type.equals(other.type)) {
      return false;
    }
    return true;
  }

  public List<Selector> getOptions() {
    return options;
  }

  public void setOptions(final List<Selector> options) {
    this.options = options;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "SelectorOptions [type=" + type + ", options=" + options + ", title=" + title + ", description=" + description + "]";
  }
}
