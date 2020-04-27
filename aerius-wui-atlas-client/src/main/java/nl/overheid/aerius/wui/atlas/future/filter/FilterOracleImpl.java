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
package nl.overheid.aerius.wui.atlas.future.filter;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.NatureArea;
import nl.overheid.aerius.wui.atlas.future.CachingOracle;
import nl.overheid.aerius.wui.atlas.service.FilterServiceAsync;
import nl.overheid.aerius.wui.domain.cache.CacheContext;

@Singleton
public class FilterOracleImpl extends CachingOracle<Map<AreaGroupType, List<NatureArea>>> implements FilterOracle {
  private static final String NATURA_2000_KEY = "n2000";

  private final FilterServiceAsync service;

  @Inject
  public FilterOracleImpl(final CacheContext context, final FilterServiceAsync storyService, final FilterCache criteriaCache) {
    super(context, criteriaCache);

    this.service = storyService;
  }

  @Override
  public boolean getAreaGroups(final AsyncCallback<Map<AreaGroupType, List<NatureArea>>> callback) {
    return ask(cache, NATURA_2000_KEY, v -> service.getNatura2000Areas(v), callback);
  }
}
