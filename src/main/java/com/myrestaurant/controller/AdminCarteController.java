package com.myrestaurant.controller;

import com.myrestaurant.model.CarteSection;
import com.myrestaurant.repository.CarteSectionRepository;
import com.myrestaurant.repository.PlatRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// crud des sections de la carte
@Controller
@RequestMapping("/admin/carte")
public class AdminCarteController {

  private final CarteSectionRepository sections;
  private final PlatRepository plats;

  public AdminCarteController(
    CarteSectionRepository sections,
    PlatRepository plats
  ) {
    this.sections = sections;
    this.plats = plats;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("sections", sections.findAllByOrderByPositionAscIdAsc());
    return "admin/carte";
  }

  @PostMapping
  public String create(
    @RequestParam String nom,
    @RequestParam(defaultValue = "0") Integer position
  ) {
    CarteSection section = new CarteSection();
    section.setNom(nom);
    section.setPosition(position);
    sections.save(section);
    return "redirect:/admin/carte";
  }

  @PostMapping("/{id}")
  public String update(
    @PathVariable @NonNull Long id,
    @RequestParam String nom,
    @RequestParam(defaultValue = "0") Integer position
  ) {
    CarteSection section = sections.findById(id).orElseThrow();
    section.setNom(nom);
    section.setPosition(position);
    sections.save(section);
    return "redirect:/admin/carte";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable @NonNull Long id) {
    CarteSection section = sections.findById(id).orElseThrow();
    section.getPlats().forEach(p -> {
      p.setCarteSection(null);
      plats.save(p);
    });
    sections.delete(section);
    return "redirect:/admin/carte";
  }
}
