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

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.LevelOption;
import nl.overheid.aerius.shared.domain.NatureArea;

public interface FilterContext {
  AreaGroupType getAreaGroup();

  boolean setAreaGroup(AreaGroupType value);

  Map<AreaGroupType, List<NatureArea>> getAreaGroups();

  boolean setAreaGroups(Map<AreaGroupType, List<NatureArea>> value);

  boolean setLevels(List<LevelOption> levels);

  List<LevelOption> getLevels();

  NatureArea findAssessmentArea(AreaGroupType areaGroupId, String assessmentAreaId);

  void reset();

}
