package nl.overheid.aerius.wui.atlas.daemon.search;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.command.MapSetExtentCommand;
import nl.overheid.aerius.shared.domain.Criterium;
import nl.overheid.aerius.shared.domain.NarrowLibrary;
import nl.overheid.aerius.shared.domain.SearchSuggestion;
import nl.overheid.aerius.shared.domain.StoryInformation;
import nl.overheid.aerius.wui.atlas.command.LibraryItemSelectionCommand;
import nl.overheid.aerius.wui.atlas.command.NoStoryCommand;
import nl.overheid.aerius.wui.atlas.event.LibraryChangeEvent;
import nl.overheid.aerius.wui.atlas.event.NatureAreaChangedEvent;
import nl.overheid.aerius.wui.atlas.future.library.LibraryOracle;
import nl.overheid.aerius.wui.domain.story.StoryContext;
import nl.overheid.aerius.wui.event.BasicEventComponent;
import nl.overheid.aerius.wui.place.PlaceController;
import nl.overheid.aerius.wui.util.FilterUtil;

/**
 * NOTE: UNUSED
 */
public class SearchNatureStoryDaemonImpl extends BasicEventComponent implements SearchNatureStoryDaemon {
  private static final SearchStoryDaemonImplEventBinder EVENT_BINDER = GWT.create(SearchStoryDaemonImplEventBinder.class);

  interface SearchStoryDaemonImplEventBinder extends EventBinder<SearchNatureStoryDaemonImpl> {}

  private static final String FRIENDLY_NAME_KEY = "friendly-name";

  @Inject StoryContext storyContext;

  @Inject LibraryOracle libraryOracle;

  @Inject PlaceController placeController;

  @EventHandler
  public void onNatureAreaChangedEvent(final NatureAreaChangedEvent e) {
    final List<Criterium> criteria = FilterUtil.I.constructNatureFilters(e.getValue());
//  placeController.goTo(new LibraryPlace(criteria));

    libraryOracle.getStories(criteria, v -> handleLibraryChange(v, e.getSuggestion()));
  }

  private void handleLibraryChange(final NarrowLibrary lib, final SearchSuggestion suggestion) {
    if (storyContext.getStory() != null) {
      final Map<String, String> properties = storyContext.getStory().info().properties();
      if (properties.containsKey(FRIENDLY_NAME_KEY)) {
        quickSelectStory(lib, properties, suggestion);
      } else {
        selectNoStory(lib, suggestion);
      }
    } else {
      selectNoStory(lib, suggestion);
    }
  }

  private void selectNoStory(final NarrowLibrary lib, final SearchSuggestion suggestion) {
    goToExtent(suggestion);

    eventBus.fireEvent(new NoStoryCommand());
    eventBus.fireEvent(new LibraryChangeEvent(lib));
  }

  private void quickSelectStory(final NarrowLibrary lib, final Map<String, String> properties, final SearchSuggestion suggestion) {
    eventBus.fireEvent(new LibraryChangeEvent(lib));

    final Optional<StoryInformation> parallel = lib.getStories().values().stream()
        .filter(v -> v.properties().containsKey(FRIENDLY_NAME_KEY)
            && v.properties().get(FRIENDLY_NAME_KEY).equals(properties.get(FRIENDLY_NAME_KEY)))
        .findFirst();
    if (parallel.isPresent()) {
      eventBus.fireEvent(new LibraryItemSelectionCommand(parallel.get()));
    } else {
      eventBus.fireEvent(new NoStoryCommand());
    }

    goToExtent(suggestion);
  }

  private void goToExtent(final SearchSuggestion suggestion) {
    if (suggestion != null && suggestion.extent() != null) {
      eventBus.fireEvent(new MapSetExtentCommand(suggestion.extent()));
    }
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
