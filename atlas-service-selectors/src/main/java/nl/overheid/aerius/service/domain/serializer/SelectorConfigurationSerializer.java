package nl.overheid.aerius.service.domain.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.machinezoo.noexception.Exceptions;

import nl.overheid.aerius.service.domain.ServiceSelectorConfiguration;

public class SelectorConfigurationSerializer extends JsonSerializer<ServiceSelectorConfiguration> {
  @Override
  public void serialize(final ServiceSelectorConfiguration selectorOptions, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
      throws IOException, JsonProcessingException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeNumberField("version", 1);
    jsonGenerator.writeStringField("type", selectorOptions.getType().getName());
    jsonGenerator.writeStringField("title", selectorOptions.getTitle());
    jsonGenerator.writeStringField("description", selectorOptions.getDescription());
    jsonGenerator.writeArrayFieldStart("options");
    selectorOptions.getSelectors().forEach(Exceptions.sneak().consumer(s -> jsonGenerator.writeObject(s)));
    jsonGenerator.writeEndArray();

    if (selectorOptions.getMeta() != null) {
      jsonGenerator.writeObjectFieldStart("meta");
      selectorOptions.getMeta().entrySet().forEach(Exceptions.sneak().consumer(s -> {
        jsonGenerator.writeObjectField(s.getKey(), s.getValue());
      }));

      jsonGenerator.writeStringField("type", selectorOptions.getType().name());

      jsonGenerator.writeEndObject();
    }

    jsonGenerator.writeEndObject();
  }
}
