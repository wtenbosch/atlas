package nl.overheid.aerius.wui.atlas.service.parser.v1;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.shared.domain.StoryIcon;
import nl.overheid.aerius.shared.domain.StoryInformation;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.dev.GWTProd;

public final class StoryJsonParser extends CommonJson {
  private StoryJsonParser() {}

  public static Story parse(final JSONObjectHandle object) {
    final JSONObjectHandle storyJson = object.getObject("stories").getArray("story").getObject(0);

    final Map<String, String> props = storyJson.getObjectOptional("properties")
        .map(StoryJsonParser::parseProperties)
        .orElseGet(HashMap::new);

    storyJson.getArrayOptional("custom_properties").ifPresent(propsJson -> {
      injectCustomProperties(propsJson, props);
    });

    final Story.Builder bldr = Story.builder()
        .info(StoryInformation.builder()
            .uid(storyJson.getString("story_id"))
            .name(storyJson.getString("name"))
            .icon(StoryIcon.valueOf(storyJson.getString("icon")))
            .changedDate(new Date(storyJson.getLong("story_changed_date")))
            .creationDate(new Date(storyJson.getLong("story_changed_date")))
            .properties(props)
            .build())
        .fragments(StoryFragmentJsonParser.parse(storyJson.getObject("story_fragments").getArray("entities")));

    final Story story = bldr.build();

    UglyBoilerPlate.injectPriorityProjectCode(story, storyJson);

    return story;
  }

  private static void injectCustomProperties(final JSONArrayHandle propsJson, final Map<String, String> properties) {
    for (int i = 0; i < propsJson.size(); i++) {
      final JSONObjectHandle prop = propsJson.getObject(i);

      properties.computeIfAbsent(prop.getString("key"), v -> prop.getString("value"));
    }
  }

  private static Map<String, String> parseProperties(final JSONObjectHandle propertiesJson) {
    final Map<String, String> fragmentProperties = new HashMap<String, String>();

    for (final String key : propertiesJson.keySet()) {
      parseProperty(key, fragmentProperties, propertiesJson.get(key));
    }

    return fragmentProperties;
  }

  private static void parseProperty(final String prefix, final Map<String, String> map, final JSONValueHandle property) {
    // Process inner properties first
    final JSONObjectHandle objectValue = property.asObjectHandle();
    if (objectValue.getInner() != null) {
      for (final String key : objectValue.keySet()) {
        parseProperty(key, map, objectValue.get(key));
        parseProperty(prefix + "." + key, map, objectValue.get(key));
      }
    }

    //
    if (property.isString()) {
      final String previous = map.put(prefix, property.asString());
      if (previous != null) {
        GWTProd.warn("Story property (" + prefix + ") has been overridden by new value. Old: " + previous + " New: " + property.asString());
      }
      return;
    }
  }
}
