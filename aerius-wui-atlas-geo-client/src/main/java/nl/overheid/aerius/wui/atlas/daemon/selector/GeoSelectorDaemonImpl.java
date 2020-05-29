package nl.overheid.aerius.wui.atlas.daemon.selector;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.event.InfoLocationChangeEvent;
import nl.overheid.aerius.wui.atlas.command.ReloadSelectorsCommand;
import nl.overheid.aerius.wui.event.BasicEventComponent;

public class GeoSelectorDaemonImpl extends BasicEventComponent implements GeoSelectorDaemon {
  private static final GeoSelectorDaemonImplEventBinder EVENT_BINDER = GWT.create(GeoSelectorDaemonImplEventBinder.class);

  interface GeoSelectorDaemonImplEventBinder extends EventBinder<GeoSelectorDaemonImpl> {}

  @EventHandler
  public void onInfoLocationChangedEvent(final InfoLocationChangeEvent e) {
    eventBus.fireEvent(new ReloadSelectorsCommand());
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
