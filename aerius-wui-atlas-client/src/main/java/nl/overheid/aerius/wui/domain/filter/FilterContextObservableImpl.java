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
package nl.overheid.aerius.wui.domain.filter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.LevelOption;
import nl.overheid.aerius.shared.domain.NatureArea;
import nl.overheid.aerius.wui.atlas.event.AreaGroupChangeEvent;
import nl.overheid.aerius.wui.atlas.event.AreaGroupListChangedEvent;
import nl.overheid.aerius.wui.atlas.event.LevelListChangeEvent;
import nl.overheid.aerius.wui.event.SimpleGenericEvent;

public class FilterContextObservableImpl extends FilterContextImpl {
  private final EventBus eventBus;

  @Inject
  public FilterContextObservableImpl(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public boolean setAreaGroups(final Map<AreaGroupType, List<NatureArea>> areaGroups) {
    return fireIfChanged((v) -> new AreaGroupListChangedEvent(v), () -> super.setAreaGroups(areaGroups), areaGroups);
  }

  @Override
  public boolean setAreaGroup(final AreaGroupType areaGroup) {
    return fireIfChanged((v) -> new AreaGroupChangeEvent(v, areaGroups.get(v)), () -> super.setAreaGroup(areaGroup), areaGroup);
  }

  @Override
  public boolean setLevels(final List<LevelOption> levels) {
    return fireIfChanged((v) -> new LevelListChangeEvent(v), () -> super.setLevels(levels), levels);
  }

  private <T, E extends SimpleGenericEvent<T>> boolean fireIfChanged(final Function<T, E> s, final Supplier<Boolean> c, final T cur) {
    final boolean changed = c.get();

    if (changed) {
      final E event = s.apply(cur);
      eventBus.fireEvent(event);
    }

    return changed;
  }
}
