package com.myrestaurant.config;

import com.myrestaurant.model.RestaurantConfig;
import com.myrestaurant.service.ConfigService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// rend la config dispo dans tous les templates
@ControllerAdvice
public class GlobalModelAdvice {

  private final ConfigService configService;

  public GlobalModelAdvice(ConfigService configService) {
    this.configService = configService;
  }

  @ModelAttribute("config")
  public RestaurantConfig config() {
    return configService.get();
  }
}
