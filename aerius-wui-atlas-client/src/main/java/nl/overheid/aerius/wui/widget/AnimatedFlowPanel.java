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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;

public class AnimatedFlowPanel extends FlowPanel {
  private static final int RECONCILIATION_DELAY = 205;

  private boolean globalAnimate = true;

  public AnimatedFlowPanel() {
    getElement().getStyle().setProperty("transition", "height 0.2s ease-out");
  }

  @Override
  public void setVisible(final boolean visible) {
    setVisible(visible, true);
  }

  private void animate(final boolean visible) {
    if (visible) {
      super.setVisible(visible);
    }

    getElement().getStyle().setOverflow(Overflow.HIDDEN);

    final int endHeight = visible ? getOffsetHeight() : 0;
    final int startHeight = visible ? 0 : getOffsetHeight();

    getElement().getStyle().setHeight(startHeight, Unit.PX);
    Scheduler.get().scheduleDeferred(() -> getElement().getStyle().setHeight(endHeight, Unit.PX));
    Scheduler.get().scheduleFixedDelay(() -> postVisible(visible), RECONCILIATION_DELAY);
  }

  private boolean postVisible(final boolean visible) {
    super.setVisible(visible);
    getElement().getStyle().clearHeight();
    getElement().getStyle().setOverflow(Overflow.VISIBLE);
    return false;
  }

  public void setVisible(final boolean visible, final boolean animate) {
    if (visible == isVisible()) {
      return;
    }

    if (!isAttached()) {
      super.setVisible(visible);
      return;
    }

    if (!animate || !globalAnimate) {
      super.setVisible(visible);
      return;
    }

    animate(visible);
  }

  public void setAnimate(final boolean animate) {
    this.globalAnimate = animate;
  }
}
