package nl.aerius.service.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import nl.aerius.shared.domain.SearchSuggestion;

public class SearchSuggestionSerializer extends JsonSerializer<SearchSuggestion> {
  @Override
  public void serialize(final SearchSuggestion sug, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
      throws IOException, JsonProcessingException {
    jsonGenerator.writeStartObject();

    jsonGenerator.writeStringField("type", sug.type().name());
    jsonGenerator.writeStringField("title", sug.title());

    if (sug.payload() != null) {
      jsonGenerator.writeStringField("payload", String.valueOf(sug.payload()));
    }
    if (sug.extent() != null) {
      jsonGenerator.writeStringField("extent", sug.extent());
    }

    jsonGenerator.writeEndObject();
  }
}
