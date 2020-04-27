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

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.wui.resources.AreaGroupResources;
import nl.overheid.aerius.wui.resources.R;

public final class AreaGroupImageUtil {
  private AreaGroupImageUtil() {}

  public static DataResource getImageResource(final AreaGroupType icon) {
    if (icon == null) {
      throw new RuntimeException("No icon for area group.");
    }

    final AreaGroupResources sir = R.images();
    final DataResource ir;

    switch (icon) {
    case DRENTHE:
      ir = sir.provinceDrenthe();
      break;
    case FLEVOLAND:
      ir = sir.provinceFlevoland();
      break;
    case FRIESLAND:
      ir = sir.provinceFriesland();
      break;
    case GELDERLAND:
      ir = sir.provinceGelderland();
      break;
    case GRONINGEN:
      ir = sir.provinceGroningen();
      break;
    case LIMBURG:
      ir = sir.provinceLimburg();
      break;
    case NOORDBRABANT:
      ir = sir.provinceNoordBrabant();
      break;
    case NOORDHOLLAND:
      ir = sir.provinceNoordHolland();
      break;
    case OVERIJSSEL:
      ir = sir.provinceOverijssel();
      break;
    case RIJKSOVERHEID:
      ir = sir.provinceRijksoverheid();
      break;
    case UTRECHT:
      ir = sir.provinceUtrecht();
      break;
    case ZEELAND:
      ir = sir.provinceZeeland();
      break;
    case ZUIDHOLLAND:
      ir = sir.provinceZuidHolland();
      break;
    default:
      throw new IllegalArgumentException("Unsupported ContextNavigationControlOption.");
    }

    return ir;
  }
}
