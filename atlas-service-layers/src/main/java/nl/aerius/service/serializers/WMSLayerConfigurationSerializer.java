package nl.aerius.service.serializers;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.machinezoo.noexception.Exceptions;

import nl.aerius.domain.WMSLayerConfiguration;

public class WMSLayerConfigurationSerializer extends JsonSerializer<WMSLayerConfiguration> {
  @Override
  public void serialize(final WMSLayerConfiguration conf, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
      throws IOException, JsonProcessingException {
    jsonGenerator.writeStartObject();

    jsonGenerator.writeObject(conf.conf());

    jsonGenerator.writeObjectFieldStart("wms");
    jsonGenerator.writeStringField("url", conf.url());
    jsonGenerator.writeStringField("layer", conf.baseLayer());
    jsonGenerator.writeStringField("format", conf.formats());
    jsonGenerator.writeStringField("style", Optional.ofNullable(conf.baseStyle()).orElse(conf.baseLayer()));

    jsonGenerator.writeStringField("viewparams", conf.viewParams());
    jsonGenerator.writeStringField("cql", conf.cqlFilter());

    if (conf.activators() != null) {
      jsonGenerator.writeObjectFieldStart("activators");
      conf.activators().forEach(Exceptions.sneak().consumer(v -> {
        jsonGenerator.writeObjectFieldStart(v.getActivator());
        jsonGenerator.writeStringField("layer", v.getLayer());
        jsonGenerator.writeStringField("style", v.getStyle());
        jsonGenerator.writeEndObject();
      }));
      jsonGenerator.writeEndObject();
    }

    jsonGenerator.writeEndObject();
    jsonGenerator.writeEndObject();
  }
}
