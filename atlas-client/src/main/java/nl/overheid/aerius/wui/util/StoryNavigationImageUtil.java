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

import com.google.gwt.resources.client.DataResource;

import nl.overheid.aerius.shared.domain.StoryIcon;
import nl.overheid.aerius.wui.resources.AtlasR;
import nl.overheid.aerius.wui.resources.StoryNavigationResources;

public final class StoryNavigationImageUtil {
  private StoryNavigationImageUtil() {}

  public static DataResource getImageResource(final StoryIcon option) {
    final StoryNavigationResources sir = AtlasR.images();
    final DataResource ir;

    switch (option) {
    case ROOM_AND_UTILIZATION:
      ir = sir.storyNavigationRoomAndUtilization();
      break;
    case PRIORITY_PROJECTS:
      ir = sir.storyNavigationPriorityProjects();
      break;
    case PERMITS:
      ir = sir.storyNavigationPermits();
      break;
    case EMISSION:
      ir = sir.storyNavigationEmission();
      break;
    case DEPOSITION:
      ir = sir.storyNavigationDeposition();
      break;
    case DEPOSITION_AND_NATURE:
      ir = sir.storyNavigationDepositionAndNature();
      break;
    case MEASUREMENTS:
      ir = sir.storyNavigationMeasurements();
      break;
    case LOCAL_MEASURES:
      ir = sir.storyNavigationLocalMeasures();
      break;
    case BASIC_INFORMATION:
      ir = sir.storyNavigationBasicInformation();
      break;
    case ANALYSIS:
      ir = sir.storyNavigationAnalysis();
      break;
    case NATURE_INFORMATION:
      ir = sir.storyNatureInformation();
      break;
    default:
      throw new IllegalArgumentException("Unsupported ContextNavigationControlOption.");
    }

    return ir;
  }
}
