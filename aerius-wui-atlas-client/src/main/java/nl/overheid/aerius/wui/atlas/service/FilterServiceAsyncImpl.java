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
package nl.overheid.aerius.wui.atlas.service;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.NatureArea;
import nl.overheid.aerius.wui.atlas.service.parser.FilterJsonParser;

public class FilterServiceAsyncImpl implements FilterServiceAsync {
  private static final String METHOD_NAME = "GetNatura2000Areas";

  @Override
  public void getNatura2000Areas(final AsyncCallback<Map<AreaGroupType, List<NatureArea>>> callback) {
    RequestUtil.doMethodGet(METHOD_NAME, v -> FilterJsonParser.wrap(v), callback);
  }
}
