package com.myrestaurant.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

// menus composes de sections
@Entity
@Table(name = "menus")
public class Menu {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nom;

  // prix custom optionnel sinon somme des plats
  private BigDecimal prix;

  @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("position, id")
  private List<MenuSection> sections = new ArrayList<>();

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

  public BigDecimal getPrix() {
    return prix;
  }

  public void setPrix(BigDecimal prix) {
    this.prix = prix;
  }

  public List<MenuSection> getSections() {
    return sections;
  }

  public void setSections(List<MenuSection> sections) {
    this.sections = sections;
  }

  public BigDecimal getPrixDefaut() {
    return sections
      .stream()
      .flatMap(s -> s.getPlats().stream())
      .map(Plat::getPrix)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public BigDecimal getPrixAffiche() {
    return prix != null ? prix : getPrixDefaut();
  }

  public boolean isReduit() {
    return prix != null && getPrixDefaut().compareTo(prix) > 0;
  }

  // prix du plat avec menu custom price
  public BigDecimal prixTheorique(Plat plat) {
    return plat
      .getPrix()
      .multiply(prix)
      .divide(getPrixDefaut(), 2, RoundingMode.HALF_UP);
  }
}
