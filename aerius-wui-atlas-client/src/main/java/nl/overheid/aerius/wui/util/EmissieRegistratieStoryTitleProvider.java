package nl.overheid.aerius.wui.util;

import com.google.inject.Singleton;

import nl.overheid.aerius.shared.domain.Story;

@Singleton
public class EmissieRegistratieStoryTitleProvider implements StoryTitleProvider {
  @Override
  public String apply(final Story story) {
    return story.info().property("title")
        .orElse(story.info().property("subtitle")
            .orElse("N/A"));
  }
}
