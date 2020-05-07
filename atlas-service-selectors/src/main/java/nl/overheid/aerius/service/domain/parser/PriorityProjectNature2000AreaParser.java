package nl.overheid.aerius.service.domain.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;

import nl.overheid.aerius.service.domain.ServiceSelector;

public class PriorityProjectNature2000AreaParser {
  public static List<ServiceSelector> parseSelectors(final Optional<JsonNode> json) {
    final List<ServiceSelector> selectors = new ArrayList<>();

    json.ifPresent(v -> {
      final JSONArray areas = v.getArray();
      for (int i = 0; i < areas.length(); i++) {
        final JSONObject area = areas.getJSONObject(i);

        final ServiceSelector selector = new ServiceSelector();
        final String code = area.getString("natura2000AreaCode");
        final String name = area.getString("natura2000AreaName");

        selector.setValue(code);
        selector.setName(name);
        selectors.add(selector);
      }
    });

    // Uglily prefer the Solleveld area
    selectors.sort((o1, o2) -> o1.getName().contains("Solleveld") ? -1 : o2.getName().contains("Solleveld") ? 1 : 0);

    return selectors;
  }
}
