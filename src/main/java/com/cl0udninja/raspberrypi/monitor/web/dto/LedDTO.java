package com.cl0udninja.raspberrypi.monitor.web.dto;

import java.io.IOException;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pi4j.io.gpio.PinState;

import lombok.Data;

@Validated
@Data
public class LedDTO {

  @JsonProperty
  @JsonSerialize(using = PinStateSerializer.class)
  @JsonDeserialize(using = PinStateDeserializer.class)
  @NotNull
  private PinState pinState;

  public static class PinStateSerializer extends JsonSerializer<PinState> {
    @Override
    public void serialize(PinState pinState, JsonGenerator json, SerializerProvider provider) throws IOException {
      json.writeString(pinState == null ? null : pinState.getName());
    }
  }

  public static class PinStateDeserializer extends JsonDeserializer<PinState> {
    @Override
    public PinState deserialize(JsonParser parser, DeserializationContext context)
        throws IOException, JsonProcessingException {
      ObjectCodec oc = parser.getCodec();
      JsonNode node = oc.readTree(parser);
      String pinState = node.textValue();
      if (pinState == null) {
        return null;
      } else if (pinState.isEmpty()) {
        throw new JsonParseException(parser, String.format("\"%s\" is an invalid PinState", pinState));
      } else if (PinState.HIGH.name().equals(pinState)) {
        return PinState.HIGH;
      } else if (PinState.LOW.name().equals(pinState)) {
        return PinState.LOW;
      }
      throw new JsonParseException(parser, String.format("\"%s\" is an invalid PinState", pinState));
    }

  }
}
