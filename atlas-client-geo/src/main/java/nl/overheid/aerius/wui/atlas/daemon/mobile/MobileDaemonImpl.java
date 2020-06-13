package nl.overheid.aerius.wui.atlas.daemon.mobile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.command.MapResizeCommand;
import nl.overheid.aerius.geo.domain.IsMapCohort;
import nl.overheid.aerius.geo.event.MapEventBus;
import nl.overheid.aerius.geo.wui.Map;
import nl.overheid.aerius.wui.atlas.command.ChapterSelectionChangeCommand;
import nl.overheid.aerius.wui.atlas.command.CompactModeActivationCommand;
import nl.overheid.aerius.wui.atlas.command.CompactModeDeactivationCommand;
import nl.overheid.aerius.wui.atlas.event.ContextPanelCollapseEvent;
import nl.overheid.aerius.wui.atlas.event.StoryLoadedEvent;
import nl.overheid.aerius.wui.atlas.util.MobileUtil;
import nl.overheid.aerius.wui.domain.map.MapContext;
import nl.overheid.aerius.wui.event.BasicEventComponent;
import nl.overheid.aerius.wui.event.PlaceChangeEvent;

public class MobileDaemonImpl extends BasicEventComponent implements MobileDaemon, IsMapCohort {
  private static final int MOBILE_MAX_WIDTH = 900;

  private final MobileDaemonImplEventBinder EVENT_BINDER = GWT.create(MobileDaemonImplEventBinder.class);

  interface MobileDaemonImplEventBinder extends EventBinder<MobileDaemonImpl> {}

  private boolean mobileMode = false;
  private String mobileModePlace;

  private MapEventBus mapEventBus;

  @Inject
  public MobileDaemonImpl(final MapContext mapContext) {
    mapContext.registerPrimaryMapCohort(this);

    Window.addResizeHandler(e -> checkActivationStatus());
    Window.addResizeHandler(e -> fireScheduledResize());
  }

  @EventHandler
  public void onPlaceChangeEvent(final PlaceChangeEvent e) {
    final boolean oldMobileMode = mobileMode;

    // Poor man's reset..
    final String placeName = e.getValue().getClass().getName();
    if (mobileModePlace != null && !mobileModePlace.equals(placeName)) {
      mobileMode = false;
    }
    mobileModePlace = placeName;

    checkActivationStatus();
    mobileMode = oldMobileMode;

  }

  @EventHandler
  public void onStoryLoadedEvent(final StoryLoadedEvent e) {
    mobileMode = false;
    checkActivationStatus();
  }

  @EventHandler
  public void onChapterSelectionChangeCommand(final ChapterSelectionChangeCommand e) {
    if (mobileMode) {
      eventBus.fireEvent(new ContextPanelCollapseEvent());
    }

    fireScheduledResize();
  }

  private void checkActivationStatus() {
    final int clientWidth = Window.getClientWidth();
    if (clientWidth < MOBILE_MAX_WIDTH && !mobileMode || MobileUtil.isDeviceMobile()) {
      activateCompactMode();
    } else if (clientWidth >= MOBILE_MAX_WIDTH && mobileMode) {
      deactiviateCompactMode();
    } else {
      // No change
    }
  }

  private void activateCompactMode() {
    mobileMode = true;
    eventBus.fireEvent(new CompactModeActivationCommand());
  }

  private void deactiviateCompactMode() {
    mobileMode = false;
    eventBus.fireEvent(new CompactModeDeactivationCommand());

    fireScheduledResize();
  }

  private void fireScheduledResize() {
    resizeDelayed(5);
    resizeDelayed(250);
    resizeDelayed(255);
    resizeDelayed(300);
  }

  private void resizeDelayed(final int delay) {
    new Timer() {
      @Override
      public void run() {
        if (mapEventBus != null) {
          mapEventBus.fireEvent(new MapResizeCommand());
        }
      }
    }.schedule(delay);
  }

  @Override
  public void notifyMap(final Map map, final MapEventBus mapEventBus) {
    this.mapEventBus = mapEventBus;
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}