package nl.overheid.aerius.shared.service;

import java.util.Map;

import nl.overheid.aerius.shared.domain.Story;

public interface LibraryService {
  Map<String, Story> getStories(String filter);
}
