package nl.overheid.aerius.wui.atlas.daemon.map;

import java.util.Optional;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.google.web.bindery.event.shared.binder.GenericEvent;

import nl.overheid.aerius.geo.command.InformationLayerActiveCommand;
import nl.overheid.aerius.geo.command.MapResizeCommand;
import nl.overheid.aerius.geo.command.MapSetExtentCommand;
import nl.overheid.aerius.geo.domain.IsMapCohort;
import nl.overheid.aerius.geo.domain.ReceptorPoint;
import nl.overheid.aerius.geo.event.InfoLocationChangeEvent;
import nl.overheid.aerius.geo.event.MapEventBus;
import nl.overheid.aerius.geo.event.MapSetExtentEvent;
import nl.overheid.aerius.geo.event.MapSetShortcircuitedExtentEvent;
import nl.overheid.aerius.geo.wui.Map;
import nl.overheid.aerius.wui.atlas.command.ActivateBigContextCommand;
import nl.overheid.aerius.wui.atlas.command.ActivateSmallContextCommand;
import nl.overheid.aerius.wui.atlas.command.StoryExitCommand;
import nl.overheid.aerius.wui.atlas.event.ContextPanelCollapseEvent;
import nl.overheid.aerius.wui.atlas.event.ContextPanelOpenEvent;
import nl.overheid.aerius.wui.atlas.event.LibraryChangeEvent;
import nl.overheid.aerius.wui.atlas.event.MapSearchSuggestionEvent;
import nl.overheid.aerius.wui.atlas.event.NatureAreaChangedEvent;
import nl.overheid.aerius.wui.atlas.event.StoryFragmentChangedEvent;
import nl.overheid.aerius.wui.atlas.util.ViewModeUtil;
import nl.overheid.aerius.wui.domain.map.MapContext;
import nl.overheid.aerius.wui.event.BasicEventComponent;

public class MapDaemonImpl extends BasicEventComponent implements MapDaemon, IsMapCohort {
  interface MapDaemonImplEventBinder extends EventBinder<MapDaemonImpl> {}

  private final MapDaemonImplEventBinder EVENT_BINDER = GWT.create(MapDaemonImplEventBinder.class);

  static class MapEvents extends BasicEventComponent {
    interface MapEventsBinder extends EventBinder<MapEvents> {}

    private final MapEventsBinder EVENT_BINDER = GWT.create(MapEventsBinder.class);

    private HandlerRegistration registration;

    private String lastExtent;
    private String lastUserExtent;
    private String lastInnerExtent;

    @Override
    public void setEventBus(final EventBus eventBus) {
      if (registration != null) {
        registration.removeHandler();
      }

      registration = super.setEventBus(eventBus, this, EVENT_BINDER);
    }

    @EventHandler
    public void onMapSearchSuggestionEvent(final MapSearchSuggestionEvent e) {
      Optional.ofNullable(e.getValue().extent()).ifPresent(v -> eventBus.fireEvent(new MapSetExtentCommand(v, true)));

      switch (e.getValue().type()) {
      case RECEPTOR:
        eventBus.fireEvent(new InfoLocationChangeEvent((ReceptorPoint) e.getValue().payload()));
        break;
      case N2000_AREA:
        eventBus.fireEvent(new NatureAreaChangedEvent((String) e.getValue().payload(), e.getValue()));
        break;
      case ADDRESS:
      case CITY:
      case COORDINATE:
      case MUNICIPALITY:
      default:
        // Do nothing
        break;
      }
    }

    /**
     * Only remember when user initiates change
     */
    @EventHandler
    public void onMapSetExtentCommand(final MapSetExtentCommand c) {
      if (c.isFromMap()) {
//        GWTProd.log("MAP", "Silencing command.");
        c.silence();
      }

      if (c.isPersistOnly()) {
//        GWTProd.log("MAP", "Persisting inner extent (direct persist) " + c.getValue());
        c.silence();
        lastUserExtent = c.getValue();
        lastInnerExtent = c.getValue();
        lastExtent = null;
        return;
      }

      if (!c.isUserInitiated() && lastUserExtent != null) {
        c.silence();
        return;
      }

      if (c.isUserInitiated()) {
//        GWTProd.log("MAP", "Persisting user extent (persist) " + c.getValue());
        lastUserExtent = c.getValue();
        lastInnerExtent = c.getValue();
        lastExtent = null;
      } else if (lastUserExtent != null) {
//        GWTProd.log("MAP", "Persisting user extent (repatch) " + c.getValue());
        lastInnerExtent = lastUserExtent;
      } else {
//        GWTProd.log("MAP", "Persisting extent (last resort)" + lastExtent);
        lastExtent = c.getValue();
      }
    }

    public String getLastInnerExtent() {
      return lastInnerExtent;
    }

    public String getLastExtent() {
      return lastExtent;
    }

    public void clear() {
      lastUserExtent = null;
      lastInnerExtent = null;
      lastExtent = null;      
    }
  }

  private final MapEvents mapEvents;

  private MapEventBus mapEventBus;

  private boolean informationLayerActive;

  @Inject
  public MapDaemonImpl(final MapContext mapContext) {
    mapContext.registerPrimaryMapCohort(this);

    mapEvents = new MapEvents();
  }

  @EventHandler(handles = { ContextPanelCollapseEvent.class, ContextPanelOpenEvent.class, ActivateBigContextCommand.class,
      ActivateSmallContextCommand.class })
  public void onResizeEvent(final GenericEvent e) {
    delayResize(300);
    delayResize(600);
  }

  private void delayResize(final int ms) {
    new Timer() {
      @Override
      public void run() {
        if (mapEventBus != null) {
          mapEventBus.fireEvent(new MapResizeCommand());
        }
      }
    }.schedule(ms);
  }

  @EventHandler
  public void onLibraryChangeEvent(final LibraryChangeEvent e) {
    if (mapEvents != null) {
      mapEvents.clear(); 
    }
  }
  
  @EventHandler
  public void onStoryFragmentChangeEvent(final StoryFragmentChangedEvent e) {
    informationLayerActive = ViewModeUtil.LOCATION.equals(e.getValue().viewMode());
  }
  
  @EventHandler
  public void onExitCommand(final StoryExitCommand c) {
    if (mapEvents != null) {
      mapEvents.clear(); 
    }
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }

  @Override
  public void notifyMap(final Map map, final MapEventBus mapEventBus) {
    this.mapEventBus = mapEventBus;
    final String lastInnerExtent = mapEvents.getLastInnerExtent();
    final String lastExtent = mapEvents.getLastExtent();

    mapEvents.setEventBus(mapEventBus);
    
    if (informationLayerActive) {
      mapEventBus.fireEvent(new InformationLayerActiveCommand());
    }
    
    if (lastInnerExtent != null) {
//      GWTProd.log("MAP", "Setting via inner extent: " + lastInnerExtent);
      mapEventBus.fireEvent(new MapSetShortcircuitedExtentEvent(lastInnerExtent));
    } else if (lastExtent != null) {
//      GWTProd.log("MAP", "Setting via extent: " + lastExtent);
      mapEventBus.fireEvent(new MapSetExtentEvent(lastExtent));
    }
  }
}
