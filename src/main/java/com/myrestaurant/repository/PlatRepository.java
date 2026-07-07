package com.myrestaurant.repository;

import com.myrestaurant.model.Plat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatRepository extends JpaRepository<Plat, Long> {}
