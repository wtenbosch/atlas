package nl.overheid.aerius.service.v2.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.machinezoo.noexception.Exceptions;

import nl.overheid.aerius.shared.domain.StoryFragment;

public class StoryFragmentSerializer extends StdSerializer<StoryFragment> {
  private static final long serialVersionUID = 1L;

  public StoryFragmentSerializer() {
    super(StoryFragment.class);
  }

  @Override
  public void serialize(final StoryFragment value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    gen.writeStringField("viewMode", value.viewMode());
    gen.writeObjectFieldStart("dataset");

    gen.writeStringField("label", value.dataset().label());
    gen.writeStringField("code", value.dataset().code());
    gen.writeEndObject();

    gen.writeArrayFieldStart("chapters");
    value.chapters().values().forEach(Exceptions.sneak().consumer(gen::writeObject));
    gen.writeEndArray();

    gen.writeStringField("panels", "implied-by-viewmode");

    gen.writeEndObject();
  }
}
