package nl.overheid.aerius.wui.atlas.daemon.library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.NarrowLibrary;
import nl.overheid.aerius.shared.domain.StoryInformation;
import nl.overheid.aerius.wui.atlas.event.LibraryChangeEvent;
import nl.overheid.aerius.wui.atlas.event.StoryLoadedEvent;
import nl.overheid.aerius.wui.atlas.future.availability.AvailabilityOracle;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.event.BasicEventComponent;
import nl.overheid.aerius.wui.util.ObservingReplacementAssistant;
import nl.overheid.aerius.wui.util.ReplacementRegistration;

public class LibraryDaemonImpl extends BasicEventComponent implements LibraryDaemon {
  private final LibraryDaemonImplEventBinder EVENT_BINDER = GWT.create(LibraryDaemonImplEventBinder.class);

  interface LibraryDaemonImplEventBinder extends EventBinder<LibraryDaemonImpl> {}

  @Inject private ObservingReplacementAssistant assistant;

  private final Collection<ReplacementRegistration> registers = new ArrayList<>();

  @Inject AvailabilityOracle availabilityOracle;

  private NarrowLibrary activeLibrary;

  @EventHandler
  public void onLibraryChangeEvent(final LibraryChangeEvent e) {
    updateLibrary(e.getValue());
  }

  @EventHandler
  public void onStoryLoadedEvent(final StoryLoadedEvent e) {
    if (activeLibrary != null) {
      Scheduler.get().scheduleDeferred(() -> updateLibrary(activeLibrary));
    }
  }

  private void updateLibrary(final NarrowLibrary lib) {
    registers.forEach(v -> v.unregister());
    registers.clear();

    Scheduler.get().scheduleDeferred(() -> trackLibrary(lib));
  }

  private void trackLibrary(final NarrowLibrary library) {
    activeLibrary = library;
    library.values().forEach(story -> {
      Optional.ofNullable(story.properties())
          .map(v -> v.get("availability-service"))
          .ifPresent(v -> trackLibraryItem(story, v));
    });
  }

  private void trackLibraryItem(final StoryInformation story, final String service) {
    registers.add(assistant.registerStrict(service, v -> {
      if (v == null) {
        GWTProd.warn("Story availability could not be fetched, which should ideally not occur.");
        updateStoryAvailability(story, false, false);
        return;
      }

      availabilityOracle.getAvailability(v, available -> updateStoryAvailability(story, available, true));
    }));
  }

  protected void updateStoryAvailability(final StoryInformation inf, final boolean available, final boolean complete) {
    notifyStoryAvailability(inf, available, complete);
  }

  protected void notifyStoryAvailability(final StoryInformation story, final boolean available, final boolean complete) {
    eventBus.fireEvent(new LibraryStatusChangedEvent(story, available, complete));
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);

  }
}
