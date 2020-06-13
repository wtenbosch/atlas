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
package nl.overheid.aerius.wui.atlas.ui.context.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.wui.atlas.event.SelectorConfigurationChangeEvent;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.atlas.ui.context.info.BasicEventPanelComposite;
import nl.overheid.aerius.wui.component.CollapsibleLayerPanelSwitcher;
import nl.overheid.aerius.wui.component.CompactMapSearchWidget;
import nl.overheid.aerius.wui.component.MapPanelSwitcher;
import nl.overheid.aerius.wui.domain.map.MapContext;
import nl.overheid.aerius.wui.event.BundledRegistration;
import nl.overheid.aerius.wui.util.SelectorUtil;
import nl.overheid.aerius.wui.util.SelectorWidgetUtil;

public class ContextMapViewImpl extends BasicEventPanelComposite implements ContextMapView {
  private static final ContextMapViewImplUiBinder UI_BINDER = GWT.create(ContextMapViewImplUiBinder.class);

  interface ContextMapViewImplUiBinder extends UiBinder<Widget, ContextMapViewImpl> {}

  private final ContextMapViewImplEventBinder EVENT_BINDER = GWT.create(ContextMapViewImplEventBinder.class);

  interface ContextMapViewImplEventBinder extends EventBinder<ContextMapViewImpl> {}

  @UiField(provided = true) CompactMapSearchWidget searchWidget;
  @UiField(provided = true) MapPanelSwitcher mapPanel;
  @UiField(provided = true) CollapsibleLayerPanelSwitcher layerPanel;

  @UiField FlowPanel selectorContainer;
  private final List<String> selectables = new ArrayList<>();
  private final Map<String, Integer> selectableConfigs = new HashMap<>();

  private final BundledRegistration registrations = new BundledRegistration();

  @Inject
  public ContextMapViewImpl(final CollapsibleLayerPanelSwitcher layerPanel, final MapPanelSwitcher mapPanel, final MapContext mapContext,
      final CompactMapSearchWidget searchWidget) {
    this.mapPanel = mapPanel;
    this.layerPanel = layerPanel;
    this.searchWidget = searchWidget;

    initWidget(UI_BINDER.createAndBindUi(this));

    registrations.add(mapContext.registerPrimaryMapCohort(layerPanel));
    registrations.add(mapContext.registerPrimaryMapCohort(searchWidget));

    registrations.add(Window.addResizeHandler(e -> resizePanelHeight()));
    resizePanelHeight();
    ensureDebugId(AtlasTestIDs.MAP);
  }

  @Override
  public void retire() {
    registrations.retire();
  }

  private void resizePanelHeight() {
    layerPanel.getElement().getStyle().setProperty("maxHeight", Window.getClientHeight() - 300, Unit.PX);
  }

  @Override
  public void setPanelContent(final PanelContent content) {
    super.setPanelContent(content);

    // Clear existing content
    selectorContainer.clear();
    selectables.clear();
    selectableConfigs.clear();

    mapPanel.show();

    // If no new content, go away
    if (content == null) {
      return;
    }

    SelectorWidgetUtil.populateSelectorWidget(selectorContainer, content.selectables(), eventBus);
    selectables.addAll(content.selectables());
  }

  @EventHandler
  public void onSelectorListChanged(final SelectorConfigurationChangeEvent e) {
    if (!SelectorUtil.matchesStrict(e.getType(), selectables)) {
      return;
    }

    selectableConfigs.put(e.getType(), e.getValue().getOptions().size());

    selectorContainer.setVisible(selectableConfigs.entrySet().stream().mapToInt(v -> v.getValue()).sum() > 0);
  }

  @Override
  public boolean supportsPanelContent(final PanelNames name) {
    return name == PanelNames.PANEL_MAP;
  }

  @Override
  public void show() {
    mapPanel.show();
    resizePanelHeight();
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);

    layerPanel.setEventBus(eventBus);
    mapPanel.setEventBus(eventBus);
  }
}
