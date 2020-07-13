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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.shared.domain.properties.MainComponentProperties;
import nl.overheid.aerius.wui.domain.selector.SimpleSelectorContext;
import nl.overheid.aerius.wui.resources.AtlasR;
import nl.overheid.aerius.wui.util.ObservingReplacementAssistant;
import nl.overheid.aerius.wui.util.ReplacementRegistration;
import nl.overheid.aerius.wui.util.WebComponentUtil;
import nl.overheid.aerius.wui.widget.HasEventBus;

public class ChapterWidgetComponentDelegate implements PanelWidgetDelegate, HasEventBus {
  interface ChapterWidgetComponentDelegateEventBinder extends EventBinder<ChapterWidgetComponentDelegate> {}

  private static final int REMOVE_DELAY = 250;

  private final ChapterWidgetComponentDelegateEventBinder EVENT_BINDER = GWT.create(ChapterWidgetComponentDelegateEventBinder.class);

  private final Map<String, String> defaultSelectors = new HashMap<>();
  private final Map<String, String> selectors = new HashMap<>();
  private Element component;
  private final List<ReplacementRegistration> registrations = new ArrayList<>();

  private final AcceptsOneWidget target;
  private EventBus eventBus;
  protected final MainComponentProperties properties;
  private final ObservingReplacementAssistant replacer;

  private final Map<String, Timer> removeTimers = new HashMap<>();

  private final SimpleSelectorContext selectorContext;

  @Inject
  public ChapterWidgetComponentDelegate(@Assisted final PanelContent conf, final @Assisted AcceptsOneWidget target,
      final ObservingReplacementAssistant replacer, final SimpleSelectorContext selectorContext) {
    this.replacer = replacer;
    this.selectorContext = selectorContext;

    this.properties = conf.asMainComponentProperties();
    this.target = target;
  }

  @Override
  public void show() {
    if (component != null) {
      target.setWidget(null);
      component = null;
      registrations.forEach(v -> v.unregister());
      registrations.clear();
    }

    WebComponentUtil.inject(properties, target, eventBus, c -> initComponentParameters(c));
  }

  @Override
  public void clear() {
    registrations.forEach(v -> v.unregister());
    registrations.clear();
  }

  public void initComponentParameters(final Element component) {
    this.component = component;

    initComponentStyle(component);
    initDefaultSelectors(component);

    updateSelectorAttributes();
    updateProperties();
  }

  private void initDefaultSelectors(final Element component) {
    defaultSelectors.clear();
    defaultSelectors.putAll(selectorContext
        .getDefaultSelectors().values().stream()
        .filter(v -> v.getValue().isPresent())
        .collect(Collectors.toMap(v -> v.getType(), v -> v.getValue().get())));
  }

  protected void updateProperties() {
    properties.getParameters().entrySet().forEach(v -> {
      registrations.add(replacer.registerStrict(v.getValue(), s -> {
        if (s != null) {
          setFinalAttribute(v.getKey(), s);
        } else {
          if (component != null) {
            overridableRemove(v.getKey());
          }
        }
      }));
    });
  }

  protected void initComponentStyle(final Element component) {
    component.addClassName(AtlasR.css().marginAround());
  }

  @Override
  public void notifySelector(final Selector selector) {
    if (selectors.containsKey(selector.getType())
        && selectors.get(selector.getType()).equals(selector.getValue().orElse(null))) {
      return;
    }

    if (selector.getValue().isPresent()) {
      selectors.put(selector.getType(), selector.getValue().get());
      setFinalAttribute(selector.getType(), selector.getValue().get());
    } else {
      selectors.remove(selector.getType());
      if (component != null) {
        overridableRemove(selector.getType());
      }
    }

    updateSelectorAttributes();
  }

  private void overridableRemove(final String key) {
    if (removeTimers.containsKey(key)) {
      cancelRemoveTimer(key);
    }

    final Timer timer = new Timer() {
      @Override
      public void run() {
        component.removeAttribute(key);
      }
    };
    removeTimers.put(key, timer);
    timer.schedule(REMOVE_DELAY);
  }

  private void cancelRemoveTimer(final String key) {
    removeTimers.computeIfPresent(key, (k, v) -> {
      v.cancel();
      return null;
    });
  }

  protected void setFinalAttribute(final String key, final String value) {
    // Note: component could actually be null here.
    // Another note: don't save selector values here
    if (component == null) {
      return;
    }

    cancelRemoveTimer(key);

    final String currentValue = component.getAttribute(key);
    if (currentValue == null || !currentValue.equals(value)) {
      component.setAttribute(key, value);
    }
  }

  private void updateSelectorAttributes() {
    defaultSelectors.entrySet().forEach(v -> setFinalAttribute(v.getKey(), v.getValue()));
    selectors.entrySet().forEach(v -> setFinalAttribute(v.getKey(), v.getValue()));
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    this.eventBus = eventBus;
    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }
}
