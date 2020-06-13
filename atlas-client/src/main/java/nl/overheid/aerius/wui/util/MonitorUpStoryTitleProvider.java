package nl.overheid.aerius.wui.util;

import com.google.inject.Singleton;

import nl.overheid.aerius.shared.domain.Story;

@Singleton
public class MonitorUpStoryTitleProvider implements StoryTitleProvider {
  @Override
  public String apply(final Story story) {
    return story.info().property("area.label")
        .orElse(story.info().property("natura2000AreaTitle")
            .orElse(story.info().property("subtitle")
                .orElse("{natura2000AreaTitle}")));
  }
}
