package com.myrestaurant.controller;

import com.myrestaurant.model.Menu;
import com.myrestaurant.model.MenuSection;
import com.myrestaurant.repository.MenuRepository;
import com.myrestaurant.repository.MenuSectionRepository;
import java.math.BigDecimal;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// crud des menus et de leurs sections
@Controller
@RequestMapping("/admin/menus")
public class AdminMenuController {

  private final MenuRepository menus;
  private final MenuSectionRepository menuSections;

  public AdminMenuController(
    MenuRepository menus,
    MenuSectionRepository menuSections
  ) {
    this.menus = menus;
    this.menuSections = menuSections;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("menus", menus.findAll());
    return "admin/menus";
  }

  @PostMapping
  public String create(
    @RequestParam String nom,
    @RequestParam(required = false) BigDecimal prix
  ) {
    Menu menu = new Menu();
    menu.setNom(nom);
    menu.setPrix(prix);
    menus.save(menu);
    return "redirect:/admin/menus";
  }

  @PostMapping("/{id}")
  public String update(
    @PathVariable @NonNull Long id,
    @RequestParam String nom,
    @RequestParam(required = false) BigDecimal prix
  ) {
    Menu menu = menus.findById(id).orElseThrow();
    menu.setNom(nom);
    menu.setPrix(prix);
    menus.save(menu);
    return "redirect:/admin/menus";
  }

  @PostMapping("/{id}/delete")
  @Transactional
  public String delete(@PathVariable @NonNull Long id) {
    Menu menu = menus.findById(id).orElseThrow();
    menu
      .getSections()
      .forEach(s -> s.getPlats().forEach(p -> p.getMenuSections().remove(s)));
    menus.delete(menu);
    return "redirect:/admin/menus";
  }

  @PostMapping("/{id}/sections")
  public String createSection(
    @PathVariable @NonNull Long id,
    @RequestParam String nom,
    @RequestParam(defaultValue = "0") Integer position
  ) {
    MenuSection section = new MenuSection();
    section.setNom(nom);
    section.setPosition(position);
    section.setMenu(menus.findById(id).orElseThrow());
    menuSections.save(section);
    return "redirect:/admin/menus";
  }

  @PostMapping("/sections/{id}")
  public String updateSection(
    @PathVariable @NonNull Long id,
    @RequestParam String nom,
    @RequestParam(defaultValue = "0") Integer position
  ) {
    MenuSection section = menuSections.findById(id).orElseThrow();
    section.setNom(nom);
    section.setPosition(position);
    menuSections.save(section);
    return "redirect:/admin/menus";
  }

  @PostMapping("/sections/{id}/delete")
  @Transactional
  public String deleteSection(@PathVariable @NonNull Long id) {
    MenuSection section = menuSections.findById(id).orElseThrow();
    section.getPlats().forEach(p -> p.getMenuSections().remove(section));
    section.getMenu().getSections().remove(section);
    menuSections.delete(section);
    return "redirect:/admin/menus";
  }
}
