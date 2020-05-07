package nl.overheid.aerius.domain;

public class LayerBehaviour {
  /**
   * The radio group name for this layer, this behaviour will cause all layers
   * under this bundle group name to be grouped under one radio item.
   */
  private String bundleGroup;
  /**
   * The cluster name for this layer; this behaviour will cause only one of the
   * layers within a cluster to be visible.
   */
  private String clusterGroup;
  /**
   * A friendly, simple, short, but unspecific, name that will identify this layer
   * within a specific context.
   */
  private String friendlyName;

  public String getBundleGroup() {
    return bundleGroup;
  }

  public void setBundleGroup(final String bundleGroup) {
    this.bundleGroup = bundleGroup;
  }

  public String getFriendlyName() {
    return friendlyName;
  }

  public void setFriendlyName(final String friendlyName) {
    this.friendlyName = friendlyName;
  }

  public String getClusterGroup() {
    return clusterGroup;
  }

  public void setClusterGroup(final String clusterGroup) {
    this.clusterGroup = clusterGroup;
  }
}
