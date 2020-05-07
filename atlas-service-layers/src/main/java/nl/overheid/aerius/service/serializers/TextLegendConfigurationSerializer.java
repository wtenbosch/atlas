package nl.overheid.aerius.service.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

import nl.overheid.aerius.domain.legend.TextLegendConfiguration;

public class TextLegendConfigurationSerializer extends LegendConfigurationSerializer<TextLegendConfiguration> {
  @Override
  public void serialize(final TextLegendConfiguration conf, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
      throws IOException, JsonProcessingException {
    jsonGenerator.writeStartObject();

    super.serialize(conf, jsonGenerator, serializerProvider);

    jsonGenerator.writeStringField("text", conf.getText());

    jsonGenerator.writeEndObject();
  }
}
