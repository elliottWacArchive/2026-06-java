package com.myrestaurant.controller;

import com.myrestaurant.model.RestaurantConfig;
import com.myrestaurant.service.ConfigService;
import com.myrestaurant.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/setup")
public class SetupController {

  private final ConfigService configService;
  private final FileStorageService fileStorageService;

  public SetupController(
    ConfigService configService,
    FileStorageService fileStorageService
  ) {
    this.configService = configService;
    this.fileStorageService = fileStorageService;
  }

  @GetMapping
  public String setup(Model model, HttpServletRequest request) {
    if (!model.containsAttribute("form")) {
      SetupForm form = new SetupForm();
      form.setUrl(request.getRequestURL().toString().replace("/setup", ""));
      form.setCouleur("#d15454");
      model.addAttribute("form", form);
    }
    return "setup";
  }

  @PostMapping
  public String save(
    @Valid @ModelAttribute("form") SetupForm form,
    BindingResult result,
    @RequestParam("banniere") MultipartFile banniere,
    Model model
  ) {
    if (banniere.isEmpty()) {
      result.rejectValue("nom", "banniere", "La bannière est obligatoire");
    }
    if (result.hasErrors()) {
      return "setup";
    }
    RestaurantConfig config = new RestaurantConfig();
    config.setNom(form.getNom());
    config.setUrl(form.getUrl());
    config.setCouleur(form.getCouleur());
    config.setBanniere(fileStorageService.store(banniere));
    config.setAdminPasswordHash(
      configService.hashPassword(form.getMotDePasse())
    );
    config.setAdresse(form.getAdresse());
    config.setTelephone(form.getTelephone());
    config.setEmail(form.getEmail());
    config.setHoraires(form.getHoraires());
    config.setDescription(form.getDescription());
    configService.save(config);
    return "redirect:/";
  }
}
