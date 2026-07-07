package com.myrestaurant.controller;

import com.myrestaurant.model.Review;
import com.myrestaurant.repository.CarteSectionRepository;
import com.myrestaurant.repository.MenuRepository;
import com.myrestaurant.repository.ReviewRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// pages puliques
@Controller
public class PublicController {

  private final CarteSectionRepository carteSections;
  private final MenuRepository menus;
  private final ReviewRepository reviews;

  public PublicController(
    CarteSectionRepository carteSections,
    MenuRepository menus,
    ReviewRepository reviews
  ) {
    this.carteSections = carteSections;
    this.menus = menus;
    this.reviews = reviews;
  }

  @GetMapping("/")
  public String accueil(Model model) {
    model.addAttribute("dernieresReviews", reviews.findTop4ByOrderByIdDesc());
    return "accueil";
  }

  @GetMapping("/carte")
  public String carte(Model model) {
    model.addAttribute(
      "sections",
      carteSections.findAllByOrderByPositionAscIdAsc()
    );
    return "carte";
  }

  @GetMapping("/menus")
  public String menus(Model model) {
    model.addAttribute("menus", menus.findAll());
    return "menus";
  }

  @GetMapping("/reviews")
  public String reviews(Model model) {
    model.addAttribute("reviews", reviews.findAllByOrderByIdDesc());
    model.addAttribute("aujourdhui", LocalDate.now());
    return "reviews";
  }

  @PostMapping("/reviews")
  public String createReview(
    @RequestParam String nom,
    @RequestParam(required = false) String email,
    @RequestParam LocalDate date,
    @RequestParam Integer note,
    @RequestParam(required = false) String commentaire
  ) {
    Review review = new Review();
    review.setNom(nom);
    review.setEmail(email);
    review.setDate(date);
    review.setNote(Math.max(1, Math.min(5, note)));
    review.setCommentaire(commentaire);
    reviews.save(review);
    return "redirect:/reviews";
  }
}
