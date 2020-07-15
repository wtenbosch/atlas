package nl.overheid.aerius.templates.stories.helpers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.overheid.aerius.collections.BackportedList;

public class ComponentConfigurationBuilder {
  private String name;
  private String source;
  private int version;

  private final Set<String> selectors = new HashSet<>();
  private final Set<SelectorResourceBuilder> chapterSelectables = new HashSet<>();

  private final Map<String, String> parameters = new HashMap<>();

  public ComponentConfigurationBuilder copy() {
    final ComponentConfigurationBuilder bldr = new ComponentConfigurationBuilder();
    bldr.name = name;
    bldr.source = source;
    bldr.version = version;
    bldr.selectors.addAll(selectors);
    bldr.chapterSelectables.addAll(chapterSelectables);
    bldr.parameters.putAll(parameters);
    return bldr;
  }

  public ComponentConfigurationBuilder component(final ComponentType component) {
    this.name = component.getName();
    this.source = component.getSource();
    this.version = component.getVersion();
    return this;
  }

  public ComponentConfigurationBuilder component(final String name, final String source) {
    this.name = name;
    this.source = source;
    return this;
  }

  public ComponentConfigurationBuilder parameters(final Map<String, String> params) {
    parameters.putAll(params);
    return this;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public ComponentConfigurationBuilder parameter(final String k, final String v) {
    parameters.put(k, v);
    return this;
  }

  public ComponentConfigurationBuilder url(final String url) {
    parameters.put("url", url);
    return this;
  }

  public Set<String> selectors() {
    return selectors;
  }

  public Set<SelectorResourceBuilder> chapterSelectables() {
    return chapterSelectables;
  }

  public ComponentConfigurationBuilder selectors(final String... lst) {
    selectors.addAll(BackportedList.of(lst));
    return this;
  }

  public ComponentConfigurationBuilder selector(final SelectorResourceBuilder selectorSpec) {
    selectors.add(selectorSpec.type());
    chapterSelectables.add(selectorSpec);

    return this;
  }

  public String getName() {
    return name;
  }

  public String getSource() {
    return source;
  }

  public int getVersion() {
    return version;
  }
}
