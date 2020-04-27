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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.domain.IsMapCohort;
import nl.overheid.aerius.geo.event.MapEventBus;
import nl.overheid.aerius.geo.wui.Map;
import nl.overheid.aerius.wui.atlas.daemon.search.SearchSuggestionDaemon;
import nl.overheid.aerius.wui.atlas.event.MapSearchSuggestionEvent;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.i18n.M;
import nl.overheid.aerius.wui.util.StyleUtil;
import nl.overheid.aerius.wui.widget.EventComposite;

public class MapSearchWidgetImpl extends EventComposite implements MapSearchWidget, IsMapCohort {
  interface MapSearchWidgetImplEventBinder extends EventBinder<MapSearchWidgetImpl> {}

  private final MapSearchWidgetImplEventBinder EVENT_BINDER = GWT.create(MapSearchWidgetImplEventBinder.class);

  private static final MapSearchWidgetImplUiBinder UI_BINDER = GWT.create(MapSearchWidgetImplUiBinder.class);

  interface MapSearchWidgetImplUiBinder extends UiBinder<Widget, MapSearchWidgetImpl> {}

  public interface CustomStyle extends CssResource {
    String focus();
  }

  private final SearchSuggestionDaemon searchDaemon;
  private MapEventBus mapEventBus;

  @UiField FlowPanel searchContainer;
  @UiField TextBox searchField;
  @UiField CustomStyle style;
  @UiField Image searchImage;

  private final MapSearchPopup popup;

  @Inject
  public MapSearchWidgetImpl(final SearchSuggestionDaemon searchDaemon) {
    this.searchDaemon = searchDaemon;

    initWidget(UI_BINDER.createAndBindUi(this));
    popup = new MapSearchPopup(this);

    StyleUtil.setPlaceHolder(searchField, M.messages().searchPlaceHolder());

    popup.addCloseHandler(() -> searchContainer.setStyleName(style.focus(), false));
    searchField.ensureDebugId(AtlasTestIDs.INPUT_MAP_SEARCH);
    searchImage.ensureDebugId(AtlasTestIDs.BUTTON_MAP_SEARCH);
  }

  @UiHandler("searchField")
  public void onSearchBoxFocus(final FocusEvent e) {
    search();
    searchContainer.setStyleName(style.focus(), true);
  }

  @UiHandler("searchImage")
  public void onSearchImageClick(final ClickEvent e) {
    search();
  }

  @UiHandler("searchField")
  public void onSearchBoxBlur(final BlurEvent e) {
    if (!popup.isShowing()) {
      searchContainer.setStyleName(style.focus(), false);
    }
  }

  @UiHandler("searchField")
  public void onSearchBoxKeyUp(final KeyUpEvent e) {
    if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
      popup.confirm();
    } else {
      search();
    }
  }

  @EventHandler
  public void onSearchSuggestionEvent(final MapSearchSuggestionEvent e) {
    popup.hide();
  }

  private void search() {
    popup.notifyChange();
    final String text = searchField.getText();
    if (text.length() < MIN_SEARCH_LENGTH) {
      return;
    }

    searchDaemon.registerMapSearchQuery(mapEventBus.getScopeId(), text, popup);
  }

  @Override
  public void notifyMap(final Map map, final MapEventBus mapEventBus) {
    super.setEventBus(mapEventBus, this, EVENT_BINDER, popup);
    this.mapEventBus = mapEventBus;
  }
}
