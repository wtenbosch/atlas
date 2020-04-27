package nl.overheid.aerius.wui.atlas.service.parser.v2;

import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson;

public final class StoryJsonParser extends CommonJson {
  private StoryJsonParser() {}

  public static Story parse(final JSONObjectHandle storyJson) {
    return Story.builder()
        .info(StoryInformationJsonParser.parse(storyJson))
        .fragments(StoryFragmentJsonParser.parse(storyJson.getArray("fragments")))
        .build();
  }
}
