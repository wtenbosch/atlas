package nl.overheid.aerius.templates.stories.helpers;

import java.util.HashSet;
import java.util.Set;

import nl.overheid.aerius.collections.BackportedList;

public class LayerConfigurationBuilder {
  private String layer;

  private final Set<String> selectors = new HashSet<>();
  private final Set<SelectorResourceBuilder> chapterSelectables = new HashSet<>();

  public String layer() {
    return layer;
  }

  public LayerConfigurationBuilder layer(final String layer) {
    this.layer = layer;
    return this;
  }

  public Set<String> selectors() {
    return selectors;
  }

  public Set<SelectorResourceBuilder> chapterSelectables() {
    return chapterSelectables;
  }

  public LayerConfigurationBuilder selectors(final String... lst) {
    selectors.addAll(BackportedList.of(lst));
    return this;
  }

  public LayerConfigurationBuilder selector(final SelectorResourceBuilder selectorSpec) {
    selectors.add(selectorSpec.type());
    chapterSelectables.add(selectorSpec);

    return this;
  }
}
