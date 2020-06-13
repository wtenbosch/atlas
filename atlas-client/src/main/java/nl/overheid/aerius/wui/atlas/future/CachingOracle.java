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
package nl.overheid.aerius.wui.atlas.future;

import java.util.Map;
import java.util.function.Consumer;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.wui.domain.cache.CacheContext;
import nl.overheid.aerius.wui.future.CachingCallback;
import nl.overheid.aerius.wui.future.Oracle;

public abstract class CachingOracle<T> implements Oracle<T> {
  private final CacheContext context;

  protected final Map<Object, T> cache;

  public CachingOracle(final CacheContext context, final Map<Object, T> cache) {
    this.context = context;
    this.cache = cache;

    context.register(cache);
  }

  @Override
  public boolean ask(final Map<Object, T> cache, final Object key, final Consumer<AsyncCallback<T>> call, final AsyncCallback<T> callback) {
    final boolean ret = context.isCaching() && cache.containsKey(key);

    if (ret) {
      // Defer this so we don't break the implicit async assumption
      Scheduler.get().scheduleDeferred(() -> callback.onSuccess(cache.get(key)));
    } else {
      call.accept(new CachingCallback<T>(key, cache, callback));
    }

    return !ret;
  }
}
