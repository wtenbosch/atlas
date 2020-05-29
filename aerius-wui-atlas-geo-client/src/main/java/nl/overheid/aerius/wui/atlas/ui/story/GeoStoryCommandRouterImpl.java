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
package nl.overheid.aerius.wui.atlas.ui.story;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.event.InfoLocationChangeEvent;
import nl.overheid.aerius.geo.util.ReceptorUtil;
import nl.overheid.aerius.shared.domain.PanelConfiguration;
import nl.overheid.aerius.shared.domain.PanelType;
import nl.overheid.aerius.wui.atlas.command.PanelSelectionChangeCommand;
import nl.overheid.aerius.wui.atlas.command.ToggleLayerPanelCommand;
import nl.overheid.aerius.wui.atlas.event.StoryLoadedEvent;
import nl.overheid.aerius.wui.atlas.event.ToggleLayerPanelEvent;
import nl.overheid.aerius.wui.atlas.place.StoryPlace;
import nl.overheid.aerius.wui.command.AbstractCommandRouter;
import nl.overheid.aerius.wui.domain.map.MapContext;
import nl.overheid.aerius.wui.domain.story.StoryContext;
import nl.overheid.aerius.wui.place.ApplicationPlace;
import nl.overheid.aerius.wui.place.PlaceController;

public class GeoStoryCommandRouterImpl extends AbstractCommandRouter implements GeoStoryCommandRouter {
  private static final GeoStoryCommandRouterImplEventBinder EVENT_BINDER = GWT.create(GeoStoryCommandRouterImplEventBinder.class);

  interface GeoStoryCommandRouterImplEventBinder extends EventBinder<GeoStoryCommandRouterImpl> {}

  @Inject protected ReceptorUtil receptorUtil;
  @Inject private MapContext mapContext;

  @Inject private PlaceController placeController;

  @Inject private StoryContext context;

  private boolean layerPanelVisible;

  @EventHandler
  public void onStoryChangeEvent(final StoryLoadedEvent e) {
    final StoryPlace previousPlace = getStoryPlaceOrNull(placeController.getPreviousPlace());
    final StoryPlace currentPlace = getStoryPlace(placeController.getPlace());

    final String prevReceptorId = previousPlace == null ? null : previousPlace.getReceptorId();
    final String currReceptorId = currentPlace.getReceptorId();

    fireIfChanged(new InfoLocationChangeEvent(),
        prevReceptorId == null ? null : receptorUtil.createReceptorPointFromId(Integer.parseInt(prevReceptorId)),
        currReceptorId == null ? null : receptorUtil.createReceptorPointFromId(Integer.parseInt(currReceptorId)));
  }

  @EventHandler
  public void onToggleLayerPanelEvent(final ToggleLayerPanelEvent c) {
    layerPanelVisible = c.getValue();
  }

  @EventHandler
  public void onPanelSelectionChangeCommand(final PanelSelectionChangeCommand c) {
    final PanelConfiguration conf = context.getPanels().get(c.getValue());
    if (conf == null) {
      return;
    }

    if (conf.asConfigurationProperties().getPanelType() == PanelType.LAYERS && conf.isIndependent()) {
      eventBus.fireEventFromSource(new ToggleLayerPanelCommand(!layerPanelVisible), mapContext.getActiveMap());
    } else if (conf.asConfigurationProperties().getPanelType() != PanelType.MAP) {
      layerPanelVisible = false;
    }
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }

  protected StoryPlace getStoryPlaceOrNull(final ApplicationPlace place) {
    return getStoryPlace(place, false);
  }

  protected StoryPlace getStoryPlace(final ApplicationPlace place) {
    return getStoryPlace(place, true);
  }

  protected StoryPlace getStoryPlace(final ApplicationPlace place, final boolean strict) {
    if (place instanceof StoryPlace) {
      return (StoryPlace) place;
    } else if (strict) {
      throw new RuntimeException("[StoryCommandRouterImpl] Unreachable state attained. PlaceChangeEvent of type " + place.getClass().getSimpleName()
          + " received while not supposed to be able to receive this type.");
    } else {
      return null;
    }
  }
}
