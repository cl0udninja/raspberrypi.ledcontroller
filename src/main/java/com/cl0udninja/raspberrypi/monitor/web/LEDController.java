package com.cl0udninja.raspberrypi.monitor.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cl0udninja.raspberrypi.monitor.service.LEDService;
import com.cl0udninja.raspberrypi.monitor.web.dto.LedDTO;

@RestController
@RequestMapping("/api/led")
@Validated
public class LEDController {

  @Autowired
  private LEDService ledService;

  @PostMapping
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void setLEDState(@RequestBody @Valid LedDTO toggleLEDDTO) {
    this.ledService.switchLED(toggleLEDDTO);
  }

  @GetMapping
  public ResponseEntity<LedDTO> getLEDState() {
    return ResponseEntity.ok(ledService.getLEDState());
  }
}
