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
package nl.overheid.aerius.wui.service;

import java.util.ArrayList;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AggregatedAsyncCallback<T> implements AsyncCallback<T> {
  private final ArrayList<AsyncCallback<T>> listeners = new ArrayList<AsyncCallback<T>>();
  private final ArrayList<AsyncCallback<T>> lastListeners = new ArrayList<AsyncCallback<T>>();

  @Override
  public void onFailure(final Throwable caught) {
    for (final AsyncCallback<T> listener : listeners) {
      listener.onFailure(caught);
    }
    for (final AsyncCallback<T> listener : lastListeners) {
      listener.onFailure(caught);
    }
  }

  @Override
  public void onSuccess(final T result) {
    for (final AsyncCallback<T> listener : listeners) {
      listener.onSuccess(result);
    }

    // Lazily call all the remainder
    Scheduler.get().scheduleDeferred(() -> {
      for (final AsyncCallback<T> listener : lastListeners) {
        listener.onSuccess(result);
      }
    });
  }

  public void addListener(final AsyncCallback<T> listener) {
    listeners.add(listener);
  }

  public void addLastListener(final AsyncCallback<T> listener) {
    lastListeners.add(listener);
  }
}