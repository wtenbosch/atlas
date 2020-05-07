package nl.overheid.aerius.service.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import nl.overheid.aerius.domain.BboxContainer;

public class BboxContainerSerializer extends JsonSerializer<BboxContainer> {
  @Override
  public void serialize(final BboxContainer box, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
      throws IOException, JsonProcessingException {
    jsonGenerator.writeStartObject();

    jsonGenerator.writeStringField("bbox", box.getBbox());
    jsonGenerator.writeStringField("title", box.getTitle());

    jsonGenerator.writeEndObject();
  }
}
