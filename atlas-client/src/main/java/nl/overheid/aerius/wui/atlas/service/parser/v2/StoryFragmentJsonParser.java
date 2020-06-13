package nl.overheid.aerius.wui.atlas.service.parser.v2;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import nl.overheid.aerius.shared.domain.DatasetConfiguration;
import nl.overheid.aerius.shared.domain.StoryFragment;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson;
import nl.overheid.aerius.wui.atlas.service.parser.JSONArrayHandle;
import nl.overheid.aerius.wui.atlas.service.parser.JSONObjectHandle;
import nl.overheid.aerius.wui.atlas.util.ViewModeUtil;

public class StoryFragmentJsonParser extends CommonJson {
  public static Map<DatasetConfiguration, StoryFragment> parse(final JSONArrayHandle fragments) {
    final HashMap<DatasetConfiguration, StoryFragment> map = new LinkedHashMap<>();

    fragments.forEach(fragmentJson -> {
      final JSONObjectHandle datasetJson = fragmentJson.getObject("dataset");

      final String viewMode = fragmentJson.getString("viewMode");

      final StoryFragment fragment = StoryFragment.builder()
          .dataset(DatasetConfigurationJsonParser.parse(datasetJson))
          .viewMode(viewMode)
          .panels(ViewModeUtil.derivePanelConfiguration(viewMode))
          .chapters(ChapterJsonParser.parse(fragmentJson.getArray("chapters")))
          .build();

      map.put(fragment.dataset(), fragment);
    });

    return map;
  }
}
