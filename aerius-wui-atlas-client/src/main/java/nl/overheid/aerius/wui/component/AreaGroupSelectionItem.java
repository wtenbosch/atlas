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
import java.util.function.Consumer;

import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.NatureArea;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.i18n.M;

public class AreaGroupSelectionItem extends Composite implements HasClickHandlers {
  private static final ProvinceSelectionItemUiBinder UI_BINDER = GWT.create(ProvinceSelectionItemUiBinder.class);

  interface ProvinceSelectionItemUiBinder extends UiBinder<Widget, AreaGroupSelectionItem> {}

  private final AreaGroupType areaGroup;

  @UiField Label areaGroupName;
  @UiField Label naturaCounter;

  public AreaGroupSelectionItem(final AreaGroupType areaGroup, final List<NatureArea> areas) {
    this.areaGroup = areaGroup;
    initWidget(UI_BINDER.createAndBindUi(this));

    areaGroupName.setText(M.messages().areaGroupName(areaGroup));
    naturaCounter.setText(String.valueOf(areas.size()));

    ensureDebugId(AtlasTestIDs.AREA_GROUP_SELECTION + "-" + areaGroup.getName());

    sinkEvents(Event.CLICK);
  }

  @Override
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

  public void addMouseOverHandler(final Consumer<AreaGroupType> consumer) {
    addDomHandler(e -> consumer.accept(areaGroup), MouseOverEvent.getType());
  }

  public void addMouseOutHandler(final Runnable runnable) {
    addDomHandler(e -> runnable.run(), MouseOutEvent.getType());
  }
}
