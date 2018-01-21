package com.cl0udninja.raspberrypi.monitor.service;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ButtonListenerService implements InitializingBean, DisposableBean, GpioPinListenerDigital {

  public static final Pin BUTTON_PIN = RaspiPin.GPIO_05;
  public static final String BUTTON_NAME = "FireflyButton";

  @Autowired
  private LEDService ledService;

  private GpioController gpio;
  private GpioPinDigitalInput ledButton;

  @Override
  public void afterPropertiesSet() throws Exception {
    log.info("ButtonListenerService is setting up...");
    gpio = GpioFactory.getInstance();
    ledButton = gpio.provisionDigitalInputPin(BUTTON_PIN, BUTTON_NAME, PinPullResistance.PULL_UP);
    ledButton.setShutdownOptions(true);

    ledButton.addListener(new GpioPinListenerDigital() {

      @Override
      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        log.debug(String.format("[internal listener] State is %s for button %s", event.getState(), event.getPin()));
      }
    }, this);

    log.info("... ButtonListenerService is set up and listening");
  }

  @Override
  public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
    log.debug(String.format("State is %s for button %s", event.getState(), event.getPin()));
    if (PinState.HIGH.equals(event.getState())) {
      this.ledService.toggleLEDState();
    }
  }

  @Override
  public void destroy() throws Exception {
    gpio.shutdown();
  }

}
