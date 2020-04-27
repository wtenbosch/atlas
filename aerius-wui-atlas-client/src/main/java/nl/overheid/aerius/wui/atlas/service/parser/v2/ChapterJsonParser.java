package nl.overheid.aerius.wui.atlas.service.parser.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.ChapterIcon;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.shared.domain.SelectorResource;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson;

public final class ChapterJsonParser extends CommonJson {
  public static Map<String, Chapter> parse(final JSONArrayHandle chapters) {
    final Map<String, Chapter> map = new LinkedHashMap<>();

    chapters.forEach(chapterJson -> {
      final List<SelectorResource> selectables = new ArrayList<>();
      final JSONObjectHandle selectablesJson = chapterJson.getObject("selectables");
      selectablesJson.keySet().forEach(v -> {
        selectables.add(SelectorResource.builder()
            .type(v)
            .url(selectablesJson.getString(v))
            .build());
      });

      final Map<PanelNames, PanelContent> panels = new HashMap<>();
      final JSONObjectHandle panelsJson = chapterJson.getObject("panels");
      panelsJson.keySet().forEach(v -> {
        panels.put(PanelNames.fromName(v), PanelContentJsonParser.parse(panelsJson.getObject(v)));
      });

      final Chapter chapter = Chapter.builder()
          .uid(chapterJson.getString("uid"))
          .title(chapterJson.getString("name"))
          .icon(ChapterIcon.valueOf(chapterJson.getString("icon")))
          .sortId(chapterJson.getInteger("sortId"))
          .selectables(selectables)
          .panels(panels)
          .build();

      map.put(chapter.uid(), chapter);
    });

    return map;
  }
}
