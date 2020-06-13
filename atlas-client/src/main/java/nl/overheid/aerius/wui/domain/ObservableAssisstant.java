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
package nl.overheid.aerius.wui.domain;

import java.util.function.Supplier;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public final class ObservableAssisstant {
  private final EventBus eventBus;

  @Inject
  public ObservableAssisstant(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public <T, E extends SimpleGenericEvent<T>> boolean fireIfChanged(final Supplier<E> s, final Supplier<Boolean> c, final T cur) {
    final boolean changed = c.get();

    if (changed) {
      final E event = s.get();
      event.setValue(cur);
      eventBus.fireEvent(event);
    }

    return changed;
  }
}
