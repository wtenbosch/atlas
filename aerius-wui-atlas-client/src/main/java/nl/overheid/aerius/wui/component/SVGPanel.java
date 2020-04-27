package nl.overheid.aerius.wui.component;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;

public class SVGPanel extends ComplexPanel {
  private static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";

  public SVGPanel() {
    setElement(createElementNS(SVG_NAMESPACE, "svg"));
    showcaseSVG();
  }

  private void showcaseSVG() {
    final Element defs = createElementNS(SVG_NAMESPACE, "defs");
    getElement().appendChild(defs);

    final Element path = createElementNS(SVG_NAMESPACE, "path");
    path.setAttribute("id", "myPath");
    path.setAttribute("stroke", "black");
    path.setAttribute("d", "M75,20 a1,1 0 0,0 100,0");
    defs.appendChild(path);

    final Element text = createElementNS(SVG_NAMESPACE, "text");
    text.setAttribute("x", "10");
    text.setAttribute("y", "100");
    getElement().appendChild(text);

    final Element textPath = createElementNS(SVG_NAMESPACE, "textPath");
    textPath.setAttribute("xlink:href", "#myPath");
    textPath.setInnerText("Text along a curved path...");
    text.appendChild(textPath);
  }

  private native Element createElementNS(final String ns,
      final String name)/*-{
                        return document.createElementNS(ns, name);
                        }-*/;
}
