package nl.overheid.aerius.service.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.machinezoo.noexception.Exceptions;

import nl.overheid.aerius.domain.BundledLayerConfiguration;

public class BundledLayerConfigurationSerializer extends JsonSerializer<BundledLayerConfiguration> {
  @Override
  public void serialize(final BundledLayerConfiguration bundle, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
      throws IOException, JsonProcessingException {
    jsonGenerator.writeStartObject();

    jsonGenerator.writeObjectFieldStart("data");

    jsonGenerator.writeArrayFieldStart("layers");
    bundle.getLayers().forEach(Exceptions.sneak().consumer(v -> jsonGenerator.writeObject(v)));
    jsonGenerator.writeEndArray();

    jsonGenerator.writeEndObject();
    jsonGenerator.writeEndObject();
  }
}
