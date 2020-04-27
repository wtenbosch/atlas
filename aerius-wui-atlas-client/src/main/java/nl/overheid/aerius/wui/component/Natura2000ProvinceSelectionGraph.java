package nl.overheid.aerius.wui.component;

import static nl.overheid.aerius.shared.domain.AreaGroupType.DRENTHE;
import static nl.overheid.aerius.shared.domain.AreaGroupType.FLEVOLAND;
import static nl.overheid.aerius.shared.domain.AreaGroupType.FRIESLAND;
import static nl.overheid.aerius.shared.domain.AreaGroupType.GELDERLAND;
import static nl.overheid.aerius.shared.domain.AreaGroupType.GRONINGEN;
import static nl.overheid.aerius.shared.domain.AreaGroupType.LIMBURG;
import static nl.overheid.aerius.shared.domain.AreaGroupType.NOORDBRABANT;
import static nl.overheid.aerius.shared.domain.AreaGroupType.NOORDHOLLAND;
import static nl.overheid.aerius.shared.domain.AreaGroupType.OVERIJSSEL;
import static nl.overheid.aerius.shared.domain.AreaGroupType.RIJKSOVERHEID;
import static nl.overheid.aerius.shared.domain.AreaGroupType.UTRECHT;
import static nl.overheid.aerius.shared.domain.AreaGroupType.ZEELAND;
import static nl.overheid.aerius.shared.domain.AreaGroupType.ZUIDHOLLAND;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.NatureArea;
import nl.overheid.aerius.wui.resources.R;

public class Natura2000ProvinceSelectionGraph extends Composite {
  private static final Natura2000ProvinceSelectionGraphUiBinder UI_BINDER = GWT.create(Natura2000ProvinceSelectionGraphUiBinder.class);

  interface Natura2000ProvinceSelectionGraphUiBinder extends UiBinder<Widget, Natura2000ProvinceSelectionGraph> {}

  private static final Map<AreaGroupType, Double[]> C = new HashMap<>();
  static {
    C.put(DRENTHE, new Double[] { 4.5D, 127D, 150D, 213412D, 522267D, 265801D, 577782D, 0.7, 0.6, 100D, -200D, });
    C.put(FLEVOLAND, new Double[] { 4.5D, 127D, 150D, 142831D, 491155D, 152548D, 495424D, 0.2, 0.1, 65D, -228D });
    C.put(FRIESLAND, new Double[] { 4.5D, 127D, 150D, 129829D, 539652D, 215342D, 611550D, 0.72, 0.58, 90D, -260D });
    C.put(GELDERLAND, new Double[] { 4.5D, 127D, 150D, 130712D, 419322D, 251003D, 473160D, 0.85, 0.35, 54D, -298D });
    C.put(GRONINGEN, new Double[] { 4.5D, 127D, 150D, 241625D, 558748D, 271248D, 574380D, 0.32, 0.15, 308D, -213D });
    C.put(LIMBURG, new Double[] { 4.5D, 127D, 150D, 172544D, 309050D, 206498D, 417197D, 0.33, 0.84, 177D, -48D });
    C.put(NOORDBRABANT, new Double[] { 4.5D, 127D, 150D, 76678D, 371217D, 193271D, 418652D, 0.84, 0.3, 56D, -387D });
    C.put(NOORDHOLLAND, new Double[] { 4.5D, 127D, 150D, 98032D, 468524D, 136431D, 566445D, 0.38, 0.82, 137D, -62D });
    C.put(OVERIJSSEL, new Double[] { 4.5D, 127D, 150D, 193192D, 462453D, 267386D, 532772D, 0.71, 0.65, 126D, -184D });
    C.put(UTRECHT, new Double[] { 4.5D, 127D, 150D, 123485D, 440212D, 168590D, 473901D, 0.55, 0.48, 150D, -269D });
    C.put(ZEELAND, new Double[] { 4.5D, 127D, 150D, 14742D, 360017D, 59521D, 414774D, 0.57, 0.59, 75D, -199D });
    C.put(ZUIDHOLLAND, new Double[] { 4.5D, 127D, 150D, 56441D, 419736D, 115097D, 470926D, 0.6, 0.43, 107D, -308D });
    C.put(RIJKSOVERHEID, new Double[] { 4.5D, 127D, 150D, 3808D, 311295D, 230296D, 797802D, 0.81, 1.54, -11D, -14D });
  }

  @UiField Image overlay;
  @UiField Natura2000SVG svgTarget;

  public Natura2000ProvinceSelectionGraph() {
    initWidget(UI_BINDER.createAndBindUi(this));

  }

  public void setAreaGroup(final AreaGroupType group, final List<NatureArea> areas) {
    SafeUri url;

    switch (group) {
    case DRENTHE:
      url = R.images().overlayDrenthe().getSafeUri();
      break;
    case FLEVOLAND:
      url = R.images().overlayFlevoland().getSafeUri();
      break;
    case FRIESLAND:
      url = R.images().overlayFriesland().getSafeUri();
      break;
    case GELDERLAND:
      url = R.images().overlayGelderland().getSafeUri();
      break;
    case GRONINGEN:
      url = R.images().overlayGroningen().getSafeUri();
      break;
    case LIMBURG:
      url = R.images().overlayLimburg().getSafeUri();
      break;
    case NOORDBRABANT:
      url = R.images().overlayBrabant().getSafeUri();
      break;
    case NOORDHOLLAND:
      url = R.images().overlayNoordHolland().getSafeUri();
      break;
    case OVERIJSSEL:
      url = R.images().overlayOverijssel().getSafeUri();
      break;
    case UTRECHT:
      url = R.images().overlayUtrecht().getSafeUri();
      break;
    case ZEELAND:
      url = R.images().overlayZeeland().getSafeUri();
      break;
    case ZUIDHOLLAND:
      url = R.images().overlayZuidHolland().getSafeUri();
      break;
    case RIJKSOVERHEID:
      url = R.images().overlayRijksOverheid().getSafeUri();
      break;
    default:
      return;
    }

    overlay.setUrl(url);

    final Double[] corrections = C.get(group);
    svgTarget.setCorrections(corrections);
    svgTarget.setNatureAreas(areas);
  }

  public void highlight(final NatureArea area) {
    svgTarget.highlight(area);
  }

  public void clearHighlight(final NatureArea area) {
    svgTarget.clearHighlight(area);
  }

  public void onNaturaSelect(final Consumer<NatureArea> consumer) {
    svgTarget.onAreaSelect(consumer);
  }
}
