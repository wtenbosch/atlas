package nl.overheid.aerius.wui.atlas.service.parser.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson;
import nl.overheid.aerius.wui.atlas.service.parser.JSONArrayHandle;
import nl.overheid.aerius.wui.atlas.service.parser.JSONObjectHandle;
import nl.overheid.aerius.wui.atlas.service.parser.JSONValueHandle;

public final class PanelContentJsonParser extends CommonJson {
  public static PanelContent parse(final JSONObjectHandle panel) {
    final Map<String, Object> properties = parseProperties(panel.getObject("properties"));

    return PanelContent.builder()
        .properties(properties)
        .selectables(panel.getStringArray("selectables"))
        .build();
  }

  private static Map<String, Object> parseProperties(final JSONObjectHandle propertiesJson) {
    final Map<String, Object> properties = new HashMap<>();
    propertiesJson.forEach((k, v) -> {
      properties.put(k, parseObject(v));
    });
    return properties;
  }

  private static Object parseObject(final JSONValueHandle v) {
    if (v.isString()) {
      return v.asString();
    } else if (v.isNumber()) {
      return v.asNumber();
    } else if (v.isObject()) {
      return parseProperties(v.asObjectHandle());
    } else if (v.isArray()) {
      return parseList(v.asArray());
    } else {
      return null;
    }
  }

  private static List<Object> parseList(final JSONArrayHandle arrayJson) {
    final List<Object> lst = new ArrayList<>();
    arrayJson.forEachValue(v -> lst.add(parseObject(v)));
    return lst;
  }
}
