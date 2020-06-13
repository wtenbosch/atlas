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

import java.util.List;
import java.util.Optional;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.shared.domain.SelectorConfiguration;
import nl.overheid.aerius.wui.atlas.command.SelectorCommand;
import nl.overheid.aerius.wui.atlas.event.SelectorConfigurationChangeEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorConfigurationClearEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorLoadFailureEvent;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.atlas.util.HoverSelectionUtil;
import nl.overheid.aerius.wui.event.BundledRegistration;
import nl.overheid.aerius.wui.resources.R;
import nl.overheid.aerius.wui.util.MathUtil;
import nl.overheid.aerius.wui.util.SelectorUtil;
import nl.overheid.aerius.wui.widget.EventComposite;
import nl.overheid.aerius.wui.widget.SwitchPanel;

public class SelectorWidget extends EventComposite implements SelectorParent {
  private static final int POPUP_ZINDEX = 12501;

  interface SelectorWidgetEventBinder extends EventBinder<SelectorWidget> {}

  private final SelectorWidgetEventBinder EVENT_BINDER = GWT.create(SelectorWidgetEventBinder.class);

  private static final SelectorWidgetUiBinder UI_BINDER = GWT.create(SelectorWidgetUiBinder.class);

  interface SelectorWidgetUiBinder extends UiBinder<Widget, SelectorWidget> {}

  public static interface CustomStyle extends CssResource {
    String disabled();
  }

  @UiField SimplePanel titleContainer;
  @UiField FlowPanel container;

  @UiField Label numText;

  @UiField FocusPanel rightButton;
  @UiField FocusPanel leftButton;
  @UiField FocusPanel textContainer;
  @UiField Label selectorText;

  @UiField SwitchPanel contentSwitchPanel;

  @UiField CustomStyle style;

  private final SelectorPopup popup;

  private String type;

  private Selector selectedSelector;

  private List<Selector> items;

  private boolean enabled;

  private final BundledRegistration register = new BundledRegistration();

  private String title;

  public SelectorWidget(final String type) {
    this.type = type;

    initWidget(UI_BINDER.createAndBindUi(this));

    popup = new SelectorPopup(this);
    popup.addCloseHandler(p -> hide());
    popup.setParentWidget(selectorText.getParent());

    register.add(HoverSelectionUtil.registerTop(titleContainer, () -> title));

    rightButton.ensureDebugId(AtlasTestIDs.BUTTON_SELECTOR_RIGHT);
    leftButton.ensureDebugId(AtlasTestIDs.BUTTON_SELECTOR_LEFT);
  }

  @Override
  protected void onUnload() {
//    register.retire();
  }

  @UiHandler("rightButton")
  public void onNextClick(final ClickEvent e) {
    if (items == null || items.isEmpty()) {
      return;
    }

    onSelect(items.get(MathUtil.positiveMod(items.indexOf(selectedSelector) + 1, items.size())));
  }

  @UiHandler("leftButton")
  public void onPreviousClick(final ClickEvent e) {
    if (items == null || items.isEmpty()) {
      return;
    }

    onSelect(items.get(MathUtil.positiveMod(items.indexOf(selectedSelector) - 1, items.size())));
  }

  @UiHandler("textContainer")
  public void onTextContainerClick(final ClickEvent e) {
    togglePopup();
  }

  private void togglePopup() {
    if (popup.isShowing()) {
      popup.hide();
    } else {
      show();
    }
  }

  private void show() {
    if (!enabled) {
      return;
    }

    popup.show();
    textContainer.addStyleName(R.css().focus());
    getElement().getStyle().setZIndex(POPUP_ZINDEX);
    selectorText.getElement().getStyle().setZIndex(POPUP_ZINDEX);
    textContainer.getElement().getStyle().setZIndex(POPUP_ZINDEX);
    rightButton.getElement().getStyle().setZIndex(POPUP_ZINDEX);
    leftButton.getElement().getStyle().setZIndex(POPUP_ZINDEX);
  }

  private void hide() {
    textContainer.removeStyleName(R.css().focus());
    getElement().getStyle().clearZIndex();
    selectorText.getElement().getStyle().clearZIndex();
    textContainer.getElement().getStyle().clearZIndex();
    rightButton.getElement().getStyle().clearZIndex();
    leftButton.getElement().getStyle().clearZIndex();
  }

  public void setType(final String type) {
    this.type = type;
  }

  @EventHandler
  public void onSelectorListChanged(final SelectorConfigurationChangeEvent e) {
    setList(e.getValue());
  }

  @EventHandler
  public void onSelectorLoadFailureEvent(final SelectorLoadFailureEvent e) {
    if (!SelectorUtil.matchesStrict(this.type, e)) {
      return;
    }

    fail();
  }

  @EventHandler
  public void onSelectorConfigurationClearEvent(final SelectorConfigurationClearEvent e) {
    contentSwitchPanel.showWidget(1);
    numText.setText("-");
  }

  public void setList(final SelectorConfiguration config) {
    if (!SelectorUtil.matchesStrict(this.type, config)) {
      return;
    }

    numText.setText(String.valueOf(config.getOptions().size()));
    titleContainer.setTitle(config.getDescription());

    title = config.getTitle();
    items = config.getOptions();

    contentSwitchPanel.showWidget(1);

    popup.setList(items);
  }

  @EventHandler
  public void onSelectorEvent(final SelectorEvent e) {
    if (!SelectorUtil.matchesStrict(type, e)) {
      return;
    }

    setSelector(e.getSelector());

    new Timer() {
      @Override
      public void run() {
        popup.hide();
      }
    }.schedule(100);
  }

  public void setSelector(final Selector selector) {
    final Optional<String> title = selector.getTitle();

    if (title.isPresent()) {
      selectorText.setText(title.get());
      contentSwitchPanel.showWidget(0);

      // Visible only when there's more than 1 item.
      if (items != null) {
        if (items.size() <= 1) {
          leftButton.getElement().getStyle().setVisibility(Visibility.HIDDEN);
          rightButton.getElement().getStyle().setVisibility(Visibility.HIDDEN);
          setEnabled(false);
        } else {
          leftButton.getElement().getStyle().setVisibility(Visibility.VISIBLE);
          rightButton.getElement().getStyle().setVisibility(Visibility.VISIBLE);
          setEnabled(true);
        }
      }

    } else {
      contentSwitchPanel.showWidget(2);
      setEnabled(false);
    }

    popup.setSelected(selector);
    selectedSelector = selector;
  }

  private void fail() {
    contentSwitchPanel.showWidget(3);
    setEnabled(false);
  }

  private void setEnabled(final boolean enabled) {
    this.enabled = enabled;
    container.setStyleName(style.disabled(), !enabled);
  }

  @Override
  public void onSelect(final Selector selector) {
    eventBus.fireEvent(new SelectorCommand(selector));
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }
}
