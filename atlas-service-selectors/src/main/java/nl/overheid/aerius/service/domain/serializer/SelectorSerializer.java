package nl.overheid.aerius.service.domain.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.machinezoo.noexception.Exceptions;

import nl.overheid.aerius.service.domain.ServiceSelector;

public class SelectorSerializer extends JsonSerializer<ServiceSelector> {
  @Override
  public void serialize(final ServiceSelector item, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
      throws IOException, JsonProcessingException {
    jsonGenerator.writeStartObject();

    jsonGenerator.writeStringField("name", item.getName());
    jsonGenerator.writeStringField("value", item.getValue());

    jsonGenerator.writeObjectFieldStart("tags");
    if (item.getTags() != null) {
      item.getTags().forEach(Exceptions.sneak().fromBiConsumer((k, v) -> jsonGenerator.writeStringField(k, v)));
    }
    jsonGenerator.writeEndObject();

    jsonGenerator.writeEndObject();
  }
}
