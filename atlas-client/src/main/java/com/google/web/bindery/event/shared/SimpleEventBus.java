/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.web.bindery.event.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.google.web.bindery.event.shared.Event.Type;

/**
 * Basic implementation of {@link EventBus}.
 */
public class SimpleEventBus extends EventBus {
  private interface Command {
    void execute();
  }

  private final boolean isReverseOrder;

  private int firingDepth = 0;

  /**
   * Add and remove operations received during dispatch.
   */
  private List<Command> deferredDeltas;

  /**
   * Map of event type to map of event source to list of their handlers.
   */
  private final Map<Event.Type<?>, Map<Object, List<?>>> map = new HashMap<Event.Type<?>, Map<Object, List<?>>>();

  public SimpleEventBus() {
    this(false);
  }

  /**
   * Allows creation of an instance that fires its handlers in the reverse of the order in which they were added, although filtered handlers all fire
   * before unfiltered handlers.
   * <p>
   *
   * @deprecated This is a legacy feature, required by GWT's old HandlerManager. Reverse order is not honored for handlers tied to a specific event
   *             source (via {@link #addHandlerToSource}.
   */
  @Deprecated
  protected SimpleEventBus(final boolean fireInReverseOrder) {
    isReverseOrder = fireInReverseOrder;
  }

  @Override
  public <H> HandlerRegistration addHandler(final Type<H> type, final H handler) {
    return doAdd(type, null, handler);
  }

  @Override
  public <H> HandlerRegistration addHandlerToSource(final Event.Type<H> type, final Object source, final H handler) {
    if (source == null) {
      throw new NullPointerException("Cannot add a handler with a null source");
    }

    return doAdd(type, source, handler);
  }

  @Override
  public void fireEvent(final Event<?> event) {
    doFire(event, null);
  }

  @Override
  public void fireEventFromSource(final Event<?> event, final Object source) {
    if (source == null) {
      throw new NullPointerException("Cannot fire from a null source");
    }
    doFire(event, source);
  }

  /**
   * @deprecated required by legacy features in GWT's old HandlerManager
   */
  @Deprecated
  protected <H> H getHandler(final Event.Type<H> type, final int index) {
    assert index < getHandlerCount(type) : "handlers for " + type.getClass() + " have size: " + getHandlerCount(type)
        + " so do not have a handler at index: " + index;

    final List<H> l = getHandlerList(type, null);
    return l.get(index);
  }

  /**
   * @deprecated required by legacy features in GWT's old HandlerManager
   */
  @Deprecated
  protected int getHandlerCount(final Event.Type<?> eventKey) {
    return getHandlerList(eventKey, null).size();
  }

  /**
   * @deprecated required by legacy features in GWT's old HandlerManager
   */
  @Deprecated
  protected boolean isEventHandled(final Event.Type<?> eventKey) {
    return map.containsKey(eventKey);
  }

  private <H> HandlerRegistration doAdd(final Event.Type<H> type, final Object source, final H handler) {
    if (type == null) {
      throw new NullPointerException("Cannot add a handler with a null type");
    }
    if (handler == null) {
      throw new NullPointerException("Cannot add a null handler");
    }

    if (firingDepth > 0) {
      // enqueueAdd(type, source, handler);
      doAddNow(type, source, handler);
    } else {
      doAddNow(type, source, handler);
    }

    return () -> doRemove(type, source, handler);
  }

  /**
   * @deprecated required by legacy features in GWT's old HandlerManager
   */
  @Deprecated
  protected <H> void doRemove(final Event.Type<H> type, final Object source, final H handler) {
    if (firingDepth > 0) {
      // enqueueRemove(type, source, handler);
      doRemoveNow(type, source, handler);
    } else {
      doRemoveNow(type, source, handler);
    }
  }

  private <H> void doAddNow(final Event.Type<H> type, final Object source, final H handler) {
    final List<H> l = ensureHandlerList(type, source);
    l.add(handler);
  }

  private <H> void doFire(final Event<H> event, final Object source) {
    if (event == null) {
      throw new NullPointerException("Cannot fire null event");
    }

    try {
      firingDepth++;

      if (source != null) {
        setSourceOfEvent(event, source);
      }

      final List<H> handlers = getDispatchList(event.getAssociatedType(), source);
      Set<Throwable> causes = null;

      final ListIterator<H> it = isReverseOrder ? handlers.listIterator(handlers.size()) : handlers.listIterator();
      while (isReverseOrder ? it.hasPrevious() : it.hasNext()) {
        final H handler = isReverseOrder ? it.previous() : it.next();

        try {
          dispatchEvent(event, handler);
        } catch (final Throwable e) {
          if (causes == null) {
            causes = new HashSet<Throwable>();
          }
          causes.add(e);
        }
      }

      if (causes != null) {
        throw new UmbrellaException(causes);
      }
    } finally {
      firingDepth--;
      if (firingDepth == 0) {
        handleQueuedAddsAndRemoves();
      }
    }
  }

  private <H> void doRemoveNow(final Event.Type<H> type, final Object source, final H handler) {
    final List<H> l = getHandlerList(type, source);

    final boolean removed = l.remove(handler);

    if (removed && l.isEmpty()) {
      prune(type, source);
    }
  }

  private <H> List<H> ensureHandlerList(final Event.Type<H> type, final Object source) {
    Map<Object, List<?>> sourceMap = map.get(type);
    if (sourceMap == null) {
      sourceMap = new HashMap<Object, List<?>>();
      map.put(type, sourceMap);
    }

    // safe, we control the puts.
    @SuppressWarnings("unchecked")
    List<H> handlers = (List<H>) sourceMap.get(source);
    if (handlers == null) {
      handlers = new ArrayList<H>();
      sourceMap.put(source, handlers);
    }

    return handlers;
  }

  private <H> List<H> getDispatchList(final Event.Type<H> type, final Object source) {
    final List<H> directHandlers = getHandlerList(type, source);
    if (source == null) {
      return directHandlers;
    }

    final List<H> globalHandlers = getHandlerList(type, null);

    final List<H> rtn = new ArrayList<H>(directHandlers);
    rtn.addAll(globalHandlers);
    return rtn;
  }

  private <H> List<H> getHandlerList(final Event.Type<H> type, final Object source) {
    final Map<Object, List<?>> sourceMap = map.get(type);
    if (sourceMap == null) {
      return Collections.emptyList();
    }

    // safe, we control the puts.
    @SuppressWarnings("unchecked")
    final List<H> handlers = (List<H>) sourceMap.get(source);
    if (handlers == null) {
      return Collections.emptyList();
    }

    return handlers;
  }

  private void handleQueuedAddsAndRemoves() {
    if (deferredDeltas != null) {
      try {
        for (final Command c : deferredDeltas) {
          c.execute();
        }
      } finally {
        deferredDeltas = null;
      }
    }
  }

  private void prune(final Event.Type<?> type, final Object source) {
    final Map<Object, List<?>> sourceMap = map.get(type);

    final List<?> pruned = sourceMap.remove(source);

    assert pruned != null : "Can't prune what wasn't there";
    assert pruned.isEmpty() : "Pruned unempty list!";

    if (sourceMap.isEmpty()) {
      map.remove(type);
    }
  }
}