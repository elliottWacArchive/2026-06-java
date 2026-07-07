package com.myrestaurant.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// plats
@Entity
public class Plat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nom;
  private String photo;

  @Column(length = 1000)
  private String description;

  private BigDecimal prix;

  @ElementCollection(fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  private Set<Allergene> allergenes = new LinkedHashSet<>();

  @ManyToOne
  private CarteSection carteSection;

  @ManyToMany
  @JoinTable(name = "plat_menu_sections")
  private List<MenuSection> menuSections = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrix() {
    return prix;
  }

  public void setPrix(BigDecimal prix) {
    this.prix = prix;
  }

  public Set<Allergene> getAllergenes() {
    return allergenes;
  }

  public void setAllergenes(Set<Allergene> allergenes) {
    this.allergenes = allergenes;
  }

  public CarteSection getCarteSection() {
    return carteSection;
  }

  public void setCarteSection(CarteSection carteSection) {
    this.carteSection = carteSection;
  }

  public List<MenuSection> getMenuSections() {
    return menuSections;
  }

  public void setMenuSections(List<MenuSection> menuSections) {
    this.menuSections = menuSections;
  }
}
