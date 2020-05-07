package nl.overheid.aerius.service.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.machinezoo.noexception.Exceptions;

import nl.overheid.aerius.domain.legend.ComponentLegendConfiguration;

public class ComponentLegendConfigurationSerializer extends LegendConfigurationSerializer<ComponentLegendConfiguration> {
  @Override
  public void serialize(final ComponentLegendConfiguration conf, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
      throws IOException, JsonProcessingException {
    jsonGenerator.writeStartObject();

    super.serialize(conf, jsonGenerator, serializerProvider);

    jsonGenerator.writeObjectFieldStart("component");
    jsonGenerator.writeStringField("name", conf.getComponentName());
    jsonGenerator.writeStringField("source", conf.getComponentSource());

    if (conf.getParameters() != null) {
      jsonGenerator.writeObjectFieldStart("params");
      conf.getParameters().forEach(Exceptions.sneak().fromBiConsumer((k, v) -> jsonGenerator.writeStringField(k, v)));
      jsonGenerator.writeEndObject();
    }

    jsonGenerator.writeEndObject();
    jsonGenerator.writeEndObject();
  }
}
