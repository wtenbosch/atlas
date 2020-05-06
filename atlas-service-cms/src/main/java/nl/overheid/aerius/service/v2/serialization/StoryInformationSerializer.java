package nl.overheid.aerius.service.v2.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import nl.overheid.aerius.shared.domain.StoryInformation;

public class StoryInformationSerializer extends StdSerializer<StoryInformation> {
  private static final long serialVersionUID = 1L;

  public StoryInformationSerializer() {
    super(StoryInformation.class);
  }

  public static void writeStoryInformation(final StoryInformation story, final JsonGenerator jgen) throws IOException {
    jgen.writeStringField("uid", story.uid());
    jgen.writeStringField("name", story.name());
    jgen.writeObjectField("icon", story.icon());
    jgen.writeStringField("author", story.authorName());
    jgen.writeNumberField("orderId", story.orderId());

    jgen.writeObjectField("changedDate", story.changedDate());
    jgen.writeObjectField("creationDate", story.creationDate());

    jgen.writeObjectField("properties", story.properties());
  }

  @Override
  public void serialize(final StoryInformation value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    writeStoryInformation(value, gen);

    gen.writeEndObject();
  }
}
