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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.ui.client.adapters.HasTextEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AnimatedLabel extends Composite implements HasText, IsEditor<LeafValueEditor<String>>, HasClickHandlers {
  interface AnimatedLabelUiBinder extends UiBinder<Widget, AnimatedLabel> {}

  private static final AnimatedLabelUiBinder UI_BINDER = GWT.create(AnimatedLabelUiBinder.class);

  private static final int CLEAN_UP_DELAY = 150;

  public interface CustomStyle extends CssResource {
    String noAnimation();

    String animatedText();
  }

  @UiField CustomStyle style;

  @UiField FlowPanel container;

  private Label currentText;

  private String value;

  private LeafValueEditor<String> editor;

  private final Timer cleanupTimer = new Timer() {
    @Override
    public void run() {
      while (container.getWidgetCount() > 1) {
        container.remove(0);
      }
    }
  };

  public AnimatedLabel() {
    this(null);
  }

  public AnimatedLabel(final String text) {
    initWidget(UI_BINDER.createAndBindUi(this));

    if (text != null) {
      setText(text);
    }
  }

  @Override
  public LeafValueEditor<String> asEditor() {
    if (editor == null) {
      editor = HasTextEditor.of(this);
    }

    return editor;
  }

  @Override
  public String getText() {
    return value;
  }

  @Override
  public void setText(final String value) {
    setText(value, true);
  }

  public void setText(final String value, final boolean up) {
    // Bug out if the text to display is the same as the current text
    if (this.value != null && this.value.equals(value)) {
      return;
    }

    final int offsetHeight = container.getOffsetHeight();

    // Animate the current text away, if there
    if (currentText != null) {
      animate(!up, currentText, offsetHeight, true);
    }

    final Label lbl = new Label(value);
    animate(up, lbl, offsetHeight, false);

    currentText = lbl;
    cleanupTimer.schedule(CLEAN_UP_DELAY);
  }

  private void animate(final boolean up, final Widget widg, final int height, final boolean away) {
    animate(widg, up ? height : -height, away);
  }

  private void animate(final Widget widg, final int distance, final boolean initial) {
    if (container.getWidgetIndex(widg) == -1) {
      widg.addStyleName(style.animatedText());
      container.add(widg);
    }

    widg.getElement().getStyle().setTop(initial ? 0 : distance, Unit.PX);

    // ComputedStyleUtil.forceStyleRender(widg);

    widg.getElement().getStyle().setTop(initial ? distance : 0, Unit.PX);
  }

  @Override
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }
}