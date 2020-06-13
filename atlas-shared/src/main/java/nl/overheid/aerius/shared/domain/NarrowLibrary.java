package nl.overheid.aerius.shared.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NarrowLibrary implements Serializable {
  private static final long serialVersionUID = 900684033062001122L;

  private final Map<String, StoryInformation> stories = new HashMap<>();

  private List<DocumentResource> documents;

  public List<DocumentResource> getDocuments() {
    return documents;
  }

  public void setDocuments(final List<DocumentResource> documents) {
    this.documents = documents;
  }

  public Map<String, StoryInformation> getStories() {
    return stories;
  }

  public void put(final String id, final StoryInformation story) {
    stories.put(id, story);
  }

  public Collection<StoryInformation> values() {
    return stories.values();
  }

  public StoryInformation get(final String id) {
    return stories.get(id);
  }

  public NarrowLibrary join(final NarrowLibrary into) {
    return NarrowLibrary.of(Stream.concat(stories.values().stream(), into.stories.values().stream())
        .collect(Collectors.toList()));
  }

  public static NarrowLibrary of(final List<StoryInformation> list) {
    final NarrowLibrary lib = new NarrowLibrary();
    lib.stories.putAll(list.stream()
        .distinct()
        .collect(Collectors.toMap(v -> v.uid(), v -> v)));
    return lib;
  }

  public static NarrowLibrary empty() {
    return new NarrowLibrary();
  }

  @Override
  public String toString() {
    return "NarrowLibrary [stories=" + stories + ", documents=" + documents + "]";
  }
}
