package nl.overheid.aerius.wui.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.user.client.ui.SimplePanel;

public class SvgUtil {
  public static final SvgUtil I = GWT.create(SvgUtil.class);

  public void setSvg(final SimplePanel widg, final DataResource image) {
    final String[] base64String = image.getSafeUri().asString().split(",");
    final String svg = new String(Base64Util.decode(base64String[base64String.length - 1]));
    widg.getElement().setInnerHTML(svg);
  }

}
