package nl.overheid.aerius.wui.atlas.service.parser.v1;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import nl.overheid.aerius.shared.domain.DatasetConfiguration;
import nl.overheid.aerius.shared.domain.StoryFragment;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson;
import nl.overheid.aerius.wui.atlas.service.parser.JSONArrayHandle;
import nl.overheid.aerius.wui.atlas.util.ViewModeUtil;
import nl.overheid.aerius.wui.dev.GWTProd;

public class StoryFragmentJsonParser extends CommonJson {
  public static Map<DatasetConfiguration, StoryFragment> parse(final JSONArrayHandle fragments) {
    final HashMap<DatasetConfiguration, StoryFragment> map = new LinkedHashMap<>();
    fragments.forEach(fragmentJson -> {
      if (fragmentJson == null) {
        GWTProd.warn("Encountered unexpected null element (story fragment)");
        return;
      }

      final String viewMode = fragmentJson.getString("view_mode");

      final String datasetLabel = fragmentJson.getObject("dataset").getObject("entity").getString("label");
      final String datasetCode = fragmentJson.getObject("dataset").getObject("entity").getString("dataset_code");

      final StoryFragment fragment = StoryFragment.builder()
          .viewMode(viewMode)
          .panels(ViewModeUtil.derivePanelConfiguration(viewMode))
          .dataset(DatasetConfiguration.builder()
              .code(datasetCode)
              .build()
              .label(datasetLabel))
          .chapters(ChapterJsonParser.parse(fragmentJson.getObject("chapters").getArray("entities")))
          .build();

      map.put(fragment.dataset(), fragment);
    });

    return map;
  }
}
