package com.myrestaurant.repository;

import com.myrestaurant.model.CarteSection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarteSectionRepository
  extends JpaRepository<CarteSection, Long>
{
  List<CarteSection> findAllByOrderByPositionAscIdAsc();
}
