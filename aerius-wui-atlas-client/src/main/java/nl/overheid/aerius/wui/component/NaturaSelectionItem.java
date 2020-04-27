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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.shared.domain.NatureArea;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.util.AreaGroupImageUtil;

public class NaturaSelectionItem extends Composite implements HasClickHandlers {
  private static final NaturaSelectionItemUiBinder UI_BINDER = GWT.create(NaturaSelectionItemUiBinder.class);

  interface NaturaSelectionItemUiBinder extends UiBinder<Widget, NaturaSelectionItem> {}

  @UiField Label areaLabel;
  @UiField(provided = true) Image authorityIcon;
  private final NatureArea area;

  public NaturaSelectionItem(final NatureArea area) {
    this.area = area;
    authorityIcon = new Image(AreaGroupImageUtil.getImageResource(area.getAuthority()).getSafeUri());

    initWidget(UI_BINDER.createAndBindUi(this));

    areaLabel.setText(area.getName());

    ensureDebugId(AtlasTestIDs.NATURA_SELECTION_ITEM + "-" + area.getName());

    sinkEvents(Event.CLICK);
  }

  @Override
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

  public void addMouseOverHandler(final Consumer<NatureArea> consumer) {
    addDomHandler(e -> consumer.accept(area), MouseOverEvent.getType());
  }

  public void addMouseOutHandler(final Consumer<NatureArea> consumer) {
    addDomHandler(e -> consumer.accept(area), MouseOutEvent.getType());
  }
}
