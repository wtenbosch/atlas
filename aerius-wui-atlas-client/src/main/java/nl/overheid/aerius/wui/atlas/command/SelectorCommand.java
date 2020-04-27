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
package nl.overheid.aerius.wui.atlas.command;

import nl.overheid.aerius.shared.domain.IsSelector;
import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.wui.atlas.event.SelectorEvent;
import nl.overheid.aerius.wui.command.SimpleGenericCommand;

public class SelectorCommand extends SimpleGenericCommand<Selector, SelectorEvent> implements IsSelector {
  public SelectorCommand(final Selector value) {
    super(value);
  }

  @Override
  protected SelectorEvent createEvent(final Selector value) {
    return new SelectorEvent(value);
  }

  @Override
  public Selector getSelector() {
    return getValue();
  }
}
