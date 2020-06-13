package nl.overheid.aerius.shared.domain;

import java.util.List;

public class EditableContentConfig {
  private String editNode;
  private List<Selector> selectorCombination;

  public String getEditNode() {
    return editNode;
  }

  public void setEditNode(final String editNode) {
    this.editNode = editNode;
  }

  public List<Selector> getSelectorCombination() {
    return selectorCombination;
  }

  public void setSelectorCombination(final List<Selector> selectorCombination) {
    this.selectorCombination = selectorCombination;
  }
}
