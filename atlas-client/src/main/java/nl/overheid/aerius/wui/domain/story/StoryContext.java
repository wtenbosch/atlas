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

public interface StoryContext {
  Story getStory();

  default StoryFragment getStoryFragment() {
    return getStory().fragments().get(getDataset());
  };

  void initStory(Story story);

  DatasetConfiguration getDataset();

  boolean setDataset(DatasetConfiguration value);

  void setDatasetDefault(DatasetConfiguration dataset);

  DatasetConfiguration getDatasetDefault();

  Set<DatasetConfiguration> getDatasets();

  boolean setDatasets(Set<DatasetConfiguration> values);

  Chapter getChapter();

  boolean setChapter(String uid);

  void setChapterDefault(String uid);

  Map<PanelNames, PanelConfiguration> getPanels();

  boolean setPanels(Map<PanelNames, PanelConfiguration> panels);

  void reset();

  default Optional<Story> getStoryOptional() {
    return Optional.ofNullable(getStory());
  }
}
