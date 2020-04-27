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
package nl.overheid.aerius.wui.command;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * TODO Split isSilent into isSilent (for broadcast) and isStrict (for pattern enforcement)
 */
public abstract class GenericCommand<E extends GenericEvent> extends GenericEvent implements Command<E> {
  private boolean silent;

  private E event;

  public GenericCommand() {}

  public GenericCommand(final boolean silent) {
    this.silent = silent;
  }

  protected abstract E createEvent();

  @Override
  public E getEvent() {
    return event == null ? createEvent() : event;
  }

  @Override
  public boolean isSilent() {
    return silent;
  }

  @Override
  public void setSilent(final boolean silent) {
    this.silent = silent;
  }
}
