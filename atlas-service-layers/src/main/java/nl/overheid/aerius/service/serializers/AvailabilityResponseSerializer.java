package nl.overheid.aerius.service.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import nl.overheid.aerius.domain.availability.AvailabilityResponse;

public class AvailabilityResponseSerializer extends JsonSerializer<AvailabilityResponse> {
  @Override
  public void serialize(final AvailabilityResponse box, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
      throws IOException, JsonProcessingException {
    jsonGenerator.writeStartObject();

    jsonGenerator.writeBooleanField("result", box.getResult());

    jsonGenerator.writeEndObject();
  }
}
