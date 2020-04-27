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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.PopupPanel.AnimationType;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.shared.domain.SearchSuggestion;
import nl.overheid.aerius.wui.event.BasicEventComponent;
import nl.overheid.aerius.wui.util.GWTAtomicInteger;
import nl.overheid.aerius.wui.widget.AutoSizePopupPanel;
import nl.overheid.aerius.wui.widget.HasEventBus;

public class MapSearchPopup extends BasicEventComponent implements Consumer<SearchSuggestion>, HasEventBus {
  // Stop-gap way of animating horizontally. Ugly but it works fine.
  private static final int ANIMATION_DELAY = 5;
  private static final int ANIMATION_STEPS = 50;

  private final Widget parent;

  private final AutoSizePopupPanel popup;
  private final MapSearchPopupContent content;

  public MapSearchPopup(final Widget parent) {
    this.parent = parent;

    content = new MapSearchPopupContent();

    popup = new AutoSizePopupPanel(true);
    popup.setAnimationType(AnimationType.ROLL_DOWN);
    popup.setAnimationEnabled(false);
    popup.addAutoHidePartner(parent.getElement());
    popup.setWidget(content);
    popup.addCloseHandler(event -> trackParent());
  }

  public void addCloseHandler(final Runnable runner) {
    popup.addCloseHandler(e -> runner.run());
  }

  private void trackParent() {
    // Another stop-gap way of animating.
    final GWTAtomicInteger at = new GWTAtomicInteger();
    Scheduler.get().scheduleFixedDelay(() -> {
      popup.setPopupPosition(parent.getAbsoluteLeft(), parent.getAbsoluteTop() + parent.getOffsetHeight());

      return at.incrementAndGet() < ANIMATION_STEPS;
    }, ANIMATION_DELAY);
  }

  public void resize() {
    if (popup.isShowing()) {
      popup.showRelativeTo(parent);
    }
  }

  public boolean isShowing() {
    return popup.isShowing();
  }

  public void notifyChange() {
    content.notifyChange();
  }

  public void show() {
    trackParent();
    popup.showRelativeTo(parent);
  }

  public void hide() {
    popup.hide();
  }

  public void clearSoftly() {

  }

  @Override
  public void accept(final SearchSuggestion t) {
    content.accept(t);
    show();
  }

  public void confirm() {
    content.confirm();
  }

  public void moveNext() {
    // Stub
  }

  public void movePrevious() {
    // Stub
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, content);
  }
}
