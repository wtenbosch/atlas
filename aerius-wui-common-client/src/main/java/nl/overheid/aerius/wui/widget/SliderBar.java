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
package nl.overheid.aerius.wui.widget;

import java.util.function.Consumer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SliderBar extends Composite {
  /**
   * This Event type is not listed in GWT's {@link Event} constant list, it is the event that is fired on Input elements for each change the user
   * makes to it. Contrasting the OnChange Event, which only fires when a change is committed (final, typically on release).
   */
  private static final String INPUT_EVENT_TYPE = "input";

  private static final SliderBarUiBinder UI_BINDER = GWT.create(SliderBarUiBinder.class);

  interface SliderBarUiBinder extends UiBinder<Widget, SliderBar> {}

  @UiField InputElement input;

  private boolean dragging;

  public SliderBar(final Consumer<Double> callback) {
    initWidget(UI_BINDER.createAndBindUi(this));

    Event.sinkEvents(input, Event.ONCHANGE);
    Event.sinkEvents(input, Event.getTypeInt(INPUT_EVENT_TYPE));
    Event.setEventListener(input, e -> {
      if (e.getTypeInt() == Event.ONMOUSEDOWN) {
        dragging = true;
      }

      if (e.getTypeInt() == Event.ONMOUSEUP) {
        dragging = false;
      }

      if (dragging && e.getTypeInt() == Event.ONMOUSEMOVE || e.getTypeInt() == Event.ONCLICK) {
        callback.accept(Double.parseDouble(input.getValue()) / 100);
        return;
      }
    });
  }

  public void setValue(final double opacity) {
    input.setValue(String.valueOf(opacity * 100));
  }
}
