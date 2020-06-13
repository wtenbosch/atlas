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
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.shared.domain.SelectorConfiguration;
import nl.overheid.aerius.wui.atlas.event.SelectorConfigurationChangeEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorConfigurationClearEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorEvent;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.util.SelectorUtil;
import nl.overheid.aerius.wui.widget.SelectorMenuItemPopup;
import nl.overheid.aerius.wui.widget.VerticalMenuItemListWidget;
import nl.overheid.aerius.wui.widget.VerticalMenuItemPopup;

public class YearListWidget extends VerticalMenuItemListWidget<Selector> {
  private static final String TYPE = UglyBoilerPlate.DEFAULT_TYPE_YEAR;

  private final YearListWidgetEventBinder EVENT_BINDER = GWT.create(YearListWidgetEventBinder.class);

  interface YearListWidgetEventBinder extends EventBinder<YearListWidget> {}

  private EventBus eventBus;

  private SelectorConfiguration configuration;

  @EventHandler
  public void onSelectionConfigurationClearEvent(final SelectorConfigurationClearEvent e) {
    init(null, null);
    setList(null);
    configuration = null;
    ensureDebugId(AtlasTestIDs.BUTTON_YEAR);
  }

  @EventHandler
  public void onYearListChangeEvent(final SelectorConfigurationChangeEvent e) {
    if (!SelectorUtil.matchesStrict(TYPE, e)) {
      return;
    }

    if (configuration != null && configuration.equals(e.getValue())) {
      return;
    }

    configuration = e.getValue();
    init(configuration.getTitle(), configuration.getDescription());
    setList(configuration.getOptions());
  }

  @EventHandler
  public void onYearChangeEvent(final SelectorEvent e) {
    if (!SelectorUtil.matchesStrict(TYPE, e)) {
      return;
    }

    setSelected(e.getValue());
  }

  @Override
  protected VerticalMenuItemPopup<Selector> createMenuPopup() {
    return new SelectorMenuItemPopup<>();
  }

  @Override
  protected String getName(final Selector selection) {
    return selection.getTitle().orElse("");
  }

  @Override
  public void onSelect(final Selector option) {
    eventBus.fireEvent(new SelectorEvent(option));
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    this.eventBus = eventBus;

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }
}
