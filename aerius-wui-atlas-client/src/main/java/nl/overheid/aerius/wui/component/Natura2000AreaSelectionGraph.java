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
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.NatureArea;
import nl.overheid.aerius.wui.atlas.event.AreaGroupListChangedEvent;
import nl.overheid.aerius.wui.widget.EventComposite;

public class Natura2000AreaSelectionGraph extends EventComposite {
  interface Natura2000AreaSelectionGraphEventBinder extends EventBinder<Natura2000AreaSelectionGraph> {}

  private final Natura2000AreaSelectionGraphEventBinder EVENT_BINDER = GWT.create(Natura2000AreaSelectionGraphEventBinder.class);

  private static final Natura2000AreaSelectionGraphUiBinder UI_BINDER = GWT.create(Natura2000AreaSelectionGraphUiBinder.class);

  interface Natura2000AreaSelectionGraphUiBinder extends UiBinder<Widget, Natura2000AreaSelectionGraph> {}

  @UiField Natura2000SVG svgTarget;

  public Natura2000AreaSelectionGraph() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @EventHandler
  public void onAreaGroupListChangedEvent(final AreaGroupListChangedEvent e) {
    svgTarget.setNatureAreas(e.getValue().values().stream()
        .flatMap(v -> v.stream())
        .distinct()
        .collect(Collectors.toList()));
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }

  public void clearHighlight(final NatureArea a) {
    svgTarget.clearHighlight(a);
  }

  public void highlight(final NatureArea a) {
    svgTarget.highlight(a);
  }

  public void onNaturaSelect(final Consumer<NatureArea> consumer) {
    svgTarget.onAreaSelect(consumer);
  }
}
