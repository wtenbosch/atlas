package nl.overheid.aerius.templates.stories.helpers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.overheid.aerius.collections.BackportedMap;
import nl.overheid.aerius.shared.domain.DatasetConfiguration;
import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.shared.domain.StoryFragment;
import nl.overheid.aerius.shared.domain.StoryIcon;
import nl.overheid.aerius.shared.domain.StoryInformation;

public class StoryBuilder {
  private final Story.Builder storyBuilder = Story.builder();
  private StoryInformation.Builder storyInformationBuilder;

  private final List<DatasetBuilder> datasets = new ArrayList<>();

  private ViewMode viewMode;

  private String id;

  private final Map<String, String> customProperties = new HashMap<>();

  public StoryBuilder id(final String id) {
    this.id = id;
    return this;
  }

  public StoryBuilder info(final String name, final StoryIcon icon, final String author, final LocalDate date) {
    storyInformationBuilder = StoryInformation.builder()
        .name(name)
        .authorName(author)
        .creationDate(java.sql.Date.valueOf(date))
        .changedDate(java.sql.Date.valueOf(date))
        .icon(icon);
    return this;
  }

  /**
   * An optional availability service endpoint for this story.
   */
  public StoryBuilder availability(final String availability) {
    return property("availability-service", availability);
  }

  public StoryBuilder property(final String key, final String value) {
    customProperties.put(key, value);
    return this;
  }

  public StoryBuilder viewMode(final ViewMode viewMode) {
    this.viewMode = viewMode;
    return this;
  }

  public StoryBuilder dataset(final List<DatasetConfiguration> datasets, final DatasetBuilder datasetBuilder) {
    datasets.forEach(dataset -> dataset(dataset, datasetBuilder));
    return this;
  }

  public StoryBuilder dataset(final DatasetConfiguration dataset, final DatasetBuilder datasetBuilder) {
    datasetBuilder.dataset(dataset);

    datasets.add(datasetBuilder);
    return this;
  }

  public StoryBuilder datasetSimple(final String dataset, final DatasetBuilder datasetBuilder) {
    return dataset(DatasetConfiguration.builder()
        .code(dataset)
        .build()
        .label(dataset), datasetBuilder);
  }

  public StoryBuilder datasetSimple(final List<String> datasets, final DatasetBuilder datasetBuilder) {
    datasets.forEach(dataset -> datasetSimple(dataset, datasetBuilder));
    return this;
  }

  public Story build(final Map<String, String> properties) {
    return storyBuilder
        .info(storyInformationBuilder
            .uid(id)
            .properties(buildProperties(BackportedMap.copyOf(properties)))
            .build())
        .fragments(buildFragments(id))
        .build();
  }

  private Map<String, String> buildProperties(final Map<String, String> map) {
    // Initialize with custom properties
    final Map<String, String> props = new HashMap<>(customProperties);

    // Override (if needed) with builder properties
    props.putAll(map);

    return props;
  }

  private Map<DatasetConfiguration, StoryFragment> buildFragments(final String storyUid) {
    // Propagate down the latest state of the viewmode
    datasets.forEach(bldr -> {
      if (bldr.viewMode() == null) {
        bldr.viewMode(viewMode);
      }
    });

    return datasets.stream()
        .map(v -> v.build(storyUid))
        .collect(Collectors.toMap(StoryFragment::dataset, v -> v));
  }

  /**
   * Specify the order id for this story.
   */
  public StoryBuilder orderId(final int orderId) {
    storyInformationBuilder.orderId(orderId);
    return this;
  }
}
