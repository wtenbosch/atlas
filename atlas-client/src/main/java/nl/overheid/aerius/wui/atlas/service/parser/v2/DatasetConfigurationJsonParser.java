package nl.overheid.aerius.wui.atlas.service.parser.v2;

import nl.overheid.aerius.shared.domain.DatasetConfiguration;
import nl.overheid.aerius.wui.atlas.service.parser.JSONObjectHandle;

public final class DatasetConfigurationJsonParser {
  private DatasetConfigurationJsonParser() {}

  public static DatasetConfiguration parse(final JSONObjectHandle datasetJson) {
    return DatasetConfiguration.builder()
        .code(datasetJson.getString("code"))
        .build()
        .label(datasetJson.getString("label"));
  }
}
