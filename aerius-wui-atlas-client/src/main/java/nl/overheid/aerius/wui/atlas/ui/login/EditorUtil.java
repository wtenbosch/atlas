package nl.overheid.aerius.wui.atlas.ui.login;

import java.util.Collection;
import java.util.Iterator;

import nl.overheid.aerius.shared.domain.Selector;

public final class EditorUtil {
  private static final String BASE_ADMIN_ADD_EDITABLE_TEXT_ADD_LINK = "http://test-monitor.aerius.nl/node/add/editable_content?";
  private static final String BASE_ADMIN_ADD_DOCUMENT_RESOURCE_ADD_LINK = "https://test-monitor.aerius.nl/node/add/documents?field_panel_ref[]=";

  private static final String BASE_ADMIN_EDIT_NODE_LINK = "http://test-monitor.aerius.nl";
  private static final String BASE_ADMIN_EDIT_RESOURCE_NODE_LINK = "https://test-monitor.aerius.nl/nl/node/";

  private EditorUtil() {}

  public static String formatAddEditableTextLink(final double addNode, final Collection<Selector> values) {
    final StringBuilder bldr = new StringBuilder(BASE_ADMIN_ADD_EDITABLE_TEXT_ADD_LINK);
    final Iterator<Selector> iterator = values.iterator();
    for (int i = 0; i < values.size(); i++) {
      final Selector selector = iterator.next();
      bldr.append("field_selectors[");
      bldr.append(i);
      bldr.append("][key]=");
      bldr.append(selector.getType());
      bldr.append("&field_selectors[");
      bldr.append(i);
      bldr.append("][value]=");
      bldr.append(selector.getValue().get());
      bldr.append("&");
    }

    bldr.append("field_panel_ref[0]=");
    bldr.append(addNode);

    return bldr.toString();
  }

  public static String formatEditEditableTextLink(final String editNode) {
    return BASE_ADMIN_EDIT_NODE_LINK + editNode + "/edit";
  }

  public static String formatEditResourceLink(final String editNode) {
    return BASE_ADMIN_EDIT_RESOURCE_NODE_LINK + editNode + "/edit";
  }

  public static String formatAddResourceLink(final String addDocument) {
    return BASE_ADMIN_ADD_DOCUMENT_RESOURCE_ADD_LINK + addDocument;
  }
}
