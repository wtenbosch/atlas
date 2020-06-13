package nl.overheid.aerius.service.domain.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;

import nl.overheid.aerius.shared.domain.ServiceSelector;
import nl.overheid.aerius.shared.domain.ServiceSelector.Builder;

public class Nature2000AreaParser {
  public static List<ServiceSelector> parseSelectors(final Optional<JsonNode> json) {
    final List<ServiceSelector> selectors = new ArrayList<>();

    json.ifPresent(v -> {
      final JSONArray habitats = v.getArray();
      for (int i = 0; i < habitats.length(); i++) {
        final JSONObject habitat = habitats.getJSONObject(i);

        final Builder bldr = ServiceSelector.builder()
            .value(habitat.getString("code"))
            .name(habitat.getString("name"));

        selectors.add(bldr.build());
      }
    });

    return selectors;
  }
}
