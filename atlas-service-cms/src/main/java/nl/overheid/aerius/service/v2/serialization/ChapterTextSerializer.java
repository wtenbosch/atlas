package nl.overheid.aerius.service.v2.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import nl.overheid.aerius.shared.domain.ChapterText;

public class ChapterTextSerializer extends StdSerializer<ChapterText> {
  private static final long serialVersionUID = 1L;

  public ChapterTextSerializer() {
    super(ChapterText.class);
  }

  @Override
  public void serialize(final ChapterText value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    gen.writeNumberField("version", 3);
    gen.writeStringField("text", value.text());

    gen.writeEndObject();
  }
}
