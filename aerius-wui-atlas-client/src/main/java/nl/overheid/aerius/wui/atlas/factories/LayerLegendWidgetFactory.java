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
package nl.overheid.aerius.wui.atlas.factories;

import java.util.List;

import nl.overheid.aerius.geo.domain.legend.ComponentLegendConfig;
import nl.overheid.aerius.wui.component.LayerLegendComponentViewImpl;
import nl.overheid.aerius.wui.component.LayerLegendLabelViewImpl;

public interface LayerLegendWidgetFactory {
  LayerLegendComponentViewImpl createComponentWidget(List<String> selectables, ComponentLegendConfig info);

  LayerLegendLabelViewImpl createLabelWidget(String label);
}
