package com.myrestaurant.service;

import com.myrestaurant.model.RestaurantConfig;
import com.myrestaurant.repository.RestaurantConfigRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {

  private final RestaurantConfigRepository repository;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public ConfigService(RestaurantConfigRepository repository) {
    this.repository = repository;
  }

  public boolean isConfigured() {
    return repository.existsById(1L);
  }

  public RestaurantConfig get() {
    return repository.findById(1L).orElse(null);
  }

  public RestaurantConfig save(RestaurantConfig config) {
    config.setId(1L);
    return repository.save(config);
  }

  public String hashPassword(String password) {
    return encoder.encode(password);
  }

  public boolean checkPassword(String password) {
    RestaurantConfig config = get();
    return (
      config != null && encoder.matches(password, config.getAdminPasswordHash())
    );
  }
}
