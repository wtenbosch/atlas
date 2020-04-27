package nl.overheid.aerius.shared.domain;

import java.util.List;

public class EditableContentConfigCollection {
  private String referenceName;
  private List<EditableContentConfig> collection;

  private double addNode;

  public String getReferenceName() {
    return referenceName;
  }

  public void setReferenceName(final String referenceName) {
    this.referenceName = referenceName;
  }

  public List<EditableContentConfig> getCollection() {
    return collection;
  }

  public void setCollection(final List<EditableContentConfig> collection) {
    this.collection = collection;
  }

  public void add(final EditableContentConfig conf) {
    collection.add(conf);
  }

  public double getAddNode() {
    return addNode;
  }

  public void setAddNode(final double addNode) {
    this.addNode = addNode;
  }
}
