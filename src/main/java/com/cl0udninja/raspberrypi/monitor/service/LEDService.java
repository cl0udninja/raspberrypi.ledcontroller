package com.cl0udninja.raspberrypi.monitor.service;

import javax.validation.Valid;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.cl0udninja.raspberrypi.monitor.web.dto.LedDTO;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@Slf4j
public class LEDService implements InitializingBean, DisposableBean {

  public static final Pin LED_PIN = RaspiPin.GPIO_01;
  public static final String LED_NAME = "Fireflies";

  private GpioController gpio;
  private GpioPinDigitalOutput pin;

  @Override
  public void afterPropertiesSet() throws Exception {
    gpio = GpioFactory.getInstance();
    pin = gpio.provisionDigitalOutputPin(LED_PIN, LED_NAME, PinState.LOW);
    // set shutdown state for this pin
    pin.setShutdownOptions(true, PinState.LOW);
  }

  @Override
  public void destroy() throws Exception {
    gpio.shutdown();
  }

  public void switchLED(@Valid LedDTO toggleLEDDTO) {
    log.debug(String.format("Changing LED state to %s", toggleLEDDTO.getPinState()));
    if (toggleLEDDTO.getPinState().equals(pin.getState())) {
      return; // nothing to do
    }
    pin.setState(toggleLEDDTO.getPinState());
  }

  public LedDTO getLEDState() {
    LedDTO leddto = new LedDTO();
    leddto.setPinState(pin.getState());
    log.debug(String.format("LED state is %s", leddto));
    return leddto;
  }

  public void toggleLEDState() {
    log.debug("Toggling LED");
    pin.toggle();
  }
}
