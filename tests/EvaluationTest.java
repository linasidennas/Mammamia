package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pizzas.*;

/**
 * Classe de tests unitaires pour la classe {@link pizzas.Evaluation}.
 *
 * Ces tests vérifient :
 * - la création valide d’évaluations,
 * - les cas d’erreurs (note invalide, pizza nulle, auteur invalide),
 * - le bon fonctionnement des getters,
 * - la représentation textuelle via toString().
 *
 * @author Inas Hallal
 */
public class EvaluationTest {

  /* ========================= */
  /* ===== CAS VALIDES ======= */
  /* ========================= */

  /**
   * Test de création d’une évaluation valide
   * sans auteur explicite.
   */
  @Test
  void testEvaluationValideSansAuteur() {
    Pizza p = new Pizza("Test", TypePizza.Viande);
    Evaluation e = new Evaluation(p, 5, "Excellent");

    assertEquals(5, e.getNote());
    assertEquals("Excellent", e.getCommentaire());
    assertEquals(p, e.getPizza());
    assertNull(e.getAuteur());
  }

  /**
   * Test de création d’une évaluation valide
   * avec auteur explicite.
   */
  @Test
  void testEvaluationValideAvecAuteur() {
    Pizza p = new Pizza("Test", TypePizza.Vegetarienne);
    Evaluation e = new Evaluation(p, "Jean", 4, "Très bonne");

    assertEquals(4, e.getNote());
    assertEquals("Très bonne", e.getCommentaire());
    assertEquals("Jean", e.getAuteur());
    assertEquals(p, e.getPizza());
  }

  /**
   * Test avec commentaire nul (autorisé).
   */
  @Test
  void testEvaluationCommentaireNull() {
    Pizza p = new Pizza("Test", TypePizza.Regionale);
    Evaluation e = new Evaluation(p, 3, null);

    assertEquals(3, e.getNote());
    assertNull(e.getCommentaire());
  }

  /**
   * Test des valeurs limites de la note (0).
   */
  @Test
  void testNoteMinimale() {
    Pizza p = new Pizza("Test", TypePizza.Viande);
    Evaluation e = new Evaluation(p, 0, "Pas terrible");

    assertEquals(0, e.getNote());
  }

  /**
   * Test des valeurs limites de la note (5).
   */
  @Test
  void testNoteMaximale() {
    Pizza p = new Pizza("Test", TypePizza.Viande);
    Evaluation e = new Evaluation(p, 5, "Parfait");

    assertEquals(5, e.getNote());
  }

  /* ========================= */
  /* ===== CAS INVALIDES ===== */
  /* ========================= */

  /**
   * Test : pizza nulle (interdit).
   */
  @Test
  void testPizzaNulle() {
    assertThrows(IllegalArgumentException.class,
        () -> new Evaluation(null, 4, "Erreur"));
  }

  /**
   * Test : note négative.
   */
  @Test
  void testNoteNegative() {
    Pizza p = new Pizza("Test", TypePizza.Viande);

    assertThrows(IllegalArgumentException.class,
        () -> new Evaluation(p, -1, "Erreur"));
  }

  /**
   * Test : note supérieure à 5.
   */
  @Test
  void testNoteSuperieureA5() {
    Pizza p = new Pizza("Test", TypePizza.Viande);

    assertThrows(IllegalArgumentException.class,
        () -> new Evaluation(p, 6, "Erreur"));
  }

  /**
   * Test : auteur nul (interdit).
   */
  @Test
  void testAuteurNull() {
    Pizza p = new Pizza("Test", TypePizza.Viande);

    assertThrows(IllegalArgumentException.class,
        () -> new Evaluation(p, null, 4, "Erreur"));
  }

  /**
   * Test : auteur vide.
   */
  @Test
  void testAuteurVide() {
    Pizza p = new Pizza("Test", TypePizza.Viande);

    assertThrows(IllegalArgumentException.class,
        () -> new Evaluation(p, "", 4, "Erreur"));
  }

  /**
   * Test : auteur uniquement composé d’espaces.
   */
  @Test
  void testAuteurEspaces() {
    Pizza p = new Pizza("Test", TypePizza.Viande);

    assertThrows(IllegalArgumentException.class,
        () -> new Evaluation(p, "   ", 4, "Erreur"));
  }

  /* ========================= */
  /* ===== AUTRES TESTS ====== */
  /* ========================= */

  /**
   * Test de la méthode toString().
   */
  @Test
  void testToString() {
    Pizza p = new Pizza("Test", TypePizza.Viande);
    Evaluation e = new Evaluation(p, "Alice", 5, "Top");

    String s = e.toString();

    assertTrue(s.contains("Test"));
    assertTrue(s.contains("Alice"));
    assertTrue(s.contains("5"));
    assertTrue(s.contains("Top"));
  }
}
