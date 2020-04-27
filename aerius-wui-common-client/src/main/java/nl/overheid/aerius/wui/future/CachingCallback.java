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
package nl.overheid.aerius.wui.future;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class CachingCallback<T> implements AsyncCallback<T> {
  private final Map<Object, T> cache;
  private final AsyncCallback<T> callback;
  private final Object key;

  public CachingCallback(final Object key, final Map<Object, T> cache, final AsyncCallback<T> callback) {
    this.key = key;
    this.cache = cache;
    this.callback = callback;
  }

  @Override
  public void onSuccess(final T result) {
    cache.put(key, result);
    callback.onSuccess(result);
  }

  @Override
  public void onFailure(final Throwable caught) {
    // Fall through without caching
    callback.onFailure(caught);
  }
}
