package com.myrestaurant.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

class SetupFormTest {

  private final Validator validator =
    Validation.buildDefaultValidatorFactory().getValidator();

  private boolean valide(String motDePasse) {
    SetupForm form = new SetupForm();
    form.setNom("Chez Test");
    form.setUrl("http://localhost:8080");
    form.setCouleur("#09b412");
    form.setAdresse("1 rue du Test");
    form.setMotDePasse(motDePasse);
    return validator.validate(form).isEmpty();
  }

  @Test
  void motDePasseValide() {
    assertTrue(valide("Password1"));
  }

  @Test
  void tropCourt() {
    assertFalse(valide("Pass1"));
  }

  @Test
  void sansMajuscule() {
    assertFalse(valide("password1"));
  }

  @Test
  void sansMinuscule() {
    assertFalse(valide("PASSWORD1"));
  }

  @Test
  void sansChiffre() {
    assertFalse(valide("Passwordd"));
  }

  @Test
  void champObligatoireManquant() {
    SetupForm form = new SetupForm();
    form.setMotDePasse("Password1");
    assertFalse(validator.validate(form).isEmpty());
  }
}
