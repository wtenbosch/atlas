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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import elemental2.dom.Document;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLIFrameElement;
import elemental2.dom.HTMLScriptElement;
import jsinterop.base.Js;

import nl.overheid.aerius.wui.atlas.command.ChapterReplacementCommand;
import nl.overheid.aerius.wui.atlas.command.SelectionCommand;
import nl.overheid.aerius.wui.atlas.event.SelectorEvent;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.util.NativeSelectorDomEvents;

public class IframeWebComponent extends Widget {
  private final String name;
  private final String src;

  private final HTMLIFrameElement frame;

  private final Consumer<Element> func;
  private final EventBus eventBus;

  @UiConstructor
  public IframeWebComponent(final String name, final String src, final EventBus eventBus, final Consumer<Element> func) {
    this.name = name;
    this.src = src;
    this.eventBus = eventBus;
    this.func = func;
    frame = Js.cast(DomGlobal.document.createElement("iframe"));

    final Document doc = frame.contentDocument;

    final HTMLScriptElement script = Js.cast(DomGlobal.document.createElement("script"));
    script.type = "module";
    script.src = UglyBoilerPlate.sanitizeComponentSource(src) + "?" + System.currentTimeMillis();

    final String html = "<head><script type=\"module\" src=\"" + UglyBoilerPlate.sanitizeComponentSource(src)
        + "\"></script></head><body><" + name + "></" + name + "></body>";

    frame.srcdoc = html;

    setElement(Js.cast(frame));
  }

  @Override
  protected void onLoad() {
    super.onLoad();

    new Timer() {

      @Override
      public void run() {
        final JavaScriptObject comp = Js.uncheckedCast(frame.contentDocument.lastChild.lastChild.firstChild);

        final Element elem = comp.cast();
        NativeSelectorDomEvents.addEventListener(elem, s -> eventBus.fireEvent(new SelectorEvent(s)));
        NativeSelectorDomEvents.addReplacementListener(elem, s -> eventBus.fireEvent(new ChapterReplacementCommand(s)));
        NativeSelectorDomEvents.addSelectionListener(elem, s -> eventBus.fireEvent(new SelectionCommand(s)));

        func.accept(elem);
      }

    }.schedule(250);
  }
}
