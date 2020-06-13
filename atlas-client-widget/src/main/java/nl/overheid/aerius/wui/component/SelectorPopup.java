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

import java.util.Collection;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.wui.i18n.AtlasM;
import nl.overheid.aerius.wui.resources.R;

public class SelectorPopup extends PopupPanel {
  private static final SelectorPopupUiBinder UI_BINDER = GWT.create(SelectorPopupUiBinder.class);

  interface SelectorPopupUiBinder extends UiBinder<Widget, SelectorPopup> {}

  @UiField FlowPanel itemPanel;

  @UiField TextBox searchBox;

  private SelectorButton selected;
  private final HashMap<Selector, SelectorButton> buttons = new HashMap<>();

  private Widget parent;

  private final SelectorParent selectorParent;

  public SelectorPopup(final SelectorParent selectorParent) {
    super(true);
    this.selectorParent = selectorParent;

    setWidget(UI_BINDER.createAndBindUi(this));

    setAnimationEnabled(true);
    setAnimationType(AnimationType.ROLL_DOWN);
    setGlassEnabled(true);
    setGlassStyleName(R.css().glassPanel());
  }

  public void setParentWidget(final Widget parent) {
    this.parent = parent;
    addAutoHidePartner(parent.getParent().getElement());
  }

  @Override
  public void show() {
    Scheduler.get().scheduleDeferred(() -> searchBox.setFocus(true));

    setPopupPosition(parent.getAbsoluteLeft(), parent.getAbsoluteTop());

    getElement().getStyle().setWidth(parent.getOffsetWidth(), Unit.PX);

    Window.addResizeHandler(e -> setPopupHeight(e.getHeight() - parent.getAbsoluteTop()));
    setPopupHeight(Window.getClientHeight() - parent.getAbsoluteTop());

    super.show();

    new Timer() {
      @Override
      public void run() {
        getGlassElement().addClassName(R.css().glassPanelFull());
      }
    }.schedule(20);

    searchBox.getElement().setPropertyString("placeholder", AtlasM.messages().searchPlaceHolder());
  }

  @UiHandler("searchBox")
  public void onSearchBoxKeyUpEvent(final KeyUpEvent e) {
    if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
      acceptFirst(searchBox.getValue());
    } else {
      narrowSelectorOptions(searchBox.getValue());
    }
  }

  private void acceptFirst(final String value) {
    buttons.values().stream()
        .filter(v -> v.matches(value))
        .findFirst()
        .ifPresent(v -> v.select());
  }

  private void narrowSelectorOptions(final String value) {
    buttons.values().forEach(v -> v.acceptHidePredicate(value));
  }

  @Override
  public void hide(final boolean autoClosed) {
    new Timer() {
      @Override
      public void run() {
        SelectorPopup.super.hide(autoClosed);
      }
    }.schedule(20);

    getGlassElement().removeClassName(R.css().glassPanelFull());
  }

  public void setList(final Collection<Selector> items) {
    buttons.clear();
    itemPanel.clear();

    for (final Selector item : items) {
      final SelectorButton maskedButton = new SelectorButton(selectorParent, item);
      itemPanel.add(maskedButton);

      buttons.put(item, maskedButton);
    }
  }

  public void setSelected(final Selector selection) {
    if (!buttons.containsKey(selection)) {
      return;
    }

    if (selected != null) {
      selected.setSelected(false);
    }

    final SelectorButton newSelection = buttons.get(selection);
    newSelection.setSelected(true);
    selected = newSelection;
  }

  private void setPopupHeight(final int height) {
    getWidget().getElement().getStyle().setProperty("maxHeight", height, Unit.PX);
  }

}
