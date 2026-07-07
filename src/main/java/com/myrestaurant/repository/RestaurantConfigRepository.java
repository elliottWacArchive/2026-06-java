package com.myrestaurant.repository;

import com.myrestaurant.model.RestaurantConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantConfigRepository
  extends JpaRepository<RestaurantConfig, Long> {}
