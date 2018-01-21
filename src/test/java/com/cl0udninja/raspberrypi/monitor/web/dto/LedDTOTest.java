package com.cl0udninja.raspberrypi.monitor.web.dto;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi4j.io.gpio.PinState;

@RunWith(SpringRunner.class)
public class LedDTOTest {

  @Test
  public void testSerialization() throws Exception {
    LedDTO leddto = new LedDTO();
    leddto.setPinState(PinState.HIGH);

    String json = new ObjectMapper().writeValueAsString(leddto);
    Assert.assertTrue(json.contains("\"pinState\""));
    Assert.assertTrue(json.contains("\"HIGH\""));
  }

  @Test
  public void testDeserialization() throws Exception {
    String json = "" + "{\"pinState\":\"LOW\"}";
    LedDTO leddto = new LedDTO();
    leddto.setPinState(PinState.LOW);

    LedDTO deserialized = new ObjectMapper().readValue(json, LedDTO.class);
    Assert.assertEquals(leddto, deserialized);
  }

}
