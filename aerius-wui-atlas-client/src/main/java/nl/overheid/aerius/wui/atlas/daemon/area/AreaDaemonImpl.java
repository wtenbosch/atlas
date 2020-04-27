package nl.overheid.aerius.wui.atlas.daemon.area;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.command.MapSetExtentCommand;
import nl.overheid.aerius.geo.domain.IsMapCohort;
import nl.overheid.aerius.geo.event.MapEventBus;
import nl.overheid.aerius.geo.wui.Map;
import nl.overheid.aerius.wui.atlas.event.SelectorEvent;
import nl.overheid.aerius.wui.atlas.event.StoryFragmentChangedEvent;
import nl.overheid.aerius.wui.atlas.service.LayerServiceAsync;
import nl.overheid.aerius.wui.atlas.util.ViewModeUtil;
import nl.overheid.aerius.wui.domain.map.MapContext;
import nl.overheid.aerius.wui.domain.story.StoryContext;
import nl.overheid.aerius.wui.event.BasicEventComponent;
import nl.overheid.aerius.wui.util.SelectorUtil;

public class AreaDaemonImpl extends BasicEventComponent implements AreaDaemon, IsMapCohort {
  private final AreaDaemonImplEventBinder EVENT_BINDER = GWT.create(AreaDaemonImplEventBinder.class);

  interface AreaDaemonImplEventBinder extends EventBinder<AreaDaemonImpl> {}

  private final LayerServiceAsync bboxService;
  private final StoryContext storyContext;

  private boolean active;
  private MapEventBus mapEventBus;
  
  private String currentArea;

  @Inject
  public AreaDaemonImpl(final StoryContext storyContext, final LayerServiceAsync bboxService, final MapContext mapContext) {
    this.storyContext = storyContext;
    this.bboxService = bboxService;

    mapContext.registerPrimaryMapCohort(this);
  }

  @EventHandler
  public void onStoryFragmentChangedEvent(final StoryFragmentChangedEvent e) {
    if (ViewModeUtil.NATIONAL.equals(e.getValue().viewMode())) {
      active = true;
    } else {
      active = false;
    }
  }

  @EventHandler
  public void onSelectorEvent(final SelectorEvent e) {
    if (!active) {
      return;
    }

    if (mapEventBus == null) {
      return;
    }

    if (!SelectorUtil.matchesStrict("natura2000AreaCode", e)) {
      return;
    }

    e.getValue().getValue().ifPresent(area -> {
      if (currentArea != null && currentArea.equals(area)) {
        return;
      }
      
      currentArea = area;
      bboxService.getBbox(storyContext.getDataset(), area, v -> mapEventBus.fireEvent(new MapSetExtentCommand(v.getBbox(), true)));
    });
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }

  @Override
  public void notifyMap(final Map map, final MapEventBus mapEventBus) {
    this.mapEventBus = mapEventBus;
  }
}
