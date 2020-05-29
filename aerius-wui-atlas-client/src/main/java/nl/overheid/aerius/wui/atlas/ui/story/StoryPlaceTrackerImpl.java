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
package nl.overheid.aerius.wui.atlas.ui.story;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.Criterium;
import nl.overheid.aerius.shared.domain.PanelConfiguration;
import nl.overheid.aerius.wui.atlas.command.ChapterSelectionChangeCommand;
import nl.overheid.aerius.wui.atlas.command.DataSetChangeCommand;
import nl.overheid.aerius.wui.atlas.command.NoStoryCommand;
import nl.overheid.aerius.wui.atlas.command.PanelSelectionChangeCommand;
import nl.overheid.aerius.wui.atlas.event.ChapterSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.place.StoryPlace;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.domain.story.StoryContext;
import nl.overheid.aerius.wui.event.BasicEventComponent;
import nl.overheid.aerius.wui.event.PlaceChangeEvent;
import nl.overheid.aerius.wui.place.ApplicationPlace;
import nl.overheid.aerius.wui.place.PlaceController;
import nl.overheid.aerius.wui.util.FilterAssistant;

/**
 * PlaceTracker for StoryActivity, responsible for keeping the Place and the
 * present context in sync.
 */
public class StoryPlaceTrackerImpl extends BasicEventComponent implements StoryPlaceTracker {
  interface StoryPlaceTrackerImplEventBinder extends EventBinder<StoryPlaceTrackerImpl> {}

  private final StoryPlaceTrackerImplEventBinder EVENT_BINDER = GWT.create(StoryPlaceTrackerImplEventBinder.class);
  private final PlaceController placeController;
  private final StoryContext context;

  @Inject FilterAssistant filterAssistant;

  @Inject GeoStoryPlaceTracker geoStoryPlaceTracker;

  @Inject
  public StoryPlaceTrackerImpl(final PlaceController placeController, final StoryContext context) {
    this.placeController = placeController;
    this.context = context;
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER, geoStoryPlaceTracker);
  }

  @EventHandler
  public void onPlaceChangeChangeEvent(final PlaceChangeEvent c) {
    final StoryPlace place = getStoryPlace(placeController.getPlace());

    // Change only if story is not null _and_ has not changed
    if (context.getStory() != null && context.getStory().uid().equals(place.getStory())) {
      if (place.getDataset() != null) {
        context.setDataset(UglyBoilerPlate.findDataset(place.getDataset()));
      }

      context.setChapter(place.getChapter());
    }
  }

  @EventHandler
  public void onNoStoryCommand(final NoStoryCommand c) {
    final StoryPlace place = getStoryPlace(placeController.getPlace());

    place.setChapter(null);
    place.setStory(null);
    place.setPanel(null);

    GWTProd.log("Deselecting story: " + context.getStory());

    if (context.getStory() != null) {
      final List<Criterium> criteria = filterAssistant.constructLibraryFiltersFromStory(context.getStory().info().properties());

      final Map<String, String> filters = filterAssistant.toMap(criteria);

      place.setFilters(filters);
    }

    placeController.goTo(place);
  }

  @EventHandler
  public void onPanelSelectionChangeCommand(final PanelSelectionChangeCommand c) {
    final StoryPlace place = getStoryPlace(placeController.getPlace());

    if (place.getPanel() != null && place.getPanel().equals(c.getValue())) {
      // c.silence();
      return;
    }

    final PanelConfiguration conf = context.getPanels().get(c.getValue());
    if (conf != null && conf.isIndependent()) {
      return;
    }

    place.setPanel(c.getValue());
    placeController.goTo(place);
  }

  @EventHandler
  public void onDatasetChangeCommand(final DataSetChangeCommand c) {
    c.silence();

    final StoryPlace place = getStoryPlace(placeController.getPlace());

    final boolean changed = context.setDataset(c.getValue());
    if (changed) {
      place.setDataset(c.getValue().code());
      place.setChapter(null);
      placeController.goTo(place);
    }
  }

  @EventHandler
  public void onChapterChangeCommand(final ChapterSelectionChangeCommand c) {
    c.silence();

    final StoryPlace place = getStoryPlace(placeController.getPlace());

    final boolean changed = context.setChapter(c.getValue().uid());
    if (changed) {
      place.setChapter(isFirstChapter(c.getValue()) ? null : c.getValue().uid());
      placeController.goTo(place);
    }
  }

  @EventHandler
  public void onChapterChangeEvent(final ChapterSelectionChangeEvent e) {
    Scheduler.get().scheduleDeferred(() -> {
      final StoryPlace place = getStoryPlace(placeController.getPlace());

      if (isChanged(
          () -> place.getChapter(),
          () -> e.getValue().uid(),
          c -> place.setChapter(isFirstChapter(e.getValue()) ? null : c))) {
        placeController.goTo(place);
      }
    });
  }

  private boolean isFirstChapter(final Chapter chapter) {
    return context.getStoryFragment()
        .chapters().values().stream()
        .findFirst()
        .map(v -> v.uid())
        .filter(v -> v.equals(chapter.uid()))
        .isPresent();
  }

  private <T> boolean isChanged(final Supplier<T> leftSupplier, final Supplier<T> rightSupplier, final Consumer<T> applier) {
    final T right = rightSupplier.get();
    final T leftPre = leftSupplier.get();

    applier.accept(right);

    final Object leftPost = leftSupplier.get();

    return leftPost == null || !leftPost.equals(leftPre);
  }

  private StoryPlace getStoryPlace(final ApplicationPlace place) {
    if (place instanceof StoryPlace) {
      return (StoryPlace) place;
    } else {
      throw new RuntimeException("Unreachable state attained. PlaceChangeEvent of type " + place.getClass().getSimpleName()
          + " received while not supposed to be able to receive this type.");
    }
  }
}
