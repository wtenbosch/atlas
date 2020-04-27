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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.NatureArea;
import nl.overheid.aerius.wui.atlas.command.NaturaChangeCommand;
import nl.overheid.aerius.wui.widget.EventComposite;

public class ProvinceSelectionGraph extends EventComposite {
  interface ProvinceSelectionGraphEventBinder extends EventBinder<ProvinceSelectionGraph> {}

  private final ProvinceSelectionGraphEventBinder EVENT_BINDER = GWT.create(ProvinceSelectionGraphEventBinder.class);

  private static final ProvinceSelectionGraphUiBinder UI_BINDER = GWT.create(ProvinceSelectionGraphUiBinder.class);

  interface ProvinceSelectionGraphUiBinder extends UiBinder<Widget, ProvinceSelectionGraph> {}

  private static final String HIGHLIGHT = "highlight";

  @UiField Natura2000AreaSelectionGraph natura2000AreaSelectionGraph;

  private Element highlight;

  public interface CustomStyle extends CssResource {
    String highlight();
  }

  @UiField CustomStyle style;

  @UiField Element provincesSVG;

  @UiField Element provinceZeeland;
  @UiField Element provinceUtrecht;
  @UiField Element provinceSouthHolland;
  @UiField Element provinceOverijssel;
  @UiField Element provinceNorthHolland;
  @UiField Element provinceNorthBrabant;
  @UiField Element provinceLimburg;
  @UiField Element provinceGroningen;
  @UiField Element provinceGelderland;
  @UiField Element provinceFriesland;
  @UiField Element provinceFlevoland;
  @UiField Element provinceDrenthe;

  public ProvinceSelectionGraph() {
    initWidget(UI_BINDER.createAndBindUi(this));

    provincesSVG.setAttribute("xmlns", "http://www.w3.org/2000/svg");
    natura2000AreaSelectionGraph.onNaturaSelect(area -> eventBus.fireEvent(new NaturaChangeCommand(area)));
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER, natura2000AreaSelectionGraph);
  }

  private Element getProvinceElement(final AreaGroupType type) {
    Element elem;

    switch (type) {
    case DRENTHE:
      elem = provinceDrenthe;
      break;
    case FLEVOLAND:
      elem = provinceFlevoland;
      break;
    case FRIESLAND:
      elem = provinceFriesland;
      break;
    case GELDERLAND:
      elem = provinceGelderland;
      break;
    case GRONINGEN:
      elem = provinceGroningen;
      break;
    case LIMBURG:
      elem = provinceLimburg;
      break;
    case NOORDBRABANT:
      elem = provinceNorthBrabant;
      break;
    case NOORDHOLLAND:
      elem = provinceNorthHolland;
      break;
    case OVERIJSSEL:
      elem = provinceOverijssel;
      break;
    case UTRECHT:
      elem = provinceUtrecht;
      break;
    case ZEELAND:
      elem = provinceZeeland;
      break;
    case ZUIDHOLLAND:
      elem = provinceSouthHolland;
      break;
    case RIJKSOVERHEID:
      elem = null;
      break;
    default:
      throw new IllegalArgumentException("Unsupported Province Type [ProvinceSelectionGraph].");
    }

    return elem;
  }

  public void highlight(final AreaGroupType ev) {
    final Element neww = getProvinceElement(ev);
    highlight(neww);
  }

  public void highlight(final Element neww) {
    unhighlight(highlight);
    highlight = neww;

    if (highlight != null) {
      final SVGClassString clazz = Js.uncheckedCast(highlight.getClassName());
      clazz.baseVal = String.join(" ", clazz.baseVal, HIGHLIGHT);
    }
  }

  private void unhighlight(final Element elem) {
    if (elem == null) {
      return;
    }

    final SVGClassString str = Js.uncheckedCast(highlight.getClassName());

    if (highlight != null && str.baseVal.contains(HIGHLIGHT)) {
      str.baseVal = str.baseVal.replaceAll(HIGHLIGHT, "").trim();
    }
  }

  public void clearHighlight() {
    if (highlight == null) {
      return;
    }

    final SVGClassString str = Js.uncheckedCast(highlight.getClassName());
    str.baseVal = str.baseVal.replaceAll(HIGHLIGHT, "").trim();
  }

  @JsType(isNative = true, namespace = "GLOBAL")
  private static class SVGClassString {
    @JsProperty String baseVal;
  }

  public void clearHighlight(final NatureArea a) {
    natura2000AreaSelectionGraph.clearHighlight(a);
  }

  public void highlight(final NatureArea a) {
    natura2000AreaSelectionGraph.highlight(a);
  }
}
