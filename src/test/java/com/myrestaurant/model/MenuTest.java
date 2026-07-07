package com.myrestaurant.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class MenuTest {

  private Menu menu(String prixCustom, String... prixPlats) {
    Menu menu = new Menu();
    menu.setNom("Menu test");
    if (prixCustom != null) {
      menu.setPrix(new BigDecimal(prixCustom));
    }
    MenuSection section = new MenuSection();
    section.setNom("Section");
    section.setMenu(menu);
    for (String p : prixPlats) {
      Plat plat = new Plat();
      plat.setPrix(new BigDecimal(p));
      section.getPlats().add(plat);
    }
    menu.setSections(List.of(section));
    return menu;
  }

  @Test
  void prixDefautEstLaSommeDesPlats() {
    assertEquals(
      new BigDecimal("41.50"),
      menu(null, "9.50", "24.00", "8.00").getPrixDefaut()
    );
  }

  @Test
  void prixDefautSansPlatEstZero() {
    assertEquals(BigDecimal.ZERO, menu(null).getPrixDefaut());
  }

  @Test
  void prixAfficheSansCustomEstLeDefaut() {
    assertEquals(
      new BigDecimal("17.50"),
      menu(null, "9.50", "8.00").getPrixAffiche()
    );
  }

  @Test
  void prixAfficheAvecCustomEstLeCustom() {
    assertEquals(
      new BigDecimal("35.00"),
      menu("35.00", "9.50", "24.00", "8.00").getPrixAffiche()
    );
  }

  @Test
  void reduitSiCustomSousLeDefaut() {
    assertTrue(menu("35.00", "9.50", "24.00", "8.00").isReduit());
  }

  @Test
  void pasReduitSansCustom() {
    assertFalse(menu(null, "9.50").isReduit());
  }

  @Test
  void pasReduitSiCustomAuDessusDuDefaut() {
    assertFalse(menu("50.00", "9.50", "24.00", "8.00").isReduit());
  }

  @Test
  void prixTheoriqueRepartitLaReductionAuProrata() {
    Menu menu = menu("20.00", "10.00", "30.00");
    assertEquals(
      new BigDecimal("5.00"),
      menu.prixTheorique(menu.getSections().get(0).getPlats().get(0))
    );
    assertEquals(
      new BigDecimal("15.00"),
      menu.prixTheorique(menu.getSections().get(0).getPlats().get(1))
    );
  }

  @Test
  void prixTheoriqueArrondiAuCentime() {
    Menu menu = menu("10.00", "5.00", "10.00");
    assertEquals(
      new BigDecimal("3.33"),
      menu.prixTheorique(menu.getSections().get(0).getPlats().get(0))
    );
  }
}
