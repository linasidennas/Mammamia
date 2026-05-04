package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import pizzas.InformationPersonnelle;

public class TestInformationPersonnelle {

  @Test
  void testConstructeurBasique() {
    InformationPersonnelle info = new InformationPersonnelle("Skywalker", "Luke");

    assertEquals("Skywalker", info.getNom());
    assertEquals("Luke", info.getPrenom());
    assertEquals(0, info.getAge());
    assertNotNull(info.getAdresse());
  }

  @Test
  void testConstructeurComplet() {
    InformationPersonnelle info = new InformationPersonnelle("Solo", "Han", "Corellia", 35);

    assertEquals(35, info.getAge());
    assertEquals("Corellia", info.getAdresse());
  }

  @Test
  void testSetAgeValide() {
    InformationPersonnelle info = new InformationPersonnelle("Kenobi", "Obiwan");
    info.setAge(50);
    assertEquals(50, info.getAge());
  }

  @Test
  void testSetAgeInvalide() {
    InformationPersonnelle info = new InformationPersonnelle("Kenobi", "Obiwan");
    info.setAge(-5);
    assertEquals(0, info.getAge());
  }

  @Test
  void testSetAdresseNull() {
    InformationPersonnelle info = new InformationPersonnelle("Leia", "Organa");
    info.setAdresse(null);
    assertNotNull(info.getAdresse());
  }
}
