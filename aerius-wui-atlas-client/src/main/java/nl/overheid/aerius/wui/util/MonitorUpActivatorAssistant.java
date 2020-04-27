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
package nl.overheid.aerius.wui.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.wui.activators.IsActivatorInfo;
import nl.overheid.aerius.wui.atlas.event.ActivatorActiveEvent;
import nl.overheid.aerius.wui.atlas.event.ActivatorInactiveEvent;
import nl.overheid.aerius.wui.event.BasicEventComponent;

@Singleton
public class MonitorUpActivatorAssistant extends BasicEventComponent implements ActivatorAssistant {
  interface MonitorUpActivatorAssistantEventBinder extends EventBinder<MonitorUpActivatorAssistant> {}

  private final MonitorUpActivatorAssistantEventBinder EVENT_BINDER = GWT.create(MonitorUpActivatorAssistantEventBinder.class);

  private final Set<String> actives = new HashSet<>();

  private class ActivationItem<T extends IsActivatorInfo> {
    private final List<T> activators;
    private final Consumer<List<T>> consumer;

    public ActivationItem(final List<T> activators, final Consumer<List<T>> consumer) {
      this.activators = activators;
      this.consumer = consumer;
    }
  }

  @SuppressWarnings("rawtypes") private final Map<Object, ActivationItem> targets = new HashMap<>();

  @EventHandler
  public void onActivatorActiveEvent(final ActivatorActiveEvent e) {
    actives.add(e.getValue());

    update();
  }

  private void update() {
    targets.values().forEach(this::acceptActivators);
  }

  @SuppressWarnings("unchecked")
  private void acceptActivators(@SuppressWarnings("rawtypes") final ActivationItem target) {
    target.consumer.accept(filterActivators(target, actives));
  }

  private <T extends IsActivatorInfo> List<T> filterActivators(final ActivationItem<T> target, final Set<String> actives) {
    return target.activators
        .stream()
        .filter(v -> actives.contains(v.getName()))
        .collect(Collectors.toList());
  }

  @EventHandler
  public void onActivatorInactiveEvent(final ActivatorInactiveEvent e) {
    actives.remove(e.getValue());

    update();
  }

  @Override
  public <T extends IsActivatorInfo> ReplacementRegistration register(final List<T> activators, final Consumer<List<T>> consumer) {
    final Object handle = new Object();

    final ActivationItem<T> target = new ActivationItem<T>(activators, consumer);
    targets.put(handle, target);

    // Set consumer with initial state.
    acceptActivators(target);

    return () -> targets.remove(handle);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
