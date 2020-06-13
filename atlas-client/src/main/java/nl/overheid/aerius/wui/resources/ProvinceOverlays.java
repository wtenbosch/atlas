package nl.overheid.aerius.wui.resources;

import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.DataResource.MimeType;

public interface ProvinceOverlays {
  @Source("images/province/overlays/Brabant.svg")
  @MimeType("image/svg+xml")
  DataResource overlayBrabant();

  @Source("images/province/overlays/Drenthe.svg")
  @MimeType("image/svg+xml")
  DataResource overlayDrenthe();

  @Source("images/province/overlays/Flevoland.svg")
  @MimeType("image/svg+xml")
  DataResource overlayFlevoland();

  @Source("images/province/overlays/Friesland.svg")
  @MimeType("image/svg+xml")
  DataResource overlayFriesland();

  @Source("images/province/overlays/Gelderland.svg")
  @MimeType("image/svg+xml")
  DataResource overlayGelderland();

  @Source("images/province/overlays/Groningen.svg")
  @MimeType("image/svg+xml")
  DataResource overlayGroningen();

  @Source("images/province/overlays/Limburg.svg")
  @MimeType("image/svg+xml")
  DataResource overlayLimburg();

  @Source("images/province/overlays/Noord-Holland.svg")
  @MimeType("image/svg+xml")
  DataResource overlayNoordHolland();

  @Source("images/province/overlays/Overijssel.svg")
  @MimeType("image/svg+xml")
  DataResource overlayOverijssel();

  @Source("images/province/overlays/Utrecht.svg")
  @MimeType("image/svg+xml")
  DataResource overlayUtrecht();

  @Source("images/province/overlays/Zeeland.svg")
  @MimeType("image/svg+xml")
  DataResource overlayZeeland();

  @Source("images/province/overlays/Zuid-Holland.svg")
  @MimeType("image/svg+xml")
  DataResource overlayZuidHolland();

  @Source("images/province/overlays/Nederland.svg")
  @MimeType("image/svg+xml")
  DataResource overlayRijksOverheid();

  @Source("images/province/overlays/Nederland-bare.svg")
  @MimeType("image/svg+xml")
  DataResource overlayRijksOverheidBare();
}
