package nl.aerius.service.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.aerius.shared.domain.ServiceSelector;
import nl.aerius.shared.domain.ServiceSelectorConfiguration;

public class SelectorBuilder {
  public static ServiceSelector.Builder selector(final String name, final String value) {
    return ServiceSelector.builder()
        .name(name)
        .value(value);
  }
  
  public static ServiceSelector.Builder selector(final String name, final String value, final ServiceSelector.Builder... subs) {
    return selector(name, value)
        .selectors(list(subs));
  }

  public static ServiceSelector.Builder selector(final String name, final String value, final List<ServiceSelector> list) {
    return ServiceSelector.builder()
        .name(name)
        .value(value)
        .selectors(list);
  }

  public static List<ServiceSelector> list(final ServiceSelector.Builder... selectors) {
    return Stream.of(selectors)
        .map(v -> v.build())
        .collect(Collectors.toList());
  }

  public static ServiceSelectorConfiguration.Builder createConfig(final String type, final String singular, final String title, final String description) {
    return ServiceSelectorConfiguration.builder()
        .title(title)
        .singular(singular)
        .description(description)
        .type(type);
  }
}
