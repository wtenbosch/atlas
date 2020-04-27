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
package nl.overheid.aerius.wui.component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;

import nl.overheid.aerius.shared.domain.NatureArea;
import nl.overheid.aerius.wui.atlas.util.HoverSelectionUtil;
import nl.overheid.aerius.wui.atlas.util.SimpleHoverBox;
import nl.overheid.aerius.wui.util.StyleUtil;

public class Natura2000SVG extends Composite {
  private static final String SVG_AREA_ID_PREFIX = "svg-area-";

  private static final Natura2000SVGUiBinder UI_BINDER = GWT.create(Natura2000SVGUiBinder.class);

  interface Natura2000SVGUiBinder extends UiBinder<Widget, Natura2000SVG> {}

  private static final String SVG_NS = "http://www.w3.org/2000/svg";

  public interface CustomStyle extends CssResource {
    String highlight();

    String outer();

    String inner();

    String hitbox();
  }

  // Ratio doesn't need to match exactly (or even roughly)
  private double svgWidth;
  private double svgHeight;

  private double minX;
  private double maxX;
  private double minY;
  private double maxY;

  private double correctTransformX = 1;
  private double correctTransformY = 1;

  private double correctTranslateX = 0;
  private double correctTranslateY = 0;

  private final Map<String, SimpleHoverBox> highlights = new HashMap<>();
  private final Map<String, NatureArea> areas = new HashMap<>();
  private final Map<String, Element> points = new HashMap<>();

  @UiField CustomStyle style;

  @UiField HTMLPanel svgContainer;
  @UiField Element svgTarget;

  @UiField FlowPanel debugPanel;
  @UiField TextBox correctTransformXField;
  @UiField TextBox correctTransformYField;
  @UiField TextBox correctTranslateXField;
  @UiField TextBox correctTranslateYField;

  @UiField TextBox correctMinXField;
  @UiField TextBox correctMinYField;
  @UiField TextBox correctMaxXField;
  @UiField TextBox correctMaxYField;

  @UiField TextBox shortcut;
  @UiField TextBox shortcut2;

  private List<NatureArea> areasRaw;

  public Natura2000SVG() {
    initWidget(UI_BINDER.createAndBindUi(this));
    svgTarget.setAttribute("xmlns", SVG_NS);
    update();

    StyleUtil.setPlaceHolder(correctTransformXField, "Transform x");
    StyleUtil.setPlaceHolder(correctTransformYField, "Transform y");
    StyleUtil.setPlaceHolder(correctTranslateXField, "Translate x");
    StyleUtil.setPlaceHolder(correctTranslateYField, "Translate y");

    StyleUtil.setPlaceHolder(correctMinXField, "min x");
    StyleUtil.setPlaceHolder(correctMinYField, "min y");
    StyleUtil.setPlaceHolder(correctMaxXField, "max x");
    StyleUtil.setPlaceHolder(correctMaxYField, "max y");

    StyleUtil.setPlaceHolder(shortcut, "boundaries");
    StyleUtil.setPlaceHolder(shortcut2, "corrections");

    Window.addResizeHandler(e -> {
      update();
    });
  }

  private void update() {
    svgTarget.setInnerHTML(svgTarget.getInnerHTML());

    final NodeList<Node> lst = svgTarget.getChildNodes();
    for (int i = 0; i < lst.getLength(); i++) {
      final Node node = lst.getItem(i);
      final Element g = node.cast();
      final String areaKey = g.getId();
      final NatureArea area = areas.get(areaKey);

      if (area != null) {
        points.put(SVG_AREA_ID_PREFIX + area.getId(), g);
      }

      Event.sinkEvents(g, Event.ONCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER);
      Event.setEventListener(g, e -> {
        if (e.getTypeInt() == Event.ONMOUSEOUT) {
          dehighlight(g);
        }

        if (e.getTypeInt() == Event.ONMOUSEOVER) {
          highlight(g, area);
        }

        if (e.getTypeInt() == Event.ONCLICK) {
          click(area);
        }
      });
    }
  }

  public void setNatureAreas(final List<NatureArea> areas) {
    if (areas == null) {
      return;
    }

    debugMinX = Integer.MAX_VALUE;
    debugMinY = Integer.MAX_VALUE;
    debugMaxX = Integer.MIN_VALUE;
    debugMaxY = Integer.MIN_VALUE;

    this.areasRaw = areas;
    svgTarget.setInnerHTML("");
    areas.forEach(this::draw);
    update();

    correctMinXField.setValue(String.valueOf((int) debugMinX));
    correctMinYField.setValue(String.valueOf((int) debugMinY));
    correctMaxXField.setValue(String.valueOf((int) debugMaxX));
    correctMaxYField.setValue(String.valueOf((int) debugMaxY));

    final int[] boundaryInts = new int[] { (int) debugMinX, (int) debugMinY, (int) debugMaxX, (int) debugMaxY };
    final Double[] correctionDoubles = new Double[] { correctTransformX, correctTransformY, correctTranslateX, correctTranslateY };
    shortcut.setValue(Arrays.stream(boundaryInts)
        .mapToObj(v -> String.valueOf(v))
        .collect(Collectors.joining(", ")));
    shortcut2.setValue(Arrays.stream(correctionDoubles)
        .map(v -> String.valueOf(v))
        .collect(Collectors.joining(", ")));
  }

  private double getWidth() {
    return maxX - minX;
  }

  private double getHeight() {
    return maxY - minY;
  }

  private double debugMinX = Integer.MAX_VALUE;
  private double debugMinY = Integer.MAX_VALUE;
  private double debugMaxX = Integer.MIN_VALUE;
  private double debugMaxY = Integer.MIN_VALUE;

  private Consumer<NatureArea> selectConsumer;

  public void draw(final NatureArea area) {
    final String cent = area.getCenterOfMass();
    final String[] parts = cent.substring(cent.indexOf("(") + 1, cent.indexOf(")")).split(" ", 2);

    final double initX = Double.parseDouble(parts[0]);
    final double initY = Double.parseDouble(parts[1]);

    debugMinX = Math.min(debugMinX, initX);
    debugMinY = Math.min(debugMinY, initY);
    debugMaxX = Math.max(debugMaxX, initX);
    debugMaxY = Math.max(debugMaxY, initY);

    final double x = (initX - minX) / getWidth() * svgWidth * correctTransformX + correctTranslateX;
    final double y = svgHeight - (initY - minY) / getHeight() * svgHeight * correctTransformY + correctTranslateY;

    final String castX = String.valueOf(x);
    final String castY = String.valueOf(y);

    final Element g = DOM.createElement("g");
    final Element hit = DOM.createElement("circle");
    hit.setClassName(style.hitbox());
    hit.setAttribute("cx", castX);
    hit.setAttribute("cy", castY);
    hit.setAttribute("r", "13");
    final Element out = DOM.createElement("circle");
    out.setClassName(style.outer());
    out.setAttribute("cx", castX);
    out.setAttribute("cy", castY);
    out.setAttribute("r", "9");
    final Element pnt = DOM.createElement("circle");
    pnt.setClassName(style.inner());
    pnt.setAttribute("cx", castX);
    pnt.setAttribute("cy", castY);
    pnt.setAttribute("r", "5");
    g.setId(SVG_AREA_ID_PREFIX + area.getId());
    g.appendChild(out);
    g.appendChild(pnt);
    g.appendChild(hit);

    areas.put(SVG_AREA_ID_PREFIX + area.getId(), area);

    svgTarget.appendChild(g);
  }

  public void highlight(final NatureArea area) {
    Optional.ofNullable(points.get(SVG_AREA_ID_PREFIX + area.getId()))
        .ifPresent(pnt -> highlight(pnt, area));
  }

  public void clearHighlight(final NatureArea area) {
    Optional.ofNullable(points.get(SVG_AREA_ID_PREFIX + area.getId()))
        .ifPresent(pnt -> dehighlight(pnt));
  }

  private void highlight(final Element pnt, final NatureArea area) {
    dehighlight(pnt);

    final SimpleHoverBox box = new SimpleHoverBox(area.getName());
    Document.get().getBody().appendChild(box.getElement());
    highlights.put(pnt.getId(), box);

    final SVGClassString clazz = Js.uncheckedCast(pnt.getClassName());
    clazz.baseVal = String.join(" ", clazz.baseVal, style.highlight());

    Scheduler.get().scheduleDeferred(() -> {
      if (!highlights.containsKey(pnt.getId())) {
        return;
      }

      HoverSelectionUtil.attachTop(box, pnt, 20);
    });
  }

  private void dehighlight(final Element pnt) {
    final SVGClassString str = Js.uncheckedCast(pnt.getClassName());
    if (str.baseVal.contains(style.highlight())) {
      str.baseVal = str.baseVal.replaceAll(style.highlight(), "").trim();
    }

    Optional.ofNullable(highlights.remove(pnt.getId()))
        .ifPresent(v -> {
          v.destroyBottom();
          Scheduler.get().scheduleFixedDelay(() -> {
            if (v.getElement().getParentElement() != null) {
              v.destroy();
            }
            return false;
          }, 3500);
        });
  }

  @Override
  protected void onUnload() {
    points.values().forEach(this::dehighlight);
  }

  private void click(final NatureArea area) {
    if (selectConsumer != null) {
      selectConsumer.accept(area);
    }
  }

  public void setSvgWidth(final double svgWidth) {
    this.svgWidth = svgWidth;
    updateViewBox();
  }

  public void setSvgHeight(final double svgHeight) {
    this.svgHeight = svgHeight;
    updateViewBox();
  }

  private void updateViewBox() {
    svgTarget.setAttribute("viewBox", "0 0 " + svgWidth + " " + svgHeight);
    update();
  }

  private void redraw() {
    setNatureAreas(areasRaw);
  }

  public void setCorrectTransformX(final double correctTransformX) {
    this.correctTransformX = correctTransformX;
    redraw();
  }

  public void setCorrectTransformY(final double correctTransformY) {
    this.correctTransformY = correctTransformY;
    redraw();
  }

  public void setCorrectTranslateY(final double correctTranslateY) {
    this.correctTranslateY = correctTranslateY;
    redraw();
  }

  public void setCorrectTranslateX(final double correctTranslateX) {
    this.correctTranslateX = correctTranslateX;
    redraw();
  }

  public void setCorrections(final Double[] corrections) {
    if (corrections == null || corrections.length < 3) {
      setDebug(true);
      return;
    }

    setSvgWidth(corrections[1] * corrections[0]);
    setSvgHeight(corrections[2] * corrections[0]);

    if (corrections == null || corrections.length < 7) {
      setDebug(true);
      return;
    }

    setMinX(corrections[3]);
    setMinY(corrections[4]);
    setMaxX(corrections[5]);
    setMaxY(corrections[6]);

    if (corrections.length < 11) {
      setDebug(true);
      return;
    }

    setCorrectTransformX(corrections[7]);
    setCorrectTransformY(corrections[8]);
    setCorrectTranslateX(corrections[9]);
    setCorrectTranslateY(corrections[10]);

//    if (Window.Location.getHost().startsWith("dev.aerius.nl")|| Window.Location.getHost().startsWith("localhost")) {
    if (Window.Location.getHost().startsWith("dev.aerius.nl")) {
      setDebug(true);
    } else {
      setDebug(false);
    }
  }

  @UiHandler("correctTransformXField")
  public void onCorrectTransformXKeyUp(final KeyUpEvent e) {
    double val = tryGetOrElse(correctTransformXField, correctTransformX);

    if (e.getNativeKeyCode() == KeyCodes.KEY_UP) {
      val += 0.01;
    } else if (e.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
      val -= 0.01;
    }

    correctTransformXField.setValue(String.valueOf((int) (val * 100) / 100D));
    setCorrectTransformX((int) (val * 100) / 100D);
  }

  @UiHandler("correctTransformYField")
  public void onCorrectTransformYKeyUp(final KeyUpEvent e) {
    double val = tryGetOrElse(correctTransformYField, correctTransformY);

    if (e.getNativeKeyCode() == KeyCodes.KEY_UP) {
      val += 0.01;
    } else if (e.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
      val -= 0.01;
    }

    correctTransformYField.setValue(String.valueOf((int) (val * 100) / 100D));
    setCorrectTransformY((int) (val * 100) / 100D);
  }

  @UiHandler("correctTranslateXField")
  public void onCorrectTranslateXKeyUp(final KeyUpEvent e) {
    double val = tryGetOrElse(correctTranslateXField, correctTranslateX);

    if (e.getNativeKeyCode() == KeyCodes.KEY_UP) {
      val += 1;
    } else if (e.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
      val -= 1;
    }

    correctTranslateXField.setValue(String.valueOf((int) (val * 100) / 100D));
    setCorrectTranslateX((int) (val * 100) / 100D);
  }

  @UiHandler("correctTranslateYField")
  public void onCorrectTranslateYKeyUp(final KeyUpEvent e) {
    double val = tryGetOrElse(correctTranslateYField, correctTranslateY);

    if (e.getNativeKeyCode() == KeyCodes.KEY_UP) {
      val += 1;
    } else if (e.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
      val -= 1;
    }

    correctTranslateYField.setValue(String.valueOf((int) (val * 100) / 100D));
    setCorrectTranslateY((int) (val * 100) / 100D);
  }

  public double tryGetOrElse(final TextBox box, final double or) {
    try {
      return Double.parseDouble(box.getValue());
    } catch (final NumberFormatException e) {
      return or;
    }
  }

  private void setDebug(final boolean b) {
    debugPanel.setVisible(b);
  }

  public void setMinX(final double minX) {
    this.minX = minX;
    redraw();
  }

  public void setMaxX(final double maxX) {
    this.maxX = maxX;
    redraw();
  }

  public void setMinY(final double minY) {
    this.minY = minY;
    redraw();
  }

  public void setMaxY(final double maxY) {
    this.maxY = maxY;
    redraw();
  }

  @JsType(isNative = true, namespace = "GLOBAL")
  private static class SVGClassString {
    @JsProperty String baseVal;
  }

  public void onAreaSelect(final Consumer<NatureArea> selectConsumer) {
    this.selectConsumer = selectConsumer;
  }
}
