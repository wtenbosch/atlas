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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.shared.util.HasParent;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.util.SvgUtil;

public abstract class MaskedButton<T extends Object> extends EventComposite {
  public interface CustomStyle extends CssResource {
    String selected();

    String disabled();
  }

  public @UiField FlowPanel control;
  public @UiField SimplePanel button;

  public @UiField Label label;

  public @UiField CustomStyle style;

  private boolean disabled;
  protected final T option;
  private boolean selected;

  private boolean deferred;

  public MaskedButton(final T option) {
    this.option = option;
  }

  @Override
  protected void initWidget(final Widget widget) {
    super.initWidget(widget);

    final DataResource image = getImage(option);
    if (image == null) {
      button.removeFromParent();
    } else {
      SvgUtil.I.setSvg(button, image);
    }

    final String labelText = getLabel(option);
    if (label != null) {
      label.setText(labelText);
    }

    control.addDomHandler(event -> {
      if (disabled) {
        return;
      }

      if (selected) {
        onUnselect(option);
      } else {
        onSelect(option);
      }
    }, ClickEvent.getType());

    control.addDomHandler(event -> {
      if (disabled) {
        return;
      }

      onHover(option);
    }, MouseOverEvent.getType());

    if (labelText != null) {
      ensureDebugId(AtlasTestIDs.MASKED_BUTTON + "-" + labelText);
    }
  }

  protected void onUnselect(final T option) {
    // Default to re-select
    onSelect(option);
  }

  protected void onHover(final T option) {
    // Do nothing
  }

  protected DataResource getImage(final T option) {
    return null;
  }

  protected void onSelect(final T value) {
    onSelect(value, true);
  }

  @SuppressWarnings("unchecked")
  protected void onSelect(final T value, final boolean userInitiated) {
    if (value instanceof HasParent) {
      final T parent = ((HasParent<T>) value).getParent();
      if (parent != null) {
        onSelect(parent, false);
      }
    }
  }

  public void deselect() {
    onDeselect(option);
  }

  /**
   * Deferredly deselect. A deselection results in a possible slew of Commands issued while state across whole sets of controls are being set.
   * Therefore, wait for state resets to settle before issuing the onDeselect -- if then needed.
   */
  private void deselectSpontaneous() {
    if (deferred) {
      return;
    }

    deferred = true;
    Scheduler.get().scheduleDeferred(() -> {
      deferred = false;

      if (selected) {
        onDeselect(option, true);
      }
    });
  }

  private void onDeselect(final T option) {
    onDeselect(option, false);
  }

  protected void onDeselect(final T option, final boolean spontaneous) {

  }

  protected String getLabel(final T option) {
    return null;
  }

  public void setDisabled(final boolean disabled) {
    this.disabled = disabled;

    control.setStyleName(style.disabled(), disabled);
    control.setStyleName(style.selected(), false);

    if (disabled) {
      deselectSpontaneous();
    } else {
      if (selected) {
        control.setStyleName(style.selected(), true);
      }
    }
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void setSelected(final boolean select) {
    this.selected = select;
    control.setStyleName(style.selected(), select);
  }

  public boolean isSelected() {
    return selected;
  }

  public void select() {
    onSelect(option);
  }
}
