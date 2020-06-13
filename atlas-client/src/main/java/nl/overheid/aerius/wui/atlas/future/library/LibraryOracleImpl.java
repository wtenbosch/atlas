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
package nl.overheid.aerius.wui.atlas.future.library;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import nl.overheid.aerius.shared.domain.Criterium;
import nl.overheid.aerius.shared.domain.NarrowLibrary;
import nl.overheid.aerius.wui.atlas.future.CachingOracle;
import nl.overheid.aerius.wui.atlas.service.LibraryServiceAsync;
import nl.overheid.aerius.wui.domain.cache.CacheContext;
import nl.overheid.aerius.wui.util.FilterAssistant;

@Singleton
public class LibraryOracleImpl extends CachingOracle<NarrowLibrary> implements LibraryOracle {
  private final LibraryServiceAsync libraryService;
  
  @Inject FilterAssistant filterAssistant;

  @Inject
  public LibraryOracleImpl(final CacheContext context, final LibraryServiceAsync storyService, final LibraryCache cache) {
    super(context, cache);

    this.libraryService = storyService;
  }

  @Override
  public boolean getStories(final List<Criterium> filters, final AsyncCallback<NarrowLibrary> future) {
    final String[] fltr = filterAssistant.formatFilters(filters);

    return ask(cache, fltr, c -> libraryService.getStories(c, fltr), future);
  }

  @Override
  public boolean getStories(final Map<String, String> filters, final AsyncCallback<NarrowLibrary> callback) {
    return getStories(filterAssistant.toCriteria(filters), callback);
  }
}
