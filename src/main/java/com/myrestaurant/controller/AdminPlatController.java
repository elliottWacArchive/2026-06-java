package com.myrestaurant.controller;

import com.myrestaurant.model.Allergene;
import com.myrestaurant.model.Plat;
import com.myrestaurant.repository.CarteSectionRepository;
import com.myrestaurant.repository.MenuRepository;
import com.myrestaurant.repository.MenuSectionRepository;
import com.myrestaurant.repository.PlatRepository;
import com.myrestaurant.service.FileStorageService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

// crud des plats leurs assoc
@Controller
@RequestMapping("/admin/plats")
public class AdminPlatController {

  private final PlatRepository plats;
  private final CarteSectionRepository carteSections;
  private final MenuRepository menus;
  private final MenuSectionRepository menuSections;
  private final FileStorageService fileStorageService;

  public AdminPlatController(
    PlatRepository plats,
    CarteSectionRepository carteSections,
    MenuRepository menus,
    MenuSectionRepository menuSections,
    FileStorageService fileStorageService
  ) {
    this.plats = plats;
    this.carteSections = carteSections;
    this.menus = menus;
    this.menuSections = menuSections;
    this.fileStorageService = fileStorageService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("plats", plats.findAll());
    return "admin/plats";
  }

  @GetMapping("/nouveau")
  public String createForm(Model model) {
    fillForm(model, new Plat());
    return "admin/plat-form";
  }

  @GetMapping("/{id}")
  public String editForm(@PathVariable @NonNull Long id, Model model) {
    fillForm(model, plats.findById(id).orElseThrow());
    return "admin/plat-form";
  }

  @PostMapping("/save")
  public String save(
    @RequestParam(required = false) Long id,
    @RequestParam String nom,
    @RequestParam(required = false) MultipartFile photo,
    @RequestParam String description,
    @RequestParam BigDecimal prix,
    @RequestParam(required = false) List<Allergene> allergenes,
    @RequestParam(required = false) Long carteSectionId,
    @RequestParam(required = false) List<Long> menuSectionIds
  ) {
    Plat plat = id != null ? plats.findById(id).orElseThrow() : new Plat();
    plat.setNom(nom);
    plat.setDescription(description);
    plat.setPrix(prix);
    if (photo != null && !photo.isEmpty()) {
      plat.setPhoto(fileStorageService.store(photo));
    }
    plat.setAllergenes(
      allergenes != null
        ? new LinkedHashSet<>(allergenes)
        : new LinkedHashSet<>()
    );
    plat.setCarteSection(
      carteSectionId != null
        ? carteSections.findById(carteSectionId).orElse(null)
        : null
    );
    plat.setMenuSections(
      menuSectionIds != null
        ? new ArrayList<>(menuSections.findAllById(menuSectionIds))
        : new ArrayList<>()
    );
    plats.save(plat);
    return "redirect:/admin/plats";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable @NonNull Long id) {
    plats.deleteById(id);
    return "redirect:/admin/plats";
  }

  private void fillForm(Model model, Plat plat) {
    model.addAttribute("plat", plat);
    model.addAttribute("allergenes", Allergene.values());
    model.addAttribute(
      "carteSections",
      carteSections.findAllByOrderByPositionAscIdAsc()
    );
    model.addAttribute("menus", menus.findAll());
  }
}
