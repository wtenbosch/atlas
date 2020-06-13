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
package nl.overheid.aerius.wui.atlas.future.availability;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import nl.overheid.aerius.wui.atlas.future.CachingOracle;
import nl.overheid.aerius.wui.atlas.service.AvailabilityServiceAsync;
import nl.overheid.aerius.wui.domain.cache.CacheContext;

@Singleton
public class AvailabilityOracleImpl extends CachingOracle<Boolean> implements AvailabilityOracle {
  private final AvailabilityServiceAsync availabilityService;

  @Inject
  public AvailabilityOracleImpl(final AvailabilityServiceAsync storyService, final AvailabilityCache cache, final CacheContext cacheContext) {
    super(cacheContext, cache);

    this.availabilityService = storyService;
  }

  @Override
  public boolean getAvailability(final String url, final AsyncCallback<Boolean> callback) {
    return ask(cache, url, c -> availabilityService.getAvailability(url, c), callback);
  }
}
