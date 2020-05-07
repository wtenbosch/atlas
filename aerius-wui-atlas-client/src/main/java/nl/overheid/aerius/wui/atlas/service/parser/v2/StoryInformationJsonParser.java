package nl.overheid.aerius.wui.atlas.service.parser.v2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nl.overheid.aerius.shared.domain.StoryIcon;
import nl.overheid.aerius.shared.domain.StoryInformation;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson;
import nl.overheid.aerius.wui.atlas.service.parser.JSONObjectHandle;
import nl.overheid.aerius.wui.dev.GWTProd;

public final class StoryInformationJsonParser extends CommonJson {
  private StoryInformationJsonParser() {}

  public static StoryInformation parse(final JSONObjectHandle storyJson) {
    final StoryInformation info = StoryInformation.builder()
        .uid(storyJson.getString("uid"))
        .name(storyJson.getString("name"))
        .icon(StoryIcon.valueOf(storyJson.getString("icon")))
        .orderId(storyJson.getInteger("orderId"))
        .changedDate(new Date(storyJson.getLong("changedDate")))
        .creationDate(new Date(storyJson.getLong("creationDate")))
        .properties(storyJson.getObjectOptional("properties")
            .map(v -> StoryInformationJsonParser.parseProperties(v))
            .orElseGet(() -> new HashMap<>()))
        .build();

    return info;
  }

  private static Map<String, String> parseProperties(final JSONObjectHandle propertiesJson) {
    final Map<String, String> properties = new HashMap<>();
    propertiesJson.forEach((k, v) -> {
      if (v.isString()) {
        properties.put(k, v.asString());
      } else {
        GWTProd.warn("Could not parse property: " + k);
      }
    });

    return properties;
  }
}
