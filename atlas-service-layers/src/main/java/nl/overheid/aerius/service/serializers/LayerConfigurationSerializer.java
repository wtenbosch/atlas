package nl.overheid.aerius.service.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.machinezoo.noexception.Exceptions;

import nl.overheid.aerius.domain.LayerConfiguration;

public class LayerConfigurationSerializer extends JsonSerializer<LayerConfiguration> {
  @Override
  public void serialize(final LayerConfiguration conf, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
      throws IOException, JsonProcessingException {
    jsonGenerator.writeStringField("version", "1");

    jsonGenerator.writeStringField("type", conf.type().name());
    jsonGenerator.writeStringField("name", conf.name());
    jsonGenerator.writeStringField("title", conf.title());

    jsonGenerator.writeBooleanField("visible", conf.visible());
    jsonGenerator.writeNumberField("opacity", conf.opacity());

    if (conf.selectables() != null) {
      jsonGenerator.writeArrayFieldStart("selectables");
      conf.selectables().forEach(Exceptions.sneak().consumer(v -> jsonGenerator.writeString(v)));
      jsonGenerator.writeEndArray();
    }

    jsonGenerator.writeObjectField("behaviour", conf.behaviour());

    jsonGenerator.writeObjectField("legend", conf.legend());
  }
}
