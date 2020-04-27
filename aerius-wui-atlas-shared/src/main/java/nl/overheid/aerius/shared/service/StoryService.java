package nl.overheid.aerius.shared.service;

import nl.overheid.aerius.shared.domain.Story;

/**
 * Synchronous version of StoryServiceAsync
 */
public interface StoryService {
  Story getStory(String story);
}
