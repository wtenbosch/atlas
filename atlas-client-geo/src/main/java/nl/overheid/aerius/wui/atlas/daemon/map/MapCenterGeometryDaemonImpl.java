package nl.overheid.aerius.wui.atlas.daemon.map;

import com.google.gwt.core.client.GWT;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.BBox;
import nl.overheid.aerius.geo.domain.Point;
import nl.overheid.aerius.geo.event.RequestExtentCorrectionEvent;
import nl.overheid.aerius.wui.atlas.command.MapCenterChangeCommand;
import nl.overheid.aerius.wui.atlas.event.ContextCompositionChangeEvent;
import nl.overheid.aerius.wui.atlas.event.MapActiveEvent;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.event.BasicEventComponent;

@Singleton
public class MapCenterGeometryDaemonImpl extends BasicEventComponent implements MapCenterGeometryDaemon {
  private final MapCenterGeometryDaemonEventBinder EVENT_BINDER = GWT.create(MapCenterGeometryDaemonEventBinder.class);

  interface MapCenterGeometryDaemonEventBinder extends EventBinder<MapCenterGeometryDaemonImpl> {}

  private int contextWidth;

  private boolean mapActive;

  @EventHandler
  public void onContextCompositionChangeEvent(final ContextCompositionChangeEvent e) {
    contextWidth = e.getValue();
  }

  @EventHandler
  public void onMapActiveEvent(final MapActiveEvent e) {
    mapActive = e.getValue();
  }

  @EventHandler
  public void onMapCenterChangeCommand(final MapCenterChangeCommand c) {
//    c.setValue(correctCenterCropLeft(contextWidth, c.getViewport().getClientWidth(), c.getBbox(), c.getValue()));
  }

  @EventHandler
  public void onRequestExtentCorrectionEvent(final RequestExtentCorrectionEvent extent) {
    if (!mapActive) {
      return;
    }

    final BBox correctedBox = correctBox(contextWidth, extent.getValue(), extent.getViewport().getClientWidth());

    extent.setCorrectedBox(correctedBox);
  }

  private Point correctCenterCropLeft(final double contextWidth, final double fullWidth, final BBox box, final Point original) {
    final double newFakeWidth = box.getWidth() * (1 + fullWidth / contextWidth);
    
    GWTProd.log("NEW FAKE WIDTH: " + newFakeWidth);
    if (newFakeWidth == 0) {
      
    }
    
    final int x = (int) Math.round(box.getMaxX() - newFakeWidth / 2);

    return new Point(x, original.getY());
  }

  public BBox correctBox(final double contextWidth, final BBox box, final double fullWidth) {
    // Ignore when there's no width to work with.
    if (fullWidth == 0 || fullWidth == contextWidth) {
      return box;
    }
    
//    GWTProd.log("MAP", "");
//    GWTProd.log("MAP", "hor span: " + box.getWidth());
//    GWTProd.log("MAP", "Width: " + contextWidth + " > Full width: " + fullWidth);

    final double multiple = 1 / (1 - contextWidth / fullWidth);
    final double newFakeWidth = box.getWidth() * multiple;

//    GWTProd.log("MAP", "Artificial elem width: " + fullWidth * multiple);
//
//    GWTProd.log("MAP", "Multiple: " + multiple);
//    GWTProd.log("MAP", "New width: " + newFakeWidth);
//
//    GWTProd.log("MAP", "Pre x: " + box.getMinX() + " > " + box.getMaxX());
//    GWTProd.log("MAP", "New x: " + (box.getMaxX() - newFakeWidth));
//
//    GWTProd.log("MAP", box.getMinX() + " > " + box.getMaxY());

    final BBox newBox = new BBox(box.getMaxX() - newFakeWidth, box.getMinY(), box.getMaxX(), box.getMaxY());

    return newBox;
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
