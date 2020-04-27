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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.wui.atlas.event.ToggleLayerPanelEvent;
import nl.overheid.aerius.wui.place.ApplicationPlace;
import nl.overheid.aerius.wui.place.DefaultPlace;
import nl.overheid.aerius.wui.place.PlaceController;
import nl.overheid.aerius.wui.widget.EventComposite;

@Singleton
public class AdditionalNavigation extends EventComposite {
  interface AdditionalNavigationEventBinder extends EventBinder<AdditionalNavigation> {}

  private final AdditionalNavigationEventBinder EVENT_BINDER = GWT.create(AdditionalNavigationEventBinder.class);

  private static final AdditionalNavigationUiBinder UI_BINDER = GWT.create(AdditionalNavigationUiBinder.class);

  interface AdditionalNavigationUiBinder extends UiBinder<Widget, AdditionalNavigation> {}

  public interface CustomStyle extends CssResource {
    String compact();
  }

  @UiField(provided = true) CompactLibraryWidget libraryWidget;
  @UiField(provided = true) DataSetListWidget dataSetWidget;
  @UiField(provided = true) YearListWidget yearWidget;
  @UiField(provided = true) PanelNavigation contextNavigation;

  @UiField(provided = true) StoryHideButton exitButton;

  @UiField CustomStyle style;

  @UiField FlowPanel container;

  private final PlaceController placeController;
  private final ApplicationPlace defaultPlace;

  @Inject
  public AdditionalNavigation(final PanelNavigation contextNavigation, final CompactLibraryWidget libraryWidget,
      final DataSetListWidget dataSetWidget, final YearListWidget yearWidget, final PlaceController placeController,
      @DefaultPlace final ApplicationPlace defaultPlace, final StoryHideButton exitButton) {
    this.contextNavigation = contextNavigation;
    this.libraryWidget = libraryWidget;
    this.dataSetWidget = dataSetWidget;
    this.yearWidget = yearWidget;
    this.exitButton = exitButton;

    this.placeController = placeController;
    this.defaultPlace = defaultPlace;

    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @UiHandler("logo")
  public void onLogoClick(final ClickEvent e) {
    placeController.goTo(defaultPlace);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, contextNavigation, libraryWidget, dataSetWidget, yearWidget, exitButton);

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @EventHandler
  public void onToggleLayerPanelEvent(final ToggleLayerPanelEvent e) {
    contextNavigation.setPanelSelection(PanelNames.PANEL_LAYER, e.getValue());
  }

  // @UiHandler("compactToggle")
  // public void onCompactClick(final ClickEvent e) {
  // container.setStyleName(style.compact(), !container.getStyleName().contains(style.compact()));
  // }
}
