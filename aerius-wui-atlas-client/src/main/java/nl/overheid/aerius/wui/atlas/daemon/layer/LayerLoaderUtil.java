package nl.overheid.aerius.wui.atlas.daemon.layer;

import java.util.List;

import com.google.gwt.core.client.Scheduler;

import nl.overheid.aerius.geo.command.LayerAddedCommand;
import nl.overheid.aerius.geo.event.MapEventBus;
import nl.overheid.aerius.geo.util.MapLayerUtil;
import nl.overheid.aerius.wui.atlas.command.BroadcastSelectorsCommand;
import nl.overheid.aerius.wui.atlas.future.layer.LayerOracle;

public class LayerLoaderUtil {
  public LayerLoaderUtil(final LayerOracle oracle, final List<String> layers, final MapEventBus eventBus) {
    // Defer so these aren't loaded (out of cache) before base layers.
    Scheduler.get().scheduleDeferred(() -> {
      layers.forEach(v -> oracle.getLayer(v, c -> {
        c.forEach(layer -> eventBus.fireEvent(new LayerAddedCommand(MapLayerUtil.prepareLayer(layer))));
      }));

      oracle.subscribeCompletion(() -> eventBus.fireEvent(new BroadcastSelectorsCommand()));
    });
  }

  public static void load(final LayerOracle oracle, final List<String> layers, final MapEventBus eventBus) {
    new LayerLoaderUtil(oracle, layers, eventBus);
  }
}
