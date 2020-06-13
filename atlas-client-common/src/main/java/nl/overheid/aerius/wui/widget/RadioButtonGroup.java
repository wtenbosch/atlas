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
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class RadioButtonGroup<T> extends Composite implements HasValueChangeHandlers<T>, LeafValueEditor<T> {
  interface RadioButtonGroupUiBinder extends UiBinder<Widget, RadioButtonGroup<?>> {}

  private static final RadioButtonGroupUiBinder UI_BINDER = GWT.create(RadioButtonGroupUiBinder.class);

  public interface RadioButtonContentResource<T> {
    String getRadioButtonText(T value);
  }

  public interface CustomStyle extends CssResource {
    String left();

    String right();

    String radioGroupButton();

    String selected();
  }

  private final RadioButtonContentResource<T> resource;

  @UiField FlowPanel panel;

  @UiField CustomStyle style;

  private final String name;

  private String baseID;

  private final HashMap<T, RadioButton> buttonMap = new HashMap<T, RadioButton>();

  /**
   * Initialise the group with the given resources.
   *
   * @param resource Resource to use for naming.
   */
  public RadioButtonGroup(final RadioButtonContentResource<T> resource) {
    this.resource = resource;

    // Doesn't really matter so long as it's the same for this group.
    this.name = String.valueOf("rb" + Math.random());

    initWidget(UI_BINDER.createAndBindUi(this));
  }

  /**
   * Clear the group.
   */
  public void clear() {
    panel.clear();
    buttonMap.clear();
  }

  @Override
  protected void onEnsureDebugId(final String baseID) {
    super.onEnsureDebugId(baseID);

    for (final Entry<T, RadioButton> entry : buttonMap.entrySet()) {
      UIObject.ensureDebugId(entry.getValue().getElement(), baseID, entry.getKey().toString());
    }

    this.baseID = baseID;
  }

  /**
   * Add a radio button to this group.
   *
   * @param object Object the radio button represents.
   */
  public void addButton(final T object) {
    final RadioButton button = new RadioButton(name, resource.getRadioButtonText(object));

    if (baseID != null) {
      UIObject.ensureDebugId(button.getElement(), baseID, object.toString());
    }

    button.addStyleName(style.radioGroupButton());
    button.addValueChangeHandler(e -> {
      ValueChangeEvent.fire(this, object);
      setValue(object);
    });

    buttonMap.put(object, button);

    if (panel.getWidgetCount() == 0) {
      button.setStyleName(style.left(), true);
    }

    panel.add(button);
  }

  /**
   * Add a list of radio buttons to this group.
   *
   * @param objects Objects the buttons should represent
   */
  public void addButtons(final Collection<T> objects) {
    for (final T obj : objects) {
      addButton(obj);
    }

    panel.getWidget(panel.getWidgetCount() - 1).setStyleName(style.right(), true);
  }

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<T> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  public void setEnabled(final T value, final boolean enable) {
    final RadioButton radioButton = buttonMap.get(value);
    if (radioButton != null) {
      radioButton.setEnabled(enable);
    }
  }

  @Override
  public void setValue(final T value) {
    resetAll();
    final RadioButton radioButton = buttonMap.get(value);
    if (radioButton != null) {
      radioButton.setStyleName(style.selected(), true);
      radioButton.setValue(Boolean.TRUE, false);
    }
  }

  @Override
  public T getValue() {
    for (final Entry<T, RadioButton> entry : buttonMap.entrySet()) {
      if (entry.getValue().getValue()) {
        return entry.getKey();
      }
    }

    return null;
  }

  /**
   * When programmatically set a value make sure all other checkboxes are unset.
   */
  private void resetAll() {
    for (final Entry<T, RadioButton> entry : buttonMap.entrySet()) {
      final RadioButton value = entry.getValue();
      value.setValue(false);
      value.setStyleName(style.selected(), false);
    }
  }
}