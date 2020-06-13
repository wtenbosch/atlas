package nl.overheid.aerius.domain.parser;

import java.util.Optional;

import com.mashape.unirest.http.JsonNode;

import nl.overheid.aerius.domain.BboxContainer;

public class BboxParser {
  public static BboxContainer parseBbox(final Optional<JsonNode> json) {
    final BboxContainer.Builder bbox = BboxContainer.builder();

    json.ifPresent(v -> {
      bbox.bbox(v.getObject().getJSONObject("natura2000AreaInfo").getString("extent"));
    });

    json.ifPresent(v -> {
      bbox.title(v.getObject().getString("name"));
    });

    return bbox.build();
  }
}
