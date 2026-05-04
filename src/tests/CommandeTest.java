package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pizzas.*;

/**
 * Tests unitaires de la classe {@link pizzas.Commande}.
 
 * Ces tests vérifient :
 * - le cycle de vie d’une commande,
 * - les règles d’ajout de pizzas,
 * - les cas d’erreurs,
 * - l’annulation,
 * - l’encapsulation,
 * - et les méthodes equals / hashCode.
 *
 * @author Inas Hallal
 */
public class CommandeTest {

  /* ===========================
   * Tests existants (fonctionnels)
   * =========================== */

  /**
   * Test du cycle de vie complet d’une commande.
   */
  @Test
  void testCycleCommande() throws CommandeException {
    InformationPersonnelle info =
        new InformationPersonnelle("Dupont", "Jean");
    Commande c = new Commande(info);

    Pizza p = new Pizza("Test", TypePizza.Viande);
    p.setPrixVente(10.0);

    c.ajouterPizza(p, 2);
    assertEquals(20.0, c.getPrixTotal());

    c.valider();
    assertEquals(Commande.EtatCommande.VALIDEE, c.getEtat());

    c.traiter();
    assertEquals(Commande.EtatCommande.TRAITEE, c.getEtat());
  }

  /**
   * Test ajout de pizza interdit après validation.
   */
  @Test
  void testAjoutPizzaApresValidation() throws CommandeException {
    InformationPersonnelle info =
        new InformationPersonnelle("Dupont", "Jean");
    Commande c = new Commande(info);
    c.valider();

    Pizza p = new Pizza("Test", TypePizza.Viande);

    assertThrows(CommandeException.class,
        () -> c.ajouterPizza(p, 1));
  }

  /* ===========================
   * Tests ajoutés : constructeur
   * =========================== */

  /**
   * Test constructeur avec client null (cas d’erreur).
   */
  @Test
  void testConstructeurClientNull() {
    assertThrows(IllegalArgumentException.class,
        () -> new Commande(null));
  }

  /* ===========================
   * Tests ajoutés : ajouterPizza
   * =========================== */

  /**
   * Test ajout de pizza null (cas d’erreur).
   */
  @Test
  void testAjoutPizzaNull() {
    Commande c = new Commande(
        new InformationPersonnelle("Dupont", "Jean"));

    assertThrows(CommandeException.class,
        () -> c.ajouterPizza(null, 1));
  }

  /**
   * Test ajout avec quantité invalide (0 ou négative).
   */
  @Test
  void testAjoutQuantiteInvalide() {
    Commande c = new Commande(
        new InformationPersonnelle("Dupont", "Jean"));
    Pizza p = new Pizza("Test", TypePizza.Viande);

    assertThrows(CommandeException.class,
        () -> c.ajouterPizza(p, 0));
  }

  /**
   * Test ajout de la même pizza plusieurs fois
   * (vérifie l’addition des quantités).
   */
  @Test
  void testAjoutMemePizzaDeuxFois() throws CommandeException {
    InformationPersonnelle info =
        new InformationPersonnelle("Dupont", "Jean");
    Commande c = new Commande(info);

    Pizza p = new Pizza("Test", TypePizza.Viande);
    p.setPrixVente(5.0);

    c.ajouterPizza(p, 1);
    c.ajouterPizza(p, 2);

    assertEquals(15.0, c.getPrixTotal());
  }

  /* ===========================
   * Tests ajoutés : annulation
   * =========================== */

  /**
   * Test annulation d’une commande en cours.
   */
  @Test
  void testAnnulerCommande() throws CommandeException {
    InformationPersonnelle info =
        new InformationPersonnelle("Dupont", "Jean");
    Commande c = new Commande(info);

    Pizza p = new Pizza("Test", TypePizza.Viande);
    p.setPrixVente(10.0);
    c.ajouterPizza(p, 1);

    c.annuler();

    assertEquals(Commande.EtatCommande.TRAITEE, c.getEtat());
    assertTrue(c.getPizzas().isEmpty());
  }

  /**
   * Test annulation interdite après validation.
   */
  @Test
  void testAnnulerCommandeApresValidation() throws CommandeException {
    Commande c = new Commande(
        new InformationPersonnelle("Dupont", "Jean"));
    c.valider();

    assertThrows(CommandeException.class, c::annuler);
  }

  /* ===========================
   * Tests ajoutés : encapsulation
   * =========================== */

  /**
   * Test que la map retournée par getPizzas()
   * est non modifiable.
   */
  @Test
  void testMapPizzasNonModifiable() {
    Commande c = new Commande(
        new InformationPersonnelle("Dupont", "Jean"));

    assertThrows(UnsupportedOperationException.class,
        () -> c.getPizzas().clear());
  }

  /* ===========================
   * Tests ajoutés : equals / hashCode
   * =========================== */

  /**
   * Test égalité de deux références identiques.
   */
  @Test
  void testEqualsCommande() {
    InformationPersonnelle info =
        new InformationPersonnelle("Dupont", "Jean");

    Commande c1 = new Commande(info);
    Commande c2 = c1;

    assertEquals(c1, c2);
    assertEquals(c1.hashCode(), c2.hashCode());
  }

  /**
   * Test non-égalité de deux commandes différentes.
   */
  @Test
  void testNotEqualsNull() {
    Commande c = new Commande(
        new InformationPersonnelle("Dupont", "Jean"));

    assertNotEquals(c, null);
  }

  @Test
  void testNotEqualsAutreObjet() {
    Commande c = new Commande(
        new InformationPersonnelle("Dupont", "Jean"));

    assertNotEquals(c, "commande");
  }

}
