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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.wui.resources.AtlasR;
import nl.overheid.aerius.wui.util.MathUtil;

public abstract class AbstractMenuItemPopup<T> extends PopupPanel implements HasEventBus {
  protected MenuItemListWidget<?, T> parent;
  protected EventBus eventBus;

  @UiField FlowPanel itemPanel;

  private MaskedButton<T> selected;
  private final HashMap<Object, MaskedButton<T>> buttons = new LinkedHashMap<>();

  public AbstractMenuItemPopup() {
    super(true);

    config();
  }

  public Object getOptionKey(final T obj) {
    return obj;
  }

  protected void config() {
    setGlassStyleName(AtlasR.css().glassPanel());
    setGlassEnabled(true);
  }

  public void setParent(final MenuItemListWidget<?, T> parent) {
    this.parent = parent;
    addAutoHidePartner(parent.getElement());
  }

  @Override
  public void show() {
    super.show();

    Scheduler.get().scheduleDeferred(() -> {
      getGlassElement().addClassName(AtlasR.css().glassPanelFull());
    });
  }

  public void prepareHide() {
    getGlassElement().removeClassName(AtlasR.css().glassPanelFull());
  }

  @Override
  public void hide() {
    super.hide();
  }

  /**
   * Simple delegate method to {@link MenuItemListWidget}'s onSelect(T)
   *
   * @param selection Selection that is to be selected.
   */
  protected void onSelect(final T selection) {
    parent.onSelect(selection);
  }

  public void setList(final Collection<T> items) {
    buttons.clear();
    itemPanel.clear();

    if (items == null) {
      return;
    }

    for (final T item : items) {
      final MaskedButton<T> maskedButton = createMaskedButton(item);
      itemPanel.add(maskedButton);

      final Object optionKey = getOptionKey(item);
      buttons.put(optionKey, maskedButton);
    }
  }

  public void setSelected(final T selection) {
    if (selected != null) {
      selected.setSelected(false);
    }

    if (!buttons.containsKey(getOptionKey(selection))) {
      return;
    }

    final MaskedButton<T> newSelection = buttons.get(getOptionKey(selection));
    newSelection.setSelected(true);
    selected = newSelection;
  }

  public void selectPrevious() {
    if (buttons.size() <= 1) {
      return;
    }

    selectIndex(getSelectedIndex() - 1);
  }

  public void selectNext() {
    if (buttons.size() <= 1) {
      return;
    }

    selectIndex(getSelectedIndex() + 1);
  }

  private void selectIndex(final int i) {
    getItem(MathUtil.positiveMod(i, buttons.size())).select();
  }

  private int getSelectedIndex() {
    int counter = 0;
    for (final MaskedButton<T> item : buttons.values()) {
      if (item.equals(selected)) {
        break;
      }
      counter++;
    }

    return counter;
  }

  private MaskedButton<T> getItem(final int idx) {
    return new ArrayList<>(buttons.values()).get(idx);
  }

  protected abstract MaskedButton<T> createMaskedButton(T item);

  @Override
  public void setEventBus(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public void setTitleText(final String name) {
    // No-op
  }

  public void setDescription(final String description) {
    // No-op
  }
}
