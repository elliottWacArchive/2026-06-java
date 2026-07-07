package com.myrestaurant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// config restaurant param : nom, url, couleur, banniere, adminPasswordHash, adresse, telephone, email, horaires, description
@Entity
public class RestaurantConfig {

  @Id
  private Long id = 1L;

  private String nom;
  private String url;
  private String couleur;
  private String banniere;
  private String adminPasswordHash;

  private String adresse;
  private String telephone;
  private String email;

  @Column(length = 2000)
  private String horaires;

  @Column(length = 2000)
  private String description;

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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getCouleur() {
    return couleur;
  }

  public void setCouleur(String couleur) {
    this.couleur = couleur;
  }

  public String getBanniere() {
    return banniere;
  }

  public void setBanniere(String banniere) {
    this.banniere = banniere;
  }

  public String getAdminPasswordHash() {
    return adminPasswordHash;
  }

  public void setAdminPasswordHash(String adminPasswordHash) {
    this.adminPasswordHash = adminPasswordHash;
  }

  public String getAdresse() {
    return adresse;
  }

  public void setAdresse(String adresse) {
    this.adresse = adresse;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getHoraires() {
    return horaires;
  }

  public void setHoraires(String horaires) {
    this.horaires = horaires;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
