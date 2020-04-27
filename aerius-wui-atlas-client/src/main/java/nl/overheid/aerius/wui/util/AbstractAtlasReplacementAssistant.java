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
package nl.overheid.aerius.wui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.Replacement;
import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.wui.atlas.command.ChapterReplacementCommand;
import nl.overheid.aerius.wui.atlas.command.StoryReplacementCommand;
import nl.overheid.aerius.wui.atlas.event.ChapterSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.event.StorySelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.place.StoryPlace;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.domain.story.StoryContext;
import nl.overheid.aerius.wui.place.ApplicationPlace;
import nl.overheid.aerius.wui.place.PlaceController;

public abstract class AbstractAtlasReplacementAssistant extends GenericReplacementAssistant {
  interface MonitorUpReplacementAssistantEventBinder extends EventBinder<AbstractAtlasReplacementAssistant> {}

  private final MonitorUpReplacementAssistantEventBinder EVENT_BINDER = GWT.create(MonitorUpReplacementAssistantEventBinder.class);

  // Find an alternative to this method of being less strict for certain keys that
  // are deemed optional.
  // This part of the code (and entire code project) ought to have no knowledge of
  // the contents of these keys.
  private static final List<String> softKeys = new ArrayList<>();
  static {
    softKeys.add("indexes");
  }

  @Inject private StoryContext storyContext;

  private final Map<String, String> chapterReplacements = new HashMap<>();
  private final Map<String, String> storyReplacements = new HashMap<>();

  @Inject PlaceController placeController;

  @Override
  protected Map<String, String> createReplacements() {
    final Map<String, String> map = new HashMap<>();

    map.put("now", FormatUtil.formatDateShort(new Date()));
    UglyBoilerPlate.addDevStrings(map);

    // Begin with soft replacements, which will be overridden by all others
    storyReplacements.forEach((k, v) -> {
      map.put(k, v);
    });
    chapterReplacements.forEach((k, v) -> {
      map.put(k, v);
    });

    // Get the story fragment, and replace all occurrences of its properties
    final Optional<Story> story = storyContext.getStoryOptional();
    if (!story.isPresent()) {
      GWTProd.warn("STORY NOT PRESENT SO CANNOT UPDATE THING");
    }

    story.ifPresent(s -> {
      s.info().properties().forEach((k, v) -> {
        map.put(k, v);
      });
    });

    formatSelectors(map, getSelectors());

    // Replace the dataset string
    if (storyContext.getDataset() != null) {
      map.put("dataset", storyContext.getStoryFragment().dataset().code());
    }

    reviewReplacements(map);

    return map;
  }

  protected void reviewReplacements(final Map<String, String> map) {
    // No-op
  }

  protected abstract Collection<Selector> getSelectors();

  @Override
  protected void cleanSoftKeys(final TemplatedString template) {
    for (final String softKey : softKeys) {
      template.replace(softKey, "");
    }
  }

  protected void formatSelectors(final Map<String, String> map, final Collection<Selector> selectors) {
    // Replace all occurrences of selectors
    selectors.forEach(v -> {
      v.getValue().ifPresent(val -> {
        map.put(v.getType(), val);
        map.put(v.getType() + ".value", val);
      });

      v.getTitle().ifPresent(val -> map.put(v.getType() + ".title", val));
      v.getTags().ifPresent(val -> {
        val.forEach((tagKey, tagValue) -> {
          map.put(v.getType() + "." + tagKey, tagValue);
        });
      });
    });
  }

  @EventHandler
  public void onChapterReplacementCommand(final ChapterReplacementCommand e) {
    chapterReplacements.put(e.getValue().getType(), e.getValue().getValue());
  }

  @EventHandler
  public void onStoryReplacementCommand(final StoryReplacementCommand e) {
    storyReplacements.put(e.getValue().getType(), e.getValue().getValue());
  }

  @EventHandler
  public void onChapterSelectionChangeEvent(final ChapterSelectionChangeEvent e) {
    chapterReplacements.clear();
  }

  @EventHandler
  public void onStorySelectionChangeEvent(final StorySelectionChangeEvent e) {
    storyReplacements.clear();

    final ApplicationPlace place = placeController.getPlace();
    if (place instanceof StoryPlace) {
      final StoryPlace storyplace = (StoryPlace) place;
      final Map<String, String> filters = storyplace.getFilters();
      if (filters.containsKey(FilterUtil.ASSESSMENT_AREA_ID)) {
        eventBus.fireEvent(new StoryReplacementCommand(new Replacement("natura2000AreaCode", filters.get(FilterUtil.ASSESSMENT_AREA_ID))));
      }
    }
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
