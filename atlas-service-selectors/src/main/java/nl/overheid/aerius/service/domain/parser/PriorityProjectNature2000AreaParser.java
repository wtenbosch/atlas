package nl.overheid.aerius.service.domain.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;

import nl.overheid.aerius.shared.domain.ServiceSelector;
import nl.overheid.aerius.shared.domain.ServiceSelector.Builder;

public class PriorityProjectNature2000AreaParser {
  public static List<ServiceSelector> parseSelectors(final Optional<JsonNode> json) {
    final List<ServiceSelector> selectors = new ArrayList<>();

    json.ifPresent(v -> {
      final JSONArray areas = v.getArray();
      for (int i = 0; i < areas.length(); i++) {
        final JSONObject area = areas.getJSONObject(i);

        final Builder bldr = ServiceSelector.builder()
            .value(area.getString("natura2000AreaCode"))
            .name(area.getString("natura2000AreaName"));

        final ServiceSelector build = bldr.build();

        selectors.add(build);
      }
    });

    // Uglily prefer the Solleveld area
    selectors.sort((o1, o2) -> o1.name().contains("Solleveld") ? -1 : o2.name().contains("Solleveld") ? 1 : 0);

    return selectors;
  }
}
