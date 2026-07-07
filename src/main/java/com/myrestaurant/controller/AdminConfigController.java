package com.myrestaurant.controller;

import com.myrestaurant.model.RestaurantConfig;
import com.myrestaurant.service.ConfigService;
import com.myrestaurant.service.FileStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminConfigController {

  private final ConfigService configService;
  private final FileStorageService fileStorageService;

  public AdminConfigController(
    ConfigService configService,
    FileStorageService fileStorageService
  ) {
    this.configService = configService;
    this.fileStorageService = fileStorageService;
  }

  @GetMapping("/admin")
  public String dashboard() {
    return "admin/config";
  }

  @PostMapping("/admin/config")
  public String save(
    @RequestParam String nom,
    @RequestParam String url,
    @RequestParam String couleur,
    @RequestParam(required = false) MultipartFile banniere,
    @RequestParam String adresse,
    @RequestParam(required = false) String telephone,
    @RequestParam(required = false) String email,
    @RequestParam(required = false) String horaires,
    @RequestParam(required = false) String description
  ) {
    RestaurantConfig config = configService.get();
    config.setNom(nom);
    config.setUrl(url);
    config.setCouleur(couleur);
    if (banniere != null && !banniere.isEmpty()) {
      config.setBanniere(fileStorageService.store(banniere));
    }
    config.setAdresse(adresse);
    config.setTelephone(telephone);
    config.setEmail(email);
    config.setHoraires(horaires);
    config.setDescription(description);
    configService.save(config);
    return "redirect:/admin";
  }
}
