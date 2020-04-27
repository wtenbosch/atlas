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
package nl.overheid.aerius.wui.domain.selector;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.shared.domain.SelectorConfiguration;
import nl.overheid.aerius.wui.atlas.daemon.config.ConfigurationDaemon;
import nl.overheid.aerius.wui.atlas.daemon.config.Configurations;
import nl.overheid.aerius.wui.atlas.event.SelectorConfigurationChangeEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorConfigurationClearEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorConfigurationReloadEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorEvent;
import nl.overheid.aerius.wui.atlas.util.TimeUtil;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.util.SelectorUtil;
import nl.overheid.aerius.wui.widget.HasEventBus;

/**
 * TODO Turn into interface
 */
@Singleton
public class SelectorContext implements HasEventBus {
  private static final String MONITOR_SELECTORS_START_KEY = "monitor-selectors_";

  interface SelectorContextEventBinder extends EventBinder<SelectorContext> {}

  private final SelectorContextEventBinder EVENT_BINDER = GWT.create(SelectorContextEventBinder.class);

  private final Set<String> activeSelectors = new HashSet<>();

  private final Map<String, Selector> selectors = new HashMap<String, Selector>();

  private final Map<String, String> rememberedDefaults = new HashMap<>();

  private EventBus eventBus;

  @Inject
  public SelectorContext(final ConfigurationDaemon config) {
    SelectorUtil.initializeSelectorContext(this);

    retainStored();
    rememberedDefaults.put(Configurations.UNIT, config.getConfig(Configurations.UNIT));
  }

  private void retainStored() {
    for (final String cookie : Cookies.getCookieNames()) {
      if (cookie.startsWith(MONITOR_SELECTORS_START_KEY)) {
        retainCookie(cookie.substring(MONITOR_SELECTORS_START_KEY.length()), Cookies.getCookie(cookie));
      }
    }
  }

  private void storeDefault(final Selector selector) {
    if (!selector.getValue().isPresent()) {
      forgetDefault(selector.getType());
    } else {
      persistDefault(selector);
    }
  }

  private void persistDefault(final Selector selector) {
    rememberedDefaults.put(selector.getType(), selector.getValue().get());
    Cookies.setCookie(MONITOR_SELECTORS_START_KEY + selector.getType(), selector.getValue().get(),
        new Date(System.currentTimeMillis() + TimeUtil.WEEK));
  }

  private void forgetDefault(final String type) {
    // GWT.log("Forgetting default selector type: " + type);
    rememberedDefaults.remove(type);
    Cookies.removeCookie(MONITOR_SELECTORS_START_KEY + type);
  }

  private void retainCookie(final String key, final String value) {
    rememberedDefaults.put(key, value);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    this.eventBus = eventBus;
    EVENT_BINDER.bindEventHandlers(this, eventBus);

    UglyBoilerPlate.addSystemSelectors(activeSelectors);
    UglyBoilerPlate.populateDefaults(rememberedDefaults, selectors);
  }

  @EventHandler
  public void onSelectorConfigurationReloadEvent(final SelectorConfigurationReloadEvent e) {
    selectors.remove(e.getValue());
  }

  @EventHandler
  public void onSelectorConfigurationClearEvent(final SelectorConfigurationClearEvent e) {
    activeSelectors.clear();
    selectors.clear();
    UglyBoilerPlate.addSystemSelectors(activeSelectors);
    UglyBoilerPlate.populateDefaults(rememberedDefaults, selectors);
  }

  @EventHandler
  public void onSelectorConfigurationChangedEvent(final SelectorConfigurationChangeEvent e) {
    final SelectorConfiguration value = e.getValue();

    activeSelectors.add(value.getType());

    final List<Selector> options = value.getOptions();
    if (!options.isEmpty()) {
      final String devault = rememberedDefaults.get(value.getType());
      final Selector selector = options.stream().filter(v -> v.getValue().get().equals(devault)).findFirst().orElse(options.get(0));
      setIfNotSet(selector);
    } else {
      final Selector empty = new Selector(value.getType(), Optional.empty());
      set(empty);
    }
  }

  @EventHandler
  public void onSelectorCommand(final SelectorEvent e) {
    final Selector ret = selectors.put(e.getSelector().getType(), e.getValue());

    // Store if the value changed.
    if (ret == null || !ret.equals(e.getValue())) {
      storeDefault(e.getSelector());
    }
  }

  public void broadcast() {
    for (final Selector sel : getSelectors().entrySet().stream()
        .filter(v -> activeSelectors.contains(v.getKey()) || v.getValue().isSystem()).map(v -> v.getValue())
        .collect(Collectors.toList())) {
      eventBus.fireEvent(new SelectorEvent(sel));
    }
  }

  public void setIfNotSet(final String key, final String value) {
    if (selectors.containsKey(key)) {
      return;
    }

    set(new Selector(key, value));
  }

  public void setIfNotSet(final Selector selector) {
    if (selectors.containsKey(selector.getType())) {
      return;
    }

    set(selector);
  }

  public void set(final Selector selector) {
    selectors.put(selector.getType(), selector);

    eventBus.fireEvent(new SelectorEvent(selector));
  }

  public Map<String, Selector> getDefaultSelectors() {
    return selectors.entrySet()
        .stream()
        .filter(v -> v.getValue().getSelector().isSystem())
        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
  }

  public Map<String, Selector> getSelectors() {
    return selectors.entrySet()
        .stream()
        .filter(v -> activeSelectors.contains(v.getKey()))
        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
  }
}
