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
package nl.overheid.aerius.wui.widget;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.binder.EventBinder;

public abstract class EventComposite extends Composite implements HasEventBus {
  protected EventBus eventBus;
  private HandlerRegistration registration;

  protected <T> HandlerRegistration bindEventHandlers(final T thiz, final EventBinder<T> binder) {
    return binder.bindEventHandlers(thiz, eventBus);
  }

  public <T> HandlerRegistration setEventBus(final EventBus eventBus, final T thiz, final EventBinder<T> binder,
      final HasEventBus... children) {
    if (registration != null) {
      GWT.log("[INFO] REMOVING HANDLERS FROM PREVIOUS SETTING. THIS IS INTENDED BUT NON-ORDINARY.");
      registration.removeHandler();
    }

    this.eventBus = eventBus;
    registration = bindEventHandlers(thiz, binder);

    setEventBus(eventBus, children);

    return registration;
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  protected void setEventBus(final EventBus eventBus, final HasEventBus... composites) {
    this.eventBus = eventBus;

    for (final HasEventBus comp : composites) {
      comp.setEventBus(eventBus);
    }
  }
}
