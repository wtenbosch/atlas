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
package nl.overheid.aerius.wui.atlas.future.selector;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import nl.overheid.aerius.shared.domain.SelectorConfiguration;
import nl.overheid.aerius.wui.atlas.future.CachingOracle;
import nl.overheid.aerius.wui.atlas.future.HardenedConcurrentCallback;
import nl.overheid.aerius.wui.atlas.future.ProvidesConcurrencyToken;
import nl.overheid.aerius.wui.atlas.service.SelectorServiceAsync;
import nl.overheid.aerius.wui.domain.cache.CacheContext;

@Singleton
public class SelectorOracleImpl extends CachingOracle<SelectorConfiguration> implements SelectorOracle, ProvidesConcurrencyToken {
  private final SelectorServiceAsync selectorService;

  private final Map<String, String> concurrencyTokens = new HashMap<>();

  @Inject
  public SelectorOracleImpl(final CacheContext context, final SelectorServiceAsync selectorService, final SelectorCache cache) {
    super(context, cache);

    this.selectorService = selectorService;
  }

  @Override
  public boolean getSelectorContent(final String url, final AsyncCallback<SelectorConfiguration> callback) {
    final String token = DOM.createUniqueId();
    concurrencyTokens.put(url, token);

    return ask(cache, url,
        c -> selectorService.getSelectorConfiguration(url, HardenedConcurrentCallback.wrap(url, token, this, c)), callback);
  }

  @Override
  public boolean getSelectorContentSoftly(final String url, final AsyncCallback<SelectorConfiguration> callback) {
    final String token = DOM.createUniqueId();
    concurrencyTokens.put(url, token);

    return ask(cache, url, c -> selectorService.getSelectorConfiguration(url, c), callback);
  }

  @Override
  public String getStateToken(final String identifier) {
    return concurrencyTokens.get(identifier);
  }
}
