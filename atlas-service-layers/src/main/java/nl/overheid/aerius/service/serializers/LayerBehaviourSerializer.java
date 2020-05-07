package nl.overheid.aerius.service.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import nl.overheid.aerius.domain.LayerBehaviour;

public class LayerBehaviourSerializer extends JsonSerializer<LayerBehaviour> {
  @Override
  public void serialize(final LayerBehaviour conf, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
      throws IOException, JsonProcessingException {
    jsonGenerator.writeStartObject();

    jsonGenerator.writeStringField("bundle", conf.getBundleGroup());
    jsonGenerator.writeStringField("cluster", conf.getClusterGroup());
    jsonGenerator.writeStringField("friendly", conf.getFriendlyName());

    jsonGenerator.writeEndObject();
  }
}
