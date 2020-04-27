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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.wui.atlas.service.parser.ProxiedCallback;
import nl.overheid.aerius.wui.domain.cache.CacheContext;
import nl.overheid.aerius.wui.future.Oracle;

public class StaggeredOracle<T> extends CachingOracle<T> implements Oracle<T> {
  private boolean busy;
  private final List<Runnable> straglers = new ArrayList<>();

  private final List<Runnable> completers = new ArrayList<>();

  public StaggeredOracle(final CacheContext cacheContext, final Map<Object, T> cache) {
    super(cacheContext, cache);
  }

  /**
   * Returns whether this call is deferred (true), or yielded immediate/cached results (false).
   */
  @Override
  public boolean ask(final Map<Object, T> cache, final Object key, final Consumer<AsyncCallback<T>> call, final AsyncCallback<T> callback) {
    straglers.add(() -> super.ask(cache, key, call, ProxiedCallback.wrap(callback, () -> requestNext())));

    if (!busy) {
      busy = true;
      requestNext();
      return true;
    }

    return false;
  }

  private void requestNext() {
    if (!straglers.isEmpty()) {
      straglers.remove(0).run();
    } else {
      busy = false;
      complete();
    }
  }

  protected void complete() {
    completers.forEach(v -> v.run());
    completers.clear();
  }

  public void subscribeCompletion(final Runnable runnable) {
    completers.add(runnable);
  }
}
