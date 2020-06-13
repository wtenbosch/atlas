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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.overheid.aerius.shared.domain.Selector;

public abstract class SelectorCapable {
  private List<String> types;

  private final Map<String, Selector> selected = new HashMap<>();

  private void processSelector(final Selector selector) {
    if (types == null || !types.contains(selector.getType())) {
      return;
    }

    selected.put(selector.getType(), selector);
  }

  protected abstract void setContentText(String contentText);

  public void setTypes(final List<String> types) {
    this.types = types;
  }

  public void clear() {
    selected.clear();
    if (types != null) {
      types = null;
    }
  }

  public void setSelectors(final List<Selector> selectors) {
    selectors.forEach(v -> processSelector(v));
  }
}
