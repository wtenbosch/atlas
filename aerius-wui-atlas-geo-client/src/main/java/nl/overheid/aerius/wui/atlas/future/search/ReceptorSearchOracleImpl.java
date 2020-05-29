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
package nl.overheid.aerius.wui.atlas.future.search;

import java.util.List;
import java.util.function.Consumer;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import nl.overheid.aerius.geo.util.ReceptorUtil;
import nl.overheid.aerius.shared.domain.SearchSuggestion;
import nl.overheid.aerius.wui.atlas.future.StaggeredOracle;
import nl.overheid.aerius.wui.atlas.service.SearchServiceAsync;
import nl.overheid.aerius.wui.domain.cache.CacheContext;

@Singleton
public class ReceptorSearchOracleImpl extends StaggeredOracle<List<SearchSuggestion>> implements SearchOracle {
  private final SearchServiceAsync service;
  private final ReceptorUtil receptorUtil;

  @Inject
  public ReceptorSearchOracleImpl(final ReceptorUtil receptorUtil, final CacheContext cacheContext, final SearchServiceAsync searchService,
      final SearchCache cache) {
    super(cacheContext, cache);
    this.receptorUtil = receptorUtil;
    this.service = searchService;
  }

  @Override
  public boolean searchQuery(final String query, final Consumer<SearchSuggestion> callback) {
    // First, compute local results
    findDirectResults(query, callback);

    // Then, defer to the query service
    return ask(cache, query, c -> service.getQueryResult(query, c), v -> v.forEach(i -> callback.accept(i)));
  }

  private void findDirectResults(final String query, final Consumer<SearchSuggestion> callback) {
    ReceptorCoordinateSearchUtil.search(receptorUtil, query, callback);
  }
}
