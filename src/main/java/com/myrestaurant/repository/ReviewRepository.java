package com.myrestaurant.repository;

import com.myrestaurant.model.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  List<Review> findTop4ByOrderByIdDesc();
  List<Review> findAllByOrderByIdDesc();
}
