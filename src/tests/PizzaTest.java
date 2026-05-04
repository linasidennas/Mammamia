package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pizzas.*;

/**
 * Classe de tests unitaires pour la classe {@link pizzas.Pizza}.
 
 * Ces tests vérifient : - la validation du constructeur, - la gestion des
 * ingrédients, - le calcul du prix minimal, - la validation du prix de vente, -
 * le calcul du bénéfice unitaire, - la gestion des évaluations et de la note
 * moyenne.
 
 * Les tests sont réalisés avec JUnit 5.
 *
 * @author Kamel BELKHIR
 */
public class PizzaTest {

  /*
   * ======================= CONSTRUCTEUR =======================
   */

  @Test
  void testConstructeurValide() {
    Pizza p = new Pizza("Margherita", TypePizza.Vegetarienne);
    assertEquals("Margherita", p.getNom());
    assertEquals(TypePizza.Vegetarienne, p.getType());
    assertEquals(0.0, p.getPrixVente());
    assertEquals(0.0, p.getNoteMoyenne());
  }

  @Test
  void testConstructeurInvalide() {
    assertThrows(IllegalArgumentException.class, () -> new Pizza("", TypePizza.Viande));

    assertThrows(IllegalArgumentException.class, () -> new Pizza("Test", null));
  }

  /*
   * ======================= INGREDIENTS =======================
   */

  @Test
  void testAjouterIngredient() {
    Pizza p = new Pizza("Test", TypePizza.Viande);
    Ingredient ing = new Ingredient("Fromage", 2.0);

    p.ajouterIngredient(ing);
    assertEquals(1, p.getIngredients().size());
    assertTrue(p.getIngredients().contains(ing));
  }

  @Test
  void testAjouterIngredientNull() {
    Pizza p = new Pizza("Test", TypePizza.Viande);

    assertThrows(IllegalArgumentException.class, () -> p.ajouterIngredient(null));
  }

  @Test
  void testRetirerIngredient() {
    Pizza p = new Pizza("Test", TypePizza.Viande);
    Ingredient ing = new Ingredient("Tomate", 1.0);

    p.ajouterIngredient(ing);

    assertTrue(p.retirerIngredient(ing));
    assertFalse(p.retirerIngredient(ing)); // déjà retiré
    assertFalse(p.retirerIngredient(null));
  }

  /*
   * ======================= PRIX =======================
   */

  @Test
  void testPrixMinimal() {
    Pizza p = new Pizza("Test", TypePizza.Vegetarienne);
    p.ajouterIngredient(new Ingredient("Fromage", 2.0));
    p.ajouterIngredient(new Ingredient("Tomate", 1.0));

    // (2 + 1) * 1.4 = 4.2
    assertEquals(4.2, p.getPrixMinimal());
  }

  @Test
  void testSetPrixVenteValide() {
    Pizza p = new Pizza("Test", TypePizza.Viande);
    p.ajouterIngredient(new Ingredient("Fromage", 2.0));

    p.setPrixVente(5.0);
    assertEquals(5.0, p.getPrixVente());
  }

  @Test
  void testSetPrixVenteInvalide() {
    Pizza p = new Pizza("Test", TypePizza.Viande);
    p.ajouterIngredient(new Ingredient("Fromage", 2.0));

    assertThrows(IllegalArgumentException.class, () -> p.setPrixVente(1.0));
  }

  @Test
  void testBeneficeUnitaire() {
    Pizza p = new Pizza("Test", TypePizza.Viande);
    p.ajouterIngredient(new Ingredient("Fromage", 2.0));

    double prixMin = p.getPrixMinimal();
    p.setPrixVente(prixMin + 2);

    assertEquals(2.0, p.getBeneficeUnitaire());
  }

  /*
   * ======================= EVALUATIONS =======================
   */

  @Test
  void testEvaluationValide() {
    Pizza p = new Pizza("Test", TypePizza.Viande);
    Evaluation e = new Evaluation(p, 4, "Bonne");

    p.ajouterEvaluation(e);

    assertEquals(1, p.getEvaluations().size());
    assertEquals(4.0, p.getNoteMoyenne());
  }

  @Test
  void testNoteMoyenneSansEvaluation() {
    Pizza p = new Pizza("Test", TypePizza.Viande);
    assertEquals(0.0, p.getNoteMoyenne());
  }

  @Test
  void testEvaluationInvalide() {
    Pizza p1 = new Pizza("P1", TypePizza.Viande);
    Pizza p2 = new Pizza("P2", TypePizza.Viande);
    Evaluation e = new Evaluation(p2, 3, "Bof");

    assertThrows(IllegalArgumentException.class, () -> p1.ajouterEvaluation(null));

    assertThrows(IllegalArgumentException.class, () -> p1.ajouterEvaluation(e));
  }
}
