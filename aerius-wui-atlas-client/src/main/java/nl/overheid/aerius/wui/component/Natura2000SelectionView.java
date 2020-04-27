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
package nl.overheid.aerius.wui.component;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.NatureArea;
import nl.overheid.aerius.wui.atlas.command.AreaGroupChangeCommand;
import nl.overheid.aerius.wui.atlas.command.NaturaChangeCommand;
import nl.overheid.aerius.wui.atlas.event.AreaGroupChangeEvent;
import nl.overheid.aerius.wui.atlas.event.AreaGroupListChangedEvent;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.i18n.M;
import nl.overheid.aerius.wui.util.StyleUtil;
import nl.overheid.aerius.wui.widget.EventComposite;
import nl.overheid.aerius.wui.widget.NavigationLabel;
import nl.overheid.aerius.wui.widget.SwitchPanel;

public class Natura2000SelectionView extends EventComposite {
  interface AreaGroupEventBinder extends EventBinder<Natura2000SelectionView> {}

  private final AreaGroupEventBinder EVENT_BINDER = GWT.create(AreaGroupEventBinder.class);

  private static final Natura2000SelectionViewUiBinder UI_BINDER = GWT.create(Natura2000SelectionViewUiBinder.class);

  interface Natura2000SelectionViewUiBinder extends UiBinder<Widget, Natura2000SelectionView> {}

  @UiField SwitchPanel areaGroupSwitchPanel;
  @UiField SwitchPanel areaResultList;

  @UiField SwitchPanel provinceSwitchPanel;

  @UiField TextBox searchField;
  private String currentFilter;

  @UiField FlowPanel provinceList;

  @UiField ProvinceSelectionGraph provinceImageSelectionGraph;
  @UiField Natura2000ProvinceSelectionGraph natura2000ProvinceSelectionGraph;

  @UiField NavigationLabel areaGroupNavigationLabel;
  @UiField FlowPanel areaList;

  private Map<AreaGroupType, List<NatureArea>> areaGroups;

  public Natura2000SelectionView() {
    initWidget(UI_BINDER.createAndBindUi(this));

    areaGroupSwitchPanel.showWidget(0);
    provinceSwitchPanel.showWidget(0);

    natura2000ProvinceSelectionGraph.onNaturaSelect(area -> eventBus.fireEvent(new NaturaChangeCommand(area)));

    StyleUtil.setPlaceHolder(searchField, M.messages().searchNature2000PlaceHolder());

    searchField.addKeyUpHandler(e -> filterNatureAreas(searchField.getValue()));
    searchField.addValueChangeHandler(e -> filterNatureAreas(e.getValue()));

    searchField.ensureDebugId(AtlasTestIDs.SEARCH_AREA);
  }

  private void filterNatureAreas(final String value) {
    if (value == null || value.isEmpty()) {
      showAreaGroups();
      provinceSwitchPanel.showWidget(1);

      currentFilter = null;
      areaGroupNavigationLabel.setText(null);
      return;
    }

    if (value.equals(currentFilter)) {
      return;
    }

    if (currentFilter == null) {
      eventBus.fireEvent(new AreaGroupChangeCommand());

      showNatureAreas();
      provinceSwitchPanel.showWidget(0);

      areaGroupNavigationLabel.setText(M.messages().searchResultText());
    }

    currentFilter = value;

    showAreas(areaGroups.values().stream()
        .flatMap(v -> v.stream())
        .filter(e -> e.getName().toLowerCase().contains(value.toLowerCase()))
        .sorted()
        .distinct()
        .collect(Collectors.toList()));
  }

  @EventHandler
  public void onProvinceListChangedEvent(final AreaGroupListChangedEvent e) {
    areaGroups = e.getValue();

    provinceList.clear();

    for (final Entry<AreaGroupType, List<NatureArea>> entry : areaGroups.entrySet()) {
      final AreaGroupSelectionItem item = new AreaGroupSelectionItem(entry.getKey(), entry.getValue());
      item.addClickHandler(ev -> eventBus.fireEvent(new AreaGroupChangeCommand(entry.getKey(), entry.getValue())));
      item.addMouseOverHandler(grp -> provinceImageSelectionGraph.highlight(grp));
      item.addMouseOutHandler(() -> provinceImageSelectionGraph.clearHighlight());
      provinceList.add(item);
    }
  }

  @EventHandler
  public void onProvinceSelectionChangedEvent(final AreaGroupChangeEvent e) {
    // If the search field is not empty (ie. user is searching..), just do nothing
    if (searchField.getValue() != null && !searchField.getValue().equals("")) {
      return;
    }

    // If the value is being reset (eg. back button was pressed) and nothing else is
    // happening, reset
    if (e.getValue() == null) {
      provinceSwitchPanel.showWidget(0);
      showAreaGroups();
      return;
    }

    // Otherwise set nature areas
    setNatureAreas(e.getValue(), e.getAssessmentAreas());
  }

  private void setNatureAreas(final AreaGroupType group, final List<NatureArea> areas) {
    showNatureAreas();
    natura2000ProvinceSelectionGraph.setAreaGroup(group, areas);
    provinceSwitchPanel.showWidget(1);
    areaGroupNavigationLabel.setText(M.messages().areaGroupName(group));
    showAreas(areas);
  }

  private void showAreas(final List<NatureArea> areas) {
    areaResultList.showWidget(areas.isEmpty() ? 0 : 1);

    areaList.clear();
    for (final NatureArea area : areas) {
      final NaturaSelectionItem item = new NaturaSelectionItem(area);
      item.addClickHandler((ev) -> eventBus.fireEvent(new NaturaChangeCommand(area)));
      item.addMouseOverHandler(a -> highlight(a));
      item.addMouseOutHandler(a -> clearHighlight(a));
      areaList.add(item);
    }
  }

  private void clearHighlight(final NatureArea a) {
    natura2000ProvinceSelectionGraph.clearHighlight(a);
    provinceImageSelectionGraph.clearHighlight(a);
  }

  private void highlight(final NatureArea a) {
    provinceImageSelectionGraph.highlight(a);
    natura2000ProvinceSelectionGraph.highlight(a);
  }

  @UiHandler("areaGroupNavigationLabel")
  public void onAreaBackButtonClick(final ClickEvent e) {
    searchField.setValue(null, true);
    eventBus.fireEvent(new AreaGroupChangeCommand());
    areaGroupNavigationLabel.setText(null);
    provinceSwitchPanel.showWidget(0);
  }

  private void showAreaGroups() {
    areaGroupSwitchPanel.showWidget(0);
  }

  private void showNatureAreas() {
    areaGroupSwitchPanel.showWidget(1);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER, provinceImageSelectionGraph);
  }
}
