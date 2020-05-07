package nl.overheid.aerius.service.domain.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;

import nl.overheid.aerius.service.domain.ServiceSelector;

public class DesignatedHabitatTypesParser {
  public static List<ServiceSelector> parseSelectors(final Optional<JsonNode> json) {
    final List<ServiceSelector> selectors = new ArrayList<>();

    json.ifPresent(v -> {
      final JSONArray habitats = v.getArray();
      for (int i = 0; i < habitats.length(); i++) {
        final JSONObject habitat = habitats.getJSONObject(i);

        if (canSkip(habitat.getJSONObject("goalHabitatTypeInfo"))) {
          continue;
        }

        final ServiceSelector selector = new ServiceSelector();
        final String code = habitat.getString("code");
        final String name = habitat.getString("name");

        selector.setName(String.format("%s - %s", code, name));
        selector.setValue(code);

        final Map<String, String> tags = new HashMap<>();
        tags.put("id", String.valueOf(habitat.getInt("id")));
        tags.put("natura2000AreaCode", habitat.getString("natura2000AreaCode"));

        selector.setTags(tags);

        selectors.add(selector);
      }
    });

    return selectors;
  }

  private static boolean canSkip(final JSONObject habitat) {
    final String qualityGoal = habitat.getString("qualityGoal");
    final String extentGoal = habitat.getString("extentGoal");

    return "none".equals(qualityGoal) && "none".equals(extentGoal);
  }
}
