package nl.overheid.aerius.wui.atlas.daemon.layer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.domain.IsMapCohort;
import nl.overheid.aerius.geo.event.InfoLocationChangeEvent;
import nl.overheid.aerius.geo.event.MapEventBus;
import nl.overheid.aerius.geo.wui.Map;
import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.wui.atlas.event.ChapterSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.event.DataSetChangeEvent;
import nl.overheid.aerius.wui.atlas.event.StoryLoadedEvent;
import nl.overheid.aerius.wui.atlas.event.StoryLoadingEvent;
import nl.overheid.aerius.wui.atlas.future.layer.LayerOracle;
import nl.overheid.aerius.wui.domain.cache.CacheContext;
import nl.overheid.aerius.wui.domain.map.MapContext;
import nl.overheid.aerius.wui.event.BasicEventComponent;

@Singleton
public class LayerPanelDaemonImpl extends BasicEventComponent implements LayerPanelDaemon, IsMapCohort {
  interface LayerPanelDaemonImplEventBinder extends EventBinder<LayerPanelDaemonImpl> {}

  private final LayerPanelDaemonImplEventBinder EVENT_BINDER = GWT.create(LayerPanelDaemonImplEventBinder.class);

  private final Set<String> loadedLayers = new HashSet<>();

  private MapEventBus activeEventBus;

  private final LayerOracle oracle;

  private Runnable nextPrimaryMapAction;

  @Inject
  public LayerPanelDaemonImpl(final LayerOracle oracle, final CacheContext cache, final MapContext mapContext) {
    this.oracle = oracle;

    mapContext.registerPrimaryMapCohort(this);
    cache.register(loadedLayers);
  }

  @EventHandler
  public void onStoryLoadingEvent(final StoryLoadingEvent e) {
    loadedLayers.clear();
  }

  @EventHandler
  public void onStoryLoadedgEvent(final StoryLoadedEvent e) {
    loadedLayers.clear();
  }

  @EventHandler
  public void onDataSetChangeEvent(final DataSetChangeEvent e) {
    loadedLayers.clear();
  }

  @EventHandler
  public void onInfoLocationChangeEvent(final InfoLocationChangeEvent e) {
    // loadedLayers.clear();
  }

  @EventHandler
  public void onChapterSelectionChangeEvent(final ChapterSelectionChangeEvent e) {
    final Chapter chapter = e.getValue();
    final PanelContent panelContent = chapter.panels().get(PanelNames.PANEL_LAYER);
    if (panelContent == null) {
      return;
    }

    final String panelKey = chapter.uid();
    if (loadedLayers.contains(panelKey)) {
      return;
    }

    nextPrimaryMapAction = () -> {
      final List<String> layers = panelContent.asLayerProperties().getLayers();
      if (layers != null) {
        LayerLoaderUtil.load(oracle, layers, activeEventBus);
      }

      loadedLayers.add(panelKey);
    };
  }

  @Override
  public void notifyMap(final Map map, final MapEventBus mapEventBus) {
    this.activeEventBus = mapEventBus;
    if (nextPrimaryMapAction != null) {
      nextPrimaryMapAction.run();
      nextPrimaryMapAction = null;
    }
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
