package nl.overheid.aerius.templates;

import nl.overheid.aerius.templates.stories.helpers.ChapterBuilder;
import nl.overheid.aerius.templates.stories.helpers.ComponentConfigurationBuilder;
import nl.overheid.aerius.templates.stories.helpers.DatasetBuilder;
import nl.overheid.aerius.templates.stories.helpers.StoryBuilder;

public class StoryTemplateBuilder {
  protected ChapterBuilder chapterBuilder() {
    return new ChapterBuilder();
  }

  protected DatasetBuilder datasetBuilder() {
    return new DatasetBuilder();
  }

  protected StoryBuilder storyBuilder(final String id) {
    return new StoryBuilder()
        .id(id);
  }

  protected ComponentConfigurationBuilder componentBuilder() {
    return new ComponentConfigurationBuilder();
  }
}
