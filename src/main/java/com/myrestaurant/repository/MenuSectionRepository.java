package com.myrestaurant.repository;

import com.myrestaurant.model.MenuSection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuSectionRepository
  extends JpaRepository<MenuSection, Long> {}
