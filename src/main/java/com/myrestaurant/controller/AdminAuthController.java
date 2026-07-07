package com.myrestaurant.controller;

import com.myrestaurant.service.ConfigService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminAuthController {

  private final ConfigService configService;

  public AdminAuthController(ConfigService configService) {
    this.configService = configService;
  }

  @GetMapping("/admin/login")
  public String login() {
    return "admin/login";
  }

  @PostMapping("/admin/login")
  public String login(
    @RequestParam String motDePasse,
    HttpSession session,
    Model model
  ) {
    if (!configService.checkPassword(motDePasse)) {
      model.addAttribute("erreur", "Mot de passe incorrect");
      return "admin/login";
    }
    session.setAttribute("admin", true);
    return "redirect:/admin";
  }

  @PostMapping("/admin/logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/";
  }
}
