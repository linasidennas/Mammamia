package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import pizzas.Ingredient;

/**
 * Classe de tests unitaires pour la classe {@link pizzas.Ingredient}.
 *
 * Cette classe vérifie le bon fonctionnement des ingrédients utilisés
 * dans la composition des pizzas, notamment :
 * - la création valide d’un ingrédient avec un nom et un prix corrects,
 * - le rejet des noms nuls ou vides,
 * - l’interdiction des prix négatifs lors de la création,
 * - la modification correcte du prix,
 * - le rejet des valeurs de prix invalides lors de la modification.
 *
 * Les tests sont réalisés à l’aide de JUnit 5.
 *
 * @author Lina Sidennas
 */
public class IngredientTest {

  /**
   * Vérifie la création valide d’un ingrédient.
   * Le nom et le prix doivent être correctement initialisés
   * et accessibles via les accesseurs.
   */
  @Test
  void testCreationValide() {
    Ingredient i = new Ingredient("Fromage", 1.5);

    assertEquals("Fromage", i.getNom());
    assertEquals(1.5, i.getPrix());
  }

  /**
   * Vérifie que la création d’un ingrédient avec un nom nul
   * déclenche une exception.
   */
  @Test
  void testNomNullInterdit() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Ingredient(null, 1.0)
    );
  }

  /**
   * Vérifie que la création d’un ingrédient avec un nom vide
   * déclenche une exception.
   */
  @Test
  void testNomVideInterdit() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Ingredient("   ", 1.0)
    );
  }

  /**
   * Vérifie que la création d’un ingrédient
   * avec un prix négatif déclenche une exception.
   */
  @Test
  void testPrixNegatifInterdit() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Ingredient("Jambon", -1.0)
    );
  }

  /**
   * Vérifie la modification correcte du prix d’un ingrédient.
   * Le nouveau prix doit être correctement appliqué.
   */
  @Test
  void testSetPrix() {
    Ingredient i = new Ingredient("Tomate", 1.0);

    i.setPrix(2.0);

    assertEquals(2.0, i.getPrix());
  }

  /**
   * Vérifie que la modification du prix vers une valeur négative
   * déclenche une exception.
   */
  @Test
  void testSetPrixNegatif() {
    Ingredient i = new Ingredient("Tomate", 1.0);

    assertThrows(
        IllegalArgumentException.class,
        () -> i.setPrix(-2.0)
    );
  }
}
