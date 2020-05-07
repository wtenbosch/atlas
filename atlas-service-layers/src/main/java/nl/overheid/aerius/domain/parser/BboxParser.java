package nl.overheid.aerius.domain.parser;

import java.util.Optional;

import com.mashape.unirest.http.JsonNode;

import nl.overheid.aerius.domain.BboxContainer;

public class BboxParser {
  public static BboxContainer parseBbox(final Optional<JsonNode> json) {
    final BboxContainer bbox = new BboxContainer();

    json.ifPresent(v -> {
      bbox.setBbox(v.getObject().getJSONObject("natura2000AreaInfo").getString("extent"));
    });

    json.ifPresent(v -> {
      bbox.setTitle(v.getObject().getString("name"));
    });

    return bbox;
  }
}
