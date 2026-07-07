package com.myrestaurant.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.myrestaurant.model.RestaurantConfig;
import com.myrestaurant.repository.RestaurantConfigRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigServiceTest {

  private RestaurantConfigRepository repository;
  private ConfigService service;

  @BeforeEach
  void setUp() {
    repository = mock(RestaurantConfigRepository.class);
    service = new ConfigService(repository);
  }

  @Test
  void motDePasseHasheEtVerifiable() {
    String hash = service.hashPassword("Password1");
    assertNotEquals("Password1", hash);

    RestaurantConfig config = new RestaurantConfig();
    config.setAdminPasswordHash(hash);
    when(repository.findById(1L)).thenReturn(Optional.of(config));

    assertTrue(service.checkPassword("Password1"));
    assertFalse(service.checkPassword("Password2"));
  }

  @Test
  void checkPasswordSansConfigEstFaux() {
    when(repository.findById(1L)).thenReturn(Optional.empty());
    assertFalse(service.checkPassword("Password1"));
  }

  @Test
  void isConfiguredSuitLaBase() {
    when(repository.existsById(1L)).thenReturn(false);
    assertFalse(service.isConfigured());
    when(repository.existsById(1L)).thenReturn(true);
    assertTrue(service.isConfigured());
  }
}
