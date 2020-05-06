package nl.overheid.aerius.service.v2.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.machinezoo.noexception.Exceptions;

import nl.overheid.aerius.shared.domain.Story;

public class StorySerializer extends StdSerializer<Story> {
  private static final long serialVersionUID = 1L;

  public StorySerializer() {
    super(Story.class);
  }

  @Override
  public void serialize(final Story value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    gen.writeNumberField("version", 2);

    StoryInformationSerializer.writeStoryInformation(value.info(), gen);

    gen.writeArrayFieldStart("fragments");
    value.fragments().values()
        .forEach(Exceptions.sneak().consumer(fragment -> {
          gen.writeObject(fragment);
        }));
    gen.writeEndArray();

    gen.writeEndObject();
  }
}
