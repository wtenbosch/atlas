package nl.overheid.aerius.templates.stories.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.overheid.aerius.boilerplate.collections.BackportedMap;
import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.ChapterIcon;
import nl.overheid.aerius.shared.domain.DatasetConfiguration;
import nl.overheid.aerius.shared.domain.StoryFragment;

public class DatasetBuilder {
  private final List<ChapterBuilder> chapters = new ArrayList<>();

  private DatasetConfiguration dataset;
  private ViewMode viewMode;

  /**
   * Specify a chapter using the given ChapterBuilder. Applies a default
   * common-sense ordering (if not explicitly set) of using the current chapter 
   * size. In other words the first chapter will appear first, the second will
   * appear second.
   */
  public DatasetBuilder chapter(final ChapterBuilder chapterBuilder) {
    chapterBuilder
        .uid(String.valueOf(chapters.size() + 1));

    if (chapterBuilder.sortId() == 0) {
      chapterBuilder.sortId(chapters.size());
    }

    chapters.add(chapterBuilder);

    return this;
  }

  /**
   * Specify a chapter with the given name and icon. Also pass a chapterBuilder
   * which the name and icon will be passed to. This method is just convenience
   * for including two fields that are required in every chapter spec (name and
   * icon). Applying the same common-sense chapter ordering.
   */
  public DatasetBuilder chapter(final String title, final ChapterIcon icon,
      final ChapterBuilder chapterBuilder) {
    chapterBuilder
        .title(title)
        .icon(icon);

    return chapter(chapterBuilder);
  }

  /**
   * Specify a chapter with the given name and icon. Also pass a chapterBuilder
   * which the name and icon will be passed to. This method is just convenience
   * for including two fields that are required in every chapter spec (name and
   * icon). Applying the same common-sense chapter ordering.
   */
  public DatasetBuilder chapter(final String title, final String name, final ChapterIcon icon,
      final ChapterBuilder chapterBuilder) {
    chapterBuilder
        .identifier(name)
        .title(title)
        .icon(icon);

    return chapter(chapterBuilder);
  }

  /**
   * Specify the dataset to use
   */
  public DatasetBuilder dataset(final DatasetConfiguration dataset) {
    this.dataset = dataset;
    return this;
  }

  /**
   * Specify the view mode to use
   */
  public DatasetBuilder viewMode(final ViewMode viewMode) {
    this.viewMode = viewMode;
    return this;
  }

  ViewMode viewMode() {
    return viewMode;
  }

  public StoryFragment build(final String story) {
    return StoryFragment.builder()
        .dataset(dataset)
        .chapters(buildChapters(story))
        .panels(BackportedMap.of())
        .viewMode(viewMode.getCode())
        .build();
  }

  private Map<String, Chapter> buildChapters(final String story) {
    return chapters
        .stream()
        .map(v -> v.build(story))
        .collect(Collectors.toMap(Chapter::uid, v -> v));
  }
}
