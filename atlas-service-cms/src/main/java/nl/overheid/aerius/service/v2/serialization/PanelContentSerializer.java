package nl.overheid.aerius.service.v2.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.machinezoo.noexception.Exceptions;

import nl.overheid.aerius.shared.domain.PanelContent;

public class PanelContentSerializer extends StdSerializer<PanelContent> {
  private static final long serialVersionUID = 1L;

  public PanelContentSerializer() {
    super(PanelContent.class);
  }

  @Override
  public void serialize(final PanelContent value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    gen.writeArrayFieldStart("selectables");
    value.selectables().forEach(Exceptions.sneak().consumer(v -> {
      gen.writeString(v);
    }));
    gen.writeEndArray();

    gen.writeObjectField("properties", value.properties());

    gen.writeEndObject();
  }
}
