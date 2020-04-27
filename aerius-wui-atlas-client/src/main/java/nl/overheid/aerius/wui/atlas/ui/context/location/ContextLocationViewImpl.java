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
package nl.overheid.aerius.wui.atlas.ui.context.location;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

import nl.overheid.aerius.geo.command.InformationLayerActiveCommand;
import nl.overheid.aerius.geo.wui.Map;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.wui.atlas.daemon.layer.LayerLoaderUtil;
import nl.overheid.aerius.wui.atlas.future.layer.LayerOracle;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.component.CompactMapSearchWidget;
import nl.overheid.aerius.wui.component.LayerPanelView;
import nl.overheid.aerius.wui.domain.map.MapContext;
import nl.overheid.aerius.wui.event.BundledRegistration;
import nl.overheid.aerius.wui.widget.EventComposite;

public class ContextLocationViewImpl extends EventComposite implements ContextLocationView {
  private static final ContextMapViewImplUiBinder UI_BINDER = GWT.create(ContextMapViewImplUiBinder.class);

  interface ContextMapViewImplUiBinder extends UiBinder<Widget, ContextLocationViewImpl> {}

  private final ContextMapViewImplEventBinder EVENT_BINDER = GWT.create(ContextMapViewImplEventBinder.class);

  interface ContextMapViewImplEventBinder extends EventBinder<ContextLocationViewImpl> {}

  @UiField(provided = true) CompactMapSearchWidget searchWidget;
  @UiField(provided = true) LayerPanelView layerPanel;
  @UiField SimplePanel mapPanel;
  private final Map map;
  private final MapContext mapContext;

  private final BundledRegistration registrations = new BundledRegistration();

  @Inject
  public ContextLocationViewImpl(final LayerPanelView layerPanel, final Map map, final MapContext mapContext,
      final CompactMapSearchWidget searchWidget, final LayerOracle layerOracle) {
    this.layerPanel = layerPanel;
    this.map = map;
    this.mapContext = mapContext;
    this.searchWidget = searchWidget;

    initWidget(UI_BINDER.createAndBindUi(this));

    mapPanel.setWidget(map);

    registrations.add(mapContext.onMap(layerPanel));
    registrations.add(mapContext.onMap(searchWidget));
    registrations.add(mapContext.onMap((m, bus) -> LayerLoaderUtil.load(layerOracle, UglyBoilerPlate.DEFAULT_LAYERS, bus)));
    registrations.add(mapContext.onMap((m, bus) -> Scheduler.get().scheduleDeferred(() -> {
      bus.fireEvent(new InformationLayerActiveCommand());
    })));

    registrations.add(Window.addResizeHandler(e ->

    resizePanelHeight()));
    resizePanelHeight();
  }

  private void resizePanelHeight() {
    layerPanel.getElement().getStyle().setProperty("maxHeight", Window.getClientHeight() - 300, Unit.PX);
  }

  @Override
  public void show() {
    resizePanelHeight();
    mapContext.claimMapPrimacy(map);
    map.attach();
  }

  @Override
  public void retire() {
    registrations.retire();
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);

    layerPanel.setEventBus(eventBus);
    map.setEventBus(eventBus);
  }

  @Override
  public boolean hasPanelContent() {
    return true;
  }

  @Override
  public void setPanelContent(final PanelContent content) {}
}
