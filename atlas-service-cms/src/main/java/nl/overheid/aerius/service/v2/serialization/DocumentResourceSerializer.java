package nl.overheid.aerius.service.v2.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import nl.overheid.aerius.shared.domain.DocumentResource;

public class DocumentResourceSerializer extends JsonSerializer<DocumentResource> {
  @Override
  public void serialize(final DocumentResource res, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
      throws IOException, JsonProcessingException {
    jsonGenerator.writeStartObject();

    jsonGenerator.writeStringField("url", res.url());
    jsonGenerator.writeStringField("name", res.name());
    jsonGenerator.writeObjectField("date", res.date());

    jsonGenerator.writeEndObject();
  }
}
