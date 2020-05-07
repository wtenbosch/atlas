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
import java.util.Optional;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.event.InfoLocationChangeEvent;
import nl.overheid.aerius.geo.util.ReceptorUtil;
import nl.overheid.aerius.shared.domain.Criterium;
import nl.overheid.aerius.shared.domain.DatasetConfiguration;
import nl.overheid.aerius.shared.domain.PanelConfiguration;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.shared.domain.PanelType;
import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.shared.domain.StoryFragment;
import nl.overheid.aerius.wui.atlas.command.ActivateBigContextCommand;
import nl.overheid.aerius.wui.atlas.command.ActivateSmallContextCommand;
import nl.overheid.aerius.wui.atlas.command.DataSetChangeCommand;
import nl.overheid.aerius.wui.atlas.command.LibraryItemSelectionCommand;
import nl.overheid.aerius.wui.atlas.command.NoStoryCommand;
import nl.overheid.aerius.wui.atlas.command.PanelSelectionChangeCommand;
import nl.overheid.aerius.wui.atlas.command.ReloadChapterCommand;
import nl.overheid.aerius.wui.atlas.command.StorySelectionChangeCommand;
import nl.overheid.aerius.wui.atlas.command.ToggleLayerPanelCommand;
import nl.overheid.aerius.wui.atlas.event.ChapterSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.event.DataSetChangeEvent;
import nl.overheid.aerius.wui.atlas.event.DataSetListChangeEvent;
import nl.overheid.aerius.wui.atlas.event.LibraryChangeEvent;
import nl.overheid.aerius.wui.atlas.event.MapActiveEvent;
import nl.overheid.aerius.wui.atlas.event.PanelSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorConfigurationClearEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorEvent;
import nl.overheid.aerius.wui.atlas.event.StoryFragmentChangedEvent;
import nl.overheid.aerius.wui.atlas.event.StoryLoadedEvent;
import nl.overheid.aerius.wui.atlas.event.StoryLoadingEvent;
import nl.overheid.aerius.wui.atlas.event.ToggleLayerPanelEvent;
import nl.overheid.aerius.wui.atlas.future.library.LibraryOracle;
import nl.overheid.aerius.wui.atlas.future.story.StoryOracle;
import nl.overheid.aerius.wui.atlas.place.StoryPlace;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.atlas.util.ViewModeUtil;
import nl.overheid.aerius.wui.command.AbstractCommandRouter;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.domain.map.MapContext;
import nl.overheid.aerius.wui.domain.selector.SelectorContext;
import nl.overheid.aerius.wui.domain.story.StoryContext;
import nl.overheid.aerius.wui.event.PlaceChangeEvent;
import nl.overheid.aerius.wui.place.ApplicationPlace;
import nl.overheid.aerius.wui.place.PlaceController;
import nl.overheid.aerius.wui.util.FilterAssistant;
import nl.overheid.aerius.wui.util.SelectorUtil;

public class StoryCommandRouterImpl extends AbstractCommandRouter implements StoryCommandRouter {
  private static final StoryCommandRouterImplEventBinder EVENT_BINDER = GWT.create(StoryCommandRouterImplEventBinder.class);

  interface StoryCommandRouterImplEventBinder extends EventBinder<StoryCommandRouterImpl> {}

  @Inject protected PlaceController placeController;

  @Inject private StoryOracle storyOracle;
  @Inject private LibraryOracle libraryOracle;

  @Inject protected ReceptorUtil receptorUtil;

  @Inject protected FilterAssistant filterAssistant;

  protected StoryContext context;
  private final MapContext mapContext;

  @Inject private SelectorContext selectors;

  private boolean layerPanelVisible;

  private int savedChapterIdx;

  public StoryCommandRouterImpl(final StoryContext context, final MapContext mapContext) {
    this.context = context;
    this.mapContext = mapContext;

    context.reset();
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, selectors);
    bindEventBus(eventBus);
  }

  public void bindEventBus(final EventBus eventBus) {
    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @EventHandler
  public void onPlaceChangeEvent(final PlaceChangeEvent e) {
    if (!(e.getValue() instanceof StoryPlace)) {
      return;
    }

    final StoryPlace currentPlace = getStoryPlace(e.getValue());
    doStoryPlaceChange(currentPlace);
  }

  @EventHandler
  public void onNoStoryCommand(final NoStoryCommand c) {
    eventBus.fireEvent(new ActivateBigContextCommand());
    pushDefaultPanel(getStoryPlace(placeController.getPlace()));
    eventBus.fireEvent(new SelectorConfigurationClearEvent());
    eventBus.fireEvent(new MapActiveEvent(false));
  }

  private void pushDefaultPanel(final StoryPlace place) {
    final Map<PanelNames, PanelConfiguration> panels = ViewModeUtil.findPanelConfiguration(place);

    context.setPanels(panels);
  }

  private void doStoryPlaceChange(final StoryPlace currentPlace) {

    final String storyUid = currentPlace.getStory();
    if (context.getStory() == null || !context.getStory().uid().equals(storyUid)) {
      if (storyUid == null) {
        eventBus.fireEvent(new NoStoryCommand());
        eventBus.fireEvent(new PanelSelectionChangeEvent(currentPlace.getPanel()));

        final List<Criterium> filters = filterAssistant.constructLibraryFiltersFromPlace(currentPlace);

        if (!filters.isEmpty()) {
          libraryOracle.getStories(filters, library -> {
            eventBus.fireEvent(new LibraryChangeEvent(library));
          });
        } else {
          GWTProd.warn("Could not recover library for empty story.");
        }
        
      } else {
        storyOracle.getStory(storyUid, story -> {
          // Handle edge-case where we are no longer in the story place when done
          // fetching, and abort
          final StoryPlace currentPlaceLater = getStoryPlaceOrNull(placeController.getPlace());
          if (currentPlaceLater == null) {
            return;
          }

          final List<Criterium> filters = filterAssistant.constructLibraryFiltersFromPlace(currentPlace);

          libraryOracle.getStories(filters, library -> {
            eventBus.fireEvent(new LibraryChangeEvent(library));
          });

          eventBus.fireEvent(new StorySelectionChangeCommand(story));
        });
      }
    }
  }

  private void initialiseStory(final Story story) {
    context.reset();
    context.initStory(story);
  }

  @EventHandler
  public void onLibraryItemSelectionCommand(final LibraryItemSelectionCommand e) {
    final boolean loading = storyOracle.getStory(e.getValue().uid(), story -> {
      // Handle edge-case where we are no longer in the story place when done
      // fetching, and abort
      final StoryPlace currentPlaceLater = getStoryPlaceOrNull(placeController.getPlace());
      if (currentPlaceLater == null) {
        return;
      }

      eventBus.fireEvent(new StorySelectionChangeCommand(story));
    });

    if (loading) {
      eventBus.fireEvent(new StoryLoadingEvent());
    }
  }

  @EventHandler
  public void onStoryChangeCommand(final StorySelectionChangeCommand c) {
    // If the story is null, don't accept the command and silence it.
    if (c.getValue() == null) {
//      eventBus.fireEvent(new NoStoryCommand());
//      eventBus.fireEvent(new PanelSelectionChangeEvent(currentPlace.getPanel()));
//      context.setDatasets(null);
//      context.setChapter(null);
      c.silence();
      return;
    }

    final StoryPlace currentPlace = getStoryPlaceOrNull(placeController.getPlace());
    final boolean loading = storyOracle.getStory(c.getValue().uid(), cs -> {
      final StoryPlace currentPlaceLater = getStoryPlaceOrNull(placeController.getPlace());
      if (currentPlaceLater == null) {
        return;
      }

      initialiseStory(cs);

      // Gymnastics for retaining dataset and chapter if they exist
      final DatasetConfiguration dataset;
      if (currentPlaceLater.getDataset() != null) {
        dataset = UglyBoilerPlate.findDataset(currentPlaceLater.getDataset());
      } else {
        dataset = context.getDatasetDefault();
      }

      final Optional<StoryFragment> fragment = cs.fragment(dataset);
      if (fragment.isPresent()) {
        context.setDataset(dataset);
      } else {
        context.setDataset(null);
      }

      // Code for retaining chapter
      if (context.getStoryFragment().chapters().containsKey(currentPlaceLater.getChapter())) {
        context.setChapter(currentPlaceLater.getChapter());
      } else {
        context.setChapter(null);
      }
    });

    if (loading) {
      eventBus.fireEvent(new StoryLoadingEvent());
    }

    final StoryPlace place = getStoryPlace(placeController.getPlace());

    if (place.getStory() != null && place.getStory().equals(c.getValue().uid())) {
      return;
    }

    place.setStory(c.getValue().uid());

    // Default to chapter 1.
    place.setChapter(null);
    placeController.goTo(place);
  }

  @EventHandler
  public void onSelectorChangeEventYear(final SelectorEvent e) {
    if (!SelectorUtil.matchesStrict(Selector.DEFAULT_TYPE_YEAR, e)) {
      return;
    }

    // Special-case the year, and explicitly set it in the selector context
    selectors.setIfNotSet(e.getValue());
  }

  @EventHandler
  public void onDataSetListChangeEvent(final DataSetListChangeEvent e) {
    final Set<DatasetConfiguration> dataSets = e.getValue();

    savedChapterIdx = -1;

    if (dataSets == null || dataSets.isEmpty()) {
      context.setDatasetDefault(null);
    } else {
      final DatasetConfiguration datasetDefault = UglyBoilerPlate.findDefaultDataset(dataSets);
      context.setDatasetDefault(datasetDefault);
    }
  }

  @EventHandler
  public void onDataSetChangeCommand(final DataSetChangeCommand e) {
    context.getStoryOptional().ifPresent(story -> {
      int idx = 0;
      if (context.getChapter() != null) {
        final String selectedUid = context.getChapter().uid();
        for (final String uid : context.getStoryFragment().chapters().keySet()) {
          if (uid.equals(selectedUid)) {
            break;
          }

          idx++;
        }
      }

      savedChapterIdx = idx;
    });
  }

  @EventHandler
  public void onDataSetChangeEvent(final DataSetChangeEvent e) {
    final StoryFragment fragment = context.getStory().fragment(e.getValue()).get();

    if (fragment.chapters().isEmpty()) {
      throw new RuntimeException("StoryFragment contains no chapters and thus no default chapter can be set.");
    }

    context.setChapterDefault(fragment.chapters().values().iterator().next().uid());

    context.setPanels(fragment.panels());
    eventBus.fireEvent(new StoryFragmentChangedEvent(fragment));
    eventBus.fireEvent(new PanelSelectionChangeEvent(((StoryPlace) placeController.getPlace()).getPanel()));

    if (savedChapterIdx >= 0) {
      context.setChapter(fragment.chapters().values().stream()
          .skip(savedChapterIdx)
          .findFirst()
          .flatMap(v -> Optional.ofNullable(v.uid()))
          .orElse(null));
    }
  }

  @EventHandler
  public void onStoryChangeEvent(final StoryLoadedEvent e) {
    eventBus.fireEvent(new ActivateSmallContextCommand());

    final Map<DatasetConfiguration, StoryFragment> fragments = e.getValue().fragments();

    context.setDatasets(fragments.keySet());

    final StoryPlace previousPlace = getStoryPlaceOrNull(placeController.getPreviousPlace());
    final StoryPlace currentPlace = getStoryPlace(placeController.getPlace());

    final String prevReceptorId = previousPlace == null ? null : previousPlace.getReceptorId();
    final String currReceptorId = currentPlace.getReceptorId();

    fireIfChanged(new InfoLocationChangeEvent(),
        prevReceptorId == null ? null : receptorUtil.createReceptorPointFromId(Integer.parseInt(prevReceptorId)),
        currReceptorId == null ? null : receptorUtil.createReceptorPointFromId(Integer.parseInt(currReceptorId)));
  }

  @EventHandler
  public void onPanelSelectionChangeCommand(final PanelSelectionChangeCommand c) {
    final PanelConfiguration conf = context.getPanels().get(c.getValue());
    if (conf == null) {
      return;
    }

    if (conf.asConfigurationProperties().getPanelType() == PanelType.LAYERS && conf.isIndependent()) {
      eventBus.fireEventFromSource(new ToggleLayerPanelCommand(!layerPanelVisible), mapContext.getActiveMap());
    } else if (conf.asConfigurationProperties().getPanelType() != PanelType.MAP) {
      layerPanelVisible = false;
    }
  }

  @EventHandler
  public void onToggleLayerPanelEvent(final ToggleLayerPanelEvent c) {
    layerPanelVisible = c.getValue();
  }

  @EventHandler
  public void onChapterChangeEvent(final ChapterSelectionChangeEvent e) {
    if (e.getValue() == null) {
      return;
    }

    eventBus.fireEvent(new SelectorConfigurationClearEvent());
    eventBus.fireEvent(new PanelSelectionChangeEvent(((StoryPlace) placeController.getPlace()).getPanel()));
  }

  @EventHandler
  public void onReloadChapterCommand(final ReloadChapterCommand c) {
    final Story story = context.getStory();

    final StorySelectionChangeCommand cmd = new StorySelectionChangeCommand(story);

    eventBus.fireEvent(cmd);
  }

  protected StoryPlace getStoryPlaceOrNull(final ApplicationPlace place) {
    return getStoryPlace(place, false);
  }

  protected StoryPlace getStoryPlace(final ApplicationPlace place) {
    return getStoryPlace(place, true);
  }

  protected StoryPlace getStoryPlace(final ApplicationPlace place, final boolean strict) {
    if (place instanceof StoryPlace) {
      return (StoryPlace) place;
    } else if (strict) {
      throw new RuntimeException("[StoryCommandRouterImpl] Unreachable state attained. PlaceChangeEvent of type " + place.getClass().getSimpleName()
          + " received while not supposed to be able to receive this type.");
    } else {
      return null;
    }
  }
}
