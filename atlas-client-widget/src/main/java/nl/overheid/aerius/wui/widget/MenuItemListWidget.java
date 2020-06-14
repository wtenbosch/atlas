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

import java.util.Collection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.wui.resources.AtlasR;

public abstract class MenuItemListWidget<P extends AbstractMenuItemPopup<T>, T> extends EventComposite implements HasEventBus {
  public interface CustomStyle extends CssResource {
    String focus();
  }

  public @UiField FocusPanel panel;

  @UiField CustomStyle style;

  protected Collection<T> items;

  protected P popup;

  public MenuItemListWidget() {}

  public MenuItemListWidget(final P popup) {
    setPopup(popup);
  }

  public void setPopup(final P popup) {
    this.popup = popup;
    popup.addCloseHandler(p -> hide());
  }

  @Override
  protected void initWidget(final Widget widget) {
    super.initWidget(widget);
    
    if (popup != null) {
      popup.setParent(this);
    }

    setVisible(false);
  }

  public void setList(final Collection<T> items) {
    this.items = items;

    popup.setList(items);

    setVisible(items != null && !items.isEmpty());

    // if (items != null && !items.isEmpty()) {
    // setSelected(items.iterator().next());
    // }
    setEmpty();
  }

  protected void setEmpty() {}

  @UiHandler("panel")
  void clickPanel(final ClickEvent e) {
    toggleMenu();
  }

  @UiHandler("panel")
  void onWheelPanel(final MouseWheelEvent e) {
    if (e.getDeltaY() < 0) {
      popup.selectPrevious();
    } else if (e.getDeltaY() > 0) {
      popup.selectNext();
    }
  }

  public void setSelected(final T selection) {
    if (items == null) {
      return;
    }

    if (selection == null) {
      setEmpty();
    }

    popup.setSelected(selection);

    if (popup.isShowing()) {
      new Timer() {
        @Override
        public void run() {
          popup.hide();
        }
      }.schedule(100);
    }
  }

  protected String getName(final T selection) {
    return String.valueOf(selection);
  }

  public abstract void onSelect(final T option);

  private void toggleMenu() {
    if (popup.isShowing()) {
      popup.hide();
    } else {
      show();
    }
  }

  private void hide() {
    popup.prepareHide();
    getElement().getStyle().clearZIndex();
    panel.removeStyleName(AtlasR.css().focus());
    panel.removeStyleName(style.focus());
  }

  private void show() {
    popup.show();
    getElement().getStyle().setZIndex(15000);
    panel.addStyleName(AtlasR.css().focus());
    panel.addStyleName(style.focus());
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, popup);
  }
}
