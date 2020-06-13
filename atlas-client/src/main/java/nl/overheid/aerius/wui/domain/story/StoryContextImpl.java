/*
 * Copyright Dutch Ministry of Agriculture, Nature and Food Quality
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package nl.overheid.aerius.wui.domain.story;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.DatasetConfiguration;
import nl.overheid.aerius.shared.domain.PanelConfiguration;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.shared.domain.StoryFragment;
import nl.overheid.aerius.wui.util.ObjectUtil;

public class StoryContextImpl implements StoryContext {
  protected Story story;

  protected DatasetConfiguration dataset;
  private DatasetConfiguration defaultDataset;
  protected Set<DatasetConfiguration> datasets;

  protected String defaultChapter;
  protected String chapter;

  protected String year;

  private Map<PanelNames, PanelConfiguration> panels;

  @Override
  public void reset() {
    story = null;
    dataset = null;
    defaultDataset = null;
    datasets = null;

    defaultChapter = null;
    chapter = null;

    panels = null;

    year = null;
  }

  @Override
  public void initStory(final Story story) {
    if (this.story != null) {
      throw new RuntimeException("Initialising StoryContext while it is already initialised.");
    }

    this.story = story;
  }

  @Override
  public DatasetConfiguration getDataset() {
    return dataset;
  }

  @Override
  public void setDatasetDefault(final DatasetConfiguration dataset) {
    defaultDataset = dataset;
  }

  @Override
  public boolean setDataset(final DatasetConfiguration dataset) {
    if (dataset == null) {
      if (defaultDataset != null) {
        setDataset(defaultDataset);
        return false;
      }
    }

    final boolean change = !ObjectUtil.equals(this.dataset, dataset);
    if (change) {
      this.dataset = dataset;
    }

    return change;
  }

  @Override
  public Story getStory() {
    return story;
  }

  @Override
  public Set<DatasetConfiguration> getDatasets() {
    return datasets;
  }

  @Override
  public boolean setDatasets(final Set<DatasetConfiguration> datasets) {
    final boolean change = !ObjectUtil.equals(this.datasets, datasets);
    this.datasets = datasets;
    return change;
  }

  @Override
  public Chapter getChapter() {
    return getChapter(chapter);
  }

  protected Chapter getChapter(final String uid) {
    final Optional<StoryFragment> fragment = Optional.ofNullable(story.fragments().get(dataset));
    if (!fragment.isPresent()) {
      throw new RuntimeException("No fragment for dataset '" + dataset + "'");
    }

    return fragment.flatMap(v -> v.chapter(uid)).orElse(null);
  }

  @Override
  public void setChapterDefault(final String uid) {
    this.defaultChapter = uid;
  }

  @Override
  public boolean setChapter(final String uid) {
    final boolean change = !ObjectUtil.equals(this.chapter, uid);

    if (uid == null) {
      if (defaultChapter == null) {
        throw new RuntimeException("Null value passed for non-optional value (chapter) but default value not set.");
      }

      setChapter(defaultChapter);
      return false;
    }

    // Sanity failure
    final Optional<StoryFragment> fragment = Optional.ofNullable(story.fragments().get(dataset));
    if (uid != null && !fragment.flatMap(v -> v.chapter(uid)).isPresent()) {
      throw new RuntimeException("No chapter for story: " + this.story.uid() + " fragment: " + this.dataset + " > chapter " + uid);
    }

    this.chapter = uid;
    return change;
  }

  @Override
  public Map<PanelNames, PanelConfiguration> getPanels() {
    return panels;
  }

  @Override 
  public boolean setPanels(final Map<PanelNames, PanelConfiguration> panels) {
    final boolean change = !ObjectUtil.equals(this.panels, panels);
    this.panels = panels;
    return change;
  }

  @Override
  public DatasetConfiguration getDatasetDefault() {
    return defaultDataset;
  }
}
