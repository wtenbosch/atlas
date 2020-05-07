package nl.overheid.aerius.service.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceSelectorConfiguration {
  private ServiceSelectorName type;
  private String title;
  private String description;
  private List<ServiceSelector> selectors;

  private boolean multiselect;
  private boolean unselectable;

  /**
   * Optional meta data describing query parameters for the given set of selectors.
   */
  private Map<String, Object> meta = new HashMap<>();

  public ServiceSelectorConfiguration() {}

  public ServiceSelectorName getType() {
    return type;
  }

  public void setType(final ServiceSelectorName type) {
    this.type = type;
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

  public List<ServiceSelector> getSelectors() {
    return selectors;
  }

  public void setSelectors(final List<ServiceSelector> selectors) {
    this.selectors = selectors;
  }

  public Map<String, Object> getMeta() {
    return meta;
  }

  public void setMeta(final Map<String, Object> meta) {
    this.meta = meta;
  }

  public boolean isUnselectable() {
    return unselectable;
  }

  public void setUnselectable(final boolean unselectable) {
    this.unselectable = unselectable;
  }

  public boolean isMultiselect() {
    return multiselect;
  }

  public void setMultiselect(final boolean multiselect) {
    this.multiselect = multiselect;
  }

  @Override
  public String toString() {
    return "SelectorConfiguration [type=" + type + ", title=" + title + ", description=" + description + "]";
  }
}
