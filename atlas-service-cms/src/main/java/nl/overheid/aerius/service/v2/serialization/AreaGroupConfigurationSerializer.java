package nl.overheid.aerius.service.v2.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.machinezoo.noexception.Exceptions;

public class AreaGroupConfigurationSerializer extends JsonSerializer<AreaGroupConfiguration> {
  @Override
  public void serialize(final AreaGroupConfiguration conf, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
      throws IOException, JsonProcessingException {

    jsonGenerator.writeStartObject();
    jsonGenerator.writeStringField("version", "2");

    jsonGenerator.writeArrayFieldStart("areas");

    conf.forEach(Exceptions.sneak().fromBiConsumer((k, v) -> {
      jsonGenerator.writeStartObject();

      jsonGenerator.writeStringField("id", k.getId());
      jsonGenerator.writeStringField("centroid", k.getCenterOfMass());
      jsonGenerator.writeStringField("name", k.getName());
      jsonGenerator.writeStringField("authority", k.getAuthority().getName());
      jsonGenerator.writeArrayFieldStart("provinces");

      v.forEach(Exceptions.sneak().consumer(a -> jsonGenerator.writeString(a.getName())));

      jsonGenerator.writeEndArray();

      jsonGenerator.writeEndObject();
    }));

    jsonGenerator.writeEndArray();

    jsonGenerator.writeEndObject();
  }
}
