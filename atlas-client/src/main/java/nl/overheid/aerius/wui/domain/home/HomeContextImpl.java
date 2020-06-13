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
package nl.overheid.aerius.wui.domain.home;

import java.util.List;

import nl.overheid.aerius.shared.domain.LevelOption;
import nl.overheid.aerius.wui.util.ObjectUtil;

public class HomeContextImpl implements HomeContext {
  protected List<LevelOption> levels;

  @Override
  public boolean setLevels(final List<LevelOption> levels) {
    if (levels == null) {
      return false;
    }

    final boolean change = !ObjectUtil.equals(this.levels, levels);
    this.levels = levels;
    return change;
  }

  @Override
  public List<LevelOption> getLevels() {
    return levels;
  }

  @Override
  public void reset() {
    levels = null;
  }
}
