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

import java.util.function.Consumer;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.wui.atlas.command.ChapterReplacementCommand;
import nl.overheid.aerius.wui.atlas.command.SelectionCommand;
import nl.overheid.aerius.wui.atlas.event.SelectorEvent;
import nl.overheid.aerius.wui.util.NativeSelectorDomEvents;

public class WebComponent extends Widget {
  @UiConstructor
  public WebComponent(final String name, final EventBus eventBus, final Consumer<Element> func) {
    final Element elem = Document.get().createElement(name);

    NativeSelectorDomEvents.addEventListener(elem, s -> eventBus.fireEvent(new SelectorEvent(s)));
    NativeSelectorDomEvents.addReplacementListener(elem, s -> eventBus.fireEvent(new ChapterReplacementCommand(s)));
    NativeSelectorDomEvents.addSelectionListener(elem, s -> eventBus.fireEvent(new SelectionCommand(s)));

    func.accept(elem);

    setElement(elem);
  }
}
