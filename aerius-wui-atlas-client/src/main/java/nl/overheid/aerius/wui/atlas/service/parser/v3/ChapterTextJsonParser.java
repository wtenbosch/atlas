package nl.overheid.aerius.wui.atlas.service.parser.v3;

import nl.overheid.aerius.wui.atlas.service.parser.CommonJson.JSONObjectHandle;

public class ChapterTextJsonParser {
  public static String parse(final JSONObjectHandle chapterTextJson) {
    return chapterTextJson.getString("text");
  }
}
