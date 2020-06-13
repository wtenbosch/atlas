package nl.overheid.aerius.wui.util;

import com.google.gwt.dom.client.Element;

import elemental2.dom.Document;
import elemental2.dom.HTMLIFrameElement;
import jsinterop.base.Js;

public final class IframeInspector {
  private IframeInspector() {}

  public static Document getElem(final Element gwtElem) {
    final HTMLIFrameElement elem = Js.cast(gwtElem);

    return elem.contentDocument;
  }
}
