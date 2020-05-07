package nl.overheid.aerius.service.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import nl.overheid.aerius.domain.legend.LegendConfiguration;

public class LegendConfigurationSerializer<L extends LegendConfiguration> extends JsonSerializer<L> {
  @Override
  public void serialize(final L conf, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
      throws IOException, JsonProcessingException {
    jsonGenerator.writeStringField("type", conf.getType().name());
  }
}
