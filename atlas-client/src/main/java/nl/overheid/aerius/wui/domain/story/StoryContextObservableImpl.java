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
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import nl.overheid.aerius.shared.domain.DatasetConfiguration;
import nl.overheid.aerius.shared.domain.PanelConfiguration;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.wui.atlas.event.ChapterSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.event.DataSetChangeEvent;
import nl.overheid.aerius.wui.atlas.event.DataSetListChangeEvent;
import nl.overheid.aerius.wui.atlas.event.PanelConfigurationChangeEvent;
import nl.overheid.aerius.wui.atlas.event.StoryLoadedEvent;
import nl.overheid.aerius.wui.domain.ObservableAssisstant;

@Singleton
public class StoryContextObservableImpl extends StoryContextImpl {
  private final ObservableAssisstant assistant;

  @Inject
  public StoryContextObservableImpl(final ObservableAssisstant assistant) {
    this.assistant = assistant;
  }

  @Override
  public void initStory(final Story story) {
    assistant.fireIfChanged(() -> new StoryLoadedEvent(), () -> {
      super.initStory(story);
      return true;
    }, story);
  }

  @Override
  public boolean setDataset(final DatasetConfiguration dataset) {
    return assistant.fireIfChanged(() -> new DataSetChangeEvent(), () -> super.setDataset(dataset), dataset);
  }

  @Override
  public boolean setDatasets(final Set<DatasetConfiguration> datasets) {
    return assistant.fireIfChanged(() -> new DataSetListChangeEvent(), () -> super.setDatasets(datasets), datasets);
  }

  @Override
  public boolean setPanels(final Map<PanelNames, PanelConfiguration> panels) {
    return assistant.fireIfChanged(() -> new PanelConfigurationChangeEvent(), () -> super.setPanels(panels), panels);
  }

  @Override
  public boolean setChapter(final String chapterUid) {
    return assistant.fireIfChanged(() -> new ChapterSelectionChangeEvent(), () -> super.setChapter(chapterUid),
        getChapter(chapterUid));
  }
}
