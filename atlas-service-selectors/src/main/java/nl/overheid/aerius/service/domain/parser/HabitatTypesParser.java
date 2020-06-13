package nl.overheid.aerius.service.domain.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;

import nl.overheid.aerius.shared.domain.ServiceSelector;
import nl.overheid.aerius.shared.domain.ServiceSelector.Builder;

public class HabitatTypesParser {
  public static List<ServiceSelector> parseSelectors(final Optional<JsonNode> json) {
    final List<ServiceSelector> selectors = new ArrayList<>();

    json.ifPresent(v -> {
      final JSONArray habitats = v.getArray();
      for (int i = 0; i < habitats.length(); i++) {
        final JSONObject habitat = habitats.getJSONObject(i);

        final Builder bldr = ServiceSelector.builder()
            .value(habitat.getString("code"))
            .name(String.format("%s - %s", habitat.getString("code"), habitat.getString("name")));

        final HashMap<String, String> tags = new HashMap<>();
        tags.put("id", String.valueOf(habitat.getInt("id")));
        tags.put("natura2000AreaCode", habitat.getString("natura2000AreaCode"));

        bldr.tags(tags);

        selectors.add(bldr.build());
      }
    });

    return selectors;
  }
}
