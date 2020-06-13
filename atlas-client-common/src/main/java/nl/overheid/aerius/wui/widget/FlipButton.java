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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class FlipButton extends Composite implements ClickHandler {
  public static final double NORTH = -90D;
  public static final double SOUTH = 90D;
  public static final double WEST = 180D;
  public static final double EAST = 0D;

  private static final FlipButtonUiBinder UI_BINDER = GWT.create(FlipButtonUiBinder.class);

  interface FlipButtonUiBinder extends UiBinder<Widget, FlipButton> {}

  private boolean closed = true;
  private Consumer<Boolean> callback;

  private double closedRotation = NORTH;
  private double openRotation = SOUTH;

  public FlipButton() {
    initWidget(UI_BINDER.createAndBindUi(this));

    addDomHandler(this, ClickEvent.getType());
  }

  public FlipButton(final Consumer<Boolean> callback) {
    this();

    this.callback = callback;
  }

  public void init() {
    init(true);
  }

  public void init(final boolean initial) {
    setState(initial);
  }

  public void setState(final boolean close) {
    if (close) {
      orientClosed();
    } else {
      orientOpen();
    }

    closed = close;
    if (callback != null) {
      callback.accept(!close);
    }
  }

  private void orientClosed() {
    setRotation(closedRotation);
  }

  private void orientOpen() {
    setRotation(openRotation);
  }

  private void setRotation(final double rotation) {
    getElement().getStyle().setProperty("transform", "rotate(" + rotation + "deg)");
  }

  @Override
  public void onClick(final ClickEvent event) {
    setState(!closed);
  }

  public void setClosedRotation(final double closedRotation) {
    this.closedRotation = closedRotation;
  }

  public void setOpenRotation(final double openRotation) {
    this.openRotation = openRotation;
  }

  public boolean isClosed() {
    return closed;
  }

  public void toggle() {
    setState(!closed);
  }
}
