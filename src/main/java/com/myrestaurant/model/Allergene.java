package com.myrestaurant.model;

// 14 allergenes ue
public enum Allergene {
  GLUTEN("Gluten"),
  CRUSTACES("Crustacés"),
  OEUFS("Œufs"),
  POISSONS("Poissons"),
  ARACHIDES("Arachides"),
  SOJA("Soja"),
  LAIT("Lait"),
  FRUITS_A_COQUE("Fruits à coque"),
  CELERI("Céleri"),
  MOUTARDE("Moutarde"),
  SESAME("Sésame"),
  SULFITES("Sulfites"),
  LUPIN("Lupin"),
  MOLLUSQUES("Mollusques");

  private final String label;

  Allergene(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
