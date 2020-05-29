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
package nl.overheid.aerius.wui.util;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.event.InfoLocationChangeEvent;

/**
 * TODO: Move to Monitor project.
 */
@Singleton
public class MonitorUpObservingReplacementAssistant extends AtlasObservingReplacementAssistant {
  interface ObservingReplacementAssistantEventBinder extends EventBinder<MonitorUpObservingReplacementAssistant> {}

  private final ObservingReplacementAssistantEventBinder EVENT_BINDER = GWT.create(ObservingReplacementAssistantEventBinder.class);

  @Inject
  public MonitorUpObservingReplacementAssistant(final ReplacementAssistant replacer, final EventBus eventBus) {
    super(replacer, eventBus);
  }

  @EventHandler
  public void onInfoLocationChangeEvent(final InfoLocationChangeEvent e) {
    scheduleUpdate();
  }

  @Override
  protected void setEventBus(final EventBus eventBus) {
    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }
}
