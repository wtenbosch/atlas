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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

import nl.overheid.aerius.shared.domain.HasIndependence;
import nl.overheid.aerius.shared.util.HasParent;

public abstract class MaskedButtonPanel<T, C extends MaskedButton<T>> extends FlowPanel {
  protected final Map<Object, C> controls = new HashMap<>();
  protected final Map<Object, T> options = new HashMap<>();

  private Set<Object> currentSelections = new HashSet<>();

  protected Object getControlKey(final T option) {
    return option;
  }

  protected void setEnabledOptions(final List<T> value) {
    controls.forEach((k, v) -> v.setDisabled(!value.contains(k)));
  }

  public void setSelectedOption(final T option, final boolean select) {
    if (!controls.keySet().contains(option)) {
      GWT.log("[WARN] Option unavailable. " + option);
      return;
    }

    if (select) {
      selectOption(option, true);
    } else {
      deselect(getControl(option));
      currentSelections.remove(getControlKey(option));
    }
  }

  private C getControl(final T option) {
    return controls.get(getControlKey(option));
  }

  protected void selectOption(final T option) {
    selectOption(option, false);
  }

  @SuppressWarnings("unchecked")
  protected void selectOption(final T value, final boolean soft) {
    if (!soft) {
      final T parent = value instanceof HasParent ? ((HasParent<T>) value).getParent() : null;

      // If this object's parent exists, select it
      if (parent != null) {
        innerSelect(parent);
      }

      // For every option which contains the given value, deselect its widget
      currentSelections.stream()
          .filter(v -> !v.equals(getControlKey(value)) && !v.equals(getControlKey(parent)))
          .forEach(v -> deselectFromControlKey(v));
    }

    innerSelect(value);
  }

  private void innerSelect(final T value) {
    if (controls.containsKey(getControlKey(value))) {
      final C control = getControl(value);
      control.setSelected(true);

      currentSelections.add(getControlKey(value));
    } else {
      reset();
    }
  }

  private boolean isIndependent(final Object v) {
    final T value = options.get(v);
    return value instanceof HasIndependence
        ? ((HasIndependence) value).isIndependent()
        : false;
  }

  protected void deselect(final T key) {
    deselectFromControlKey(getControlKey(key));
  }

  private void deselectFromControlKey(final Object key) {
    final C c = controls.get(key);
    c.setSelected(false);
  }

  protected void deselect(final C control) {
    control.setSelected(false);
  }

  protected void setControls(final Collection<T> value) {
    clear();

    addControls(value);
  }

  protected void addControls(final Collection<T> value) {
    if (value == null) {
      currentSelections = null;
      return;
    }

    for (final T option : value) {
      addControl(option);
    }
  }

  protected void addControl(final T value) {
    final C control = createControl(value);

    add(control);
    controls.put(getControlKey(value), control);
    options.put(getControlKey(value), value);

    if (currentSelections.contains(value)) {
      control.setSelected(true);
    }
  }

  protected void reset() {
    currentSelections.clear();
    final Object defaultSelection = findDefaultSelection();

    if (controls.containsKey(defaultSelection)) {
      selectOption(options.get(defaultSelection));
    }
  }

  protected Object findDefaultSelection() {
    return controls.entrySet().stream()
        .filter(v -> !v.getValue().isDisabled())
        .map(v -> v.getKey()).findFirst()
        .orElse(null);
  }

  protected abstract C createControl(final T option);

  @Override
  public void clear() {
    super.clear();

    currentSelections.clear();
    controls.clear();
  }
}
