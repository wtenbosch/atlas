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
package nl.overheid.aerius.shared.domain.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MetaProperties extends TextProperties {
  private static final String META_VALUE = "value";
  private static final String META_EXPLANATION = "explanation_dataset";

  private static final String ADS_URL = "ads_url";

  public MetaProperties(final Map<String, Object> map) {
    super(map);
  }

  public MetaProperties() {
    this(new HashMap<>());
  }

  public Optional<String> getExplanationDataset() {
    return getOptionalProperties(META_EXPLANATION).flatMap(v -> v.getOptionalString(META_VALUE));
  }

  public Optional<String> getDataService() {
    return getOptionalString(ADS_URL);
  }
}
