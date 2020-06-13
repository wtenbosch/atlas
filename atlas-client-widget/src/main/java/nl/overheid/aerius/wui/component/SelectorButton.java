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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.wui.widget.AnimatedFlowPanel;

public class SelectorButton extends Composite {
  private static final SelectorButtonUiBinder UI_BINDER = GWT.create(SelectorButtonUiBinder.class);

  interface SelectorButtonUiBinder extends UiBinder<Widget, SelectorButton> {}

  public interface CustomStyle extends CssResource {
    String selected();
  }

  @UiField AnimatedFlowPanel control;
  @UiField Label label;

  @UiField CustomStyle style;

  private final Selector item;
  private final SelectorParent parent;

  public SelectorButton(final SelectorParent parent, final Selector item) {
    this.parent = parent;
    this.item = item;
    initWidget(UI_BINDER.createAndBindUi(this));

    label.setText(item.getTitle().orElse(""));

    control.addDomHandler(event -> {
      select();
    }, ClickEvent.getType());
  }

  public void setSelected(final boolean select) {
    control.setStyleName(style.selected(), select);
  }

  public boolean matches(final String value) {
    if (value.isEmpty()) {
      return true;
    }

    final String matchCoerced = value.toLowerCase();

    final String titleCoerced = item.getTitle().orElse("").toLowerCase();
    final String valueCoerced = item.getValue().orElse("").toLowerCase();

    return titleCoerced.contains(matchCoerced) || valueCoerced.contains(matchCoerced);
  }

  public void acceptHidePredicate(final String value) {
    control.setVisible(matches(value));
  }

  public void select() {
    parent.onSelect(item);
  }
}
