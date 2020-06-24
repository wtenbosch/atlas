package nl.overheid.aerius.service.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.overheid.aerius.shared.domain.ServiceSelector;
import nl.overheid.aerius.shared.domain.ServiceSelectorConfiguration;

public class SelectorBuilder {
  public static ServiceSelector.Builder selector(final String name, final String value, final ServiceSelector.Builder... subs) {
    return ServiceSelector.builder()
        .name(name)
        .value(value)
        .selectors(list(subs));
  }

  public static List<ServiceSelector> list(final ServiceSelector.Builder... selectors) {
    return Stream.of(selectors)
        .map(v -> v.build())
        .collect(Collectors.toList());
  }

  public static ServiceSelectorConfiguration.Builder createConfig(final String type, final String title, final String description) {
    return ServiceSelectorConfiguration.builder()
        .title(title)
        .description(description)
        .type(type);
  }
}
