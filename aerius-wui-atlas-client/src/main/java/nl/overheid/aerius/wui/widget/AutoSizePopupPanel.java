/*
 * Copyright Dutch Ministry of Economic Affairs
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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RequiresResize;

public class AutoSizePopupPanel extends PopupPanel implements RequiresResize, ResizeHandler {
  private static final int SPACING = 20;

  private final ScheduledCommand layoutCmd = () -> {
    layoutScheduled = false;
    forceLayout();
  };

  private boolean layoutScheduled;

  private HandlerRegistration resizeHandle;

  public AutoSizePopupPanel() {
    super();
  }

  public AutoSizePopupPanel(final boolean autoHide) {
    super(autoHide);
  }

  @Override
  protected void onLoad() {
    if (resizeHandle == null) {
      resizeHandle = Window.addResizeHandler(this);
    }

    onResize();
  }

  @Override
  public void onResize(final ResizeEvent event) {
    onResize();
  }

  @Override
  public void setPopupPosition(final int left, final int top) {
    super.setPopupPosition(left, top);

    if (isAttached()) {
      onResize();
    }
  }

  @Override
  public void onResize() {
    scheduledLayout();
  }

  private void scheduledLayout() {
    if (layoutScheduled) {
      return;
    }

    layoutScheduled = true;
    Scheduler.get().scheduleDeferred(layoutCmd);
  }

  private void forceLayout() {
    final double newMaxHeight = Math.max(0, Window.getClientHeight() - getElement().getAbsoluteTop() - SPACING);
    getWidget().getElement().getStyle().setProperty("maxHeight", newMaxHeight, Unit.PX);

    final double newMaxWidth = Math.max(0, Window.getClientWidth() - getElement().getAbsoluteLeft() - SPACING);
    getWidget().getElement().getStyle().setProperty("maxWidth", newMaxWidth, Unit.PX);
  }
}