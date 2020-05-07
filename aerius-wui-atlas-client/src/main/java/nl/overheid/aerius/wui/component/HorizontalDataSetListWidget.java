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

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.DatasetConfiguration;
import nl.overheid.aerius.wui.atlas.command.DataSetChangeCommand;
import nl.overheid.aerius.wui.atlas.event.DataSetChangeEvent;
import nl.overheid.aerius.wui.atlas.event.DataSetListChangeEvent;
import nl.overheid.aerius.wui.i18n.AtlasM;
import nl.overheid.aerius.wui.widget.HorizontalMenuItemListWidget;
import nl.overheid.aerius.wui.widget.HorizontalMenuItemPopup;

public class HorizontalDataSetListWidget extends HorizontalMenuItemListWidget<DatasetConfiguration> {
  private final DataSetListWidgetEventBinder EVENT_BINDER = GWT.create(DataSetListWidgetEventBinder.class);

  interface DataSetListWidgetEventBinder extends EventBinder<HorizontalDataSetListWidget> {}

  private EventBus eventBus;

  public HorizontalDataSetListWidget() {
    super(AtlasM.messages().contextDataSetTitle(), AtlasM.messages().contextDataSetDescription());
  }

  @Override
  protected String getName(final DatasetConfiguration selection) {
    return selection.label();
  }
  
  @Override
  protected final HorizontalMenuItemPopup<DatasetConfiguration> createMenuPopup() {
    return new HorizontalMenuItemPopup<>(v -> getName(v));
  }

  @EventHandler
  public void onDataSetListChangeEvent(final DataSetListChangeEvent e) {
    if (e.getValue() == null) {
      setList(null);
    } else {
      setList(new ArrayList<>(e.getValue()));
    }
  }

  @EventHandler
  public void onDataSetChangeEvent(final DataSetChangeEvent e) {
    setSelected(e.getValue());
  }

  @Override
  public void onSelect(final DatasetConfiguration option) {
    eventBus.fireEvent(new DataSetChangeCommand(option));
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    this.eventBus = eventBus;

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }
}
