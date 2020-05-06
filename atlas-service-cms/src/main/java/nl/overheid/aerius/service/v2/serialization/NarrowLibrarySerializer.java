package nl.overheid.aerius.service.v2.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.machinezoo.noexception.Exceptions;

import nl.overheid.aerius.shared.domain.NarrowLibrary;

public class NarrowLibrarySerializer extends JsonSerializer<NarrowLibrary> {
  @Override
  public void serialize(final NarrowLibrary library, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
      throws IOException, JsonProcessingException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeNumberField("version", 2);

    jsonGenerator.writeArrayFieldStart("library");
    library.values().forEach(Exceptions.sneak().consumer(v -> {
      jsonGenerator.writeObject(v);
    }));
    jsonGenerator.writeEndArray();

    if (library.getDocuments() != null) {
      jsonGenerator.writeArrayFieldStart("documents");
      library.getDocuments().forEach(Exceptions.sneak().consumer(doc -> {
        jsonGenerator.writeObject(doc);
      }));
      jsonGenerator.writeEndArray();
    }

    jsonGenerator.writeEndObject();

  }
}
