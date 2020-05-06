package nl.overheid.aerius.service.v2.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.machinezoo.noexception.Exceptions;

import nl.overheid.aerius.shared.domain.Chapter;

public class ChapterSerializer extends StdSerializer<Chapter> {
  private static final long serialVersionUID = 1L;

  public ChapterSerializer() {
    super(Chapter.class);
  }

  @Override
  public void serialize(final Chapter value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    gen.writeStringField("uid", value.uid());
    gen.writeStringField("name", value.title());
    gen.writeStringField("icon", value.icon().name());
    gen.writeNumberField("sortId", value.sortId());

    gen.writeObjectFieldStart("selectables");
    value.selectables().forEach(Exceptions.sneak().consumer(v -> {
      gen.writeStringField(v.type(), v.url());
    }));
    gen.writeEndObject();

    gen.writeObjectFieldStart("panels");
    value.panels().entrySet().forEach(Exceptions.sneak().consumer(v -> {
      gen.writeObjectField(v.getKey().getName(), v.getValue());
    }));
    gen.writeEndObject();

    gen.writeEndObject();
  }
}
