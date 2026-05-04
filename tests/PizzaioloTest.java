package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import pizzas.*;



/**
 * Tests unitaires de la classe {@link pizzas.Pizzaiolo}.
 
 * Ces tests couvrent : - la gestion des ingrédients, - la création et la
 * composition des pizzas, - la gestion des interdictions, - la gestion des
 * prix, - le traitement des commandes, - les statistiques principales.
 *
 * @author Lina Sidennas
 */
public class PizzaioloTest {

  /*
   * ======================= INGREDIENTS =======================
   */

  @Test
  public void testCreerIngredient() {
    Pizzaiolo p = new Pizzaiolo(new GestionCommande());

    assertEquals(0, p.creerIngredient("Tomate", 1.0));
    assertEquals(-2, p.creerIngredient("Tomate", 1.0)); // doublon
    assertEquals(-1, p.creerIngredient(null, 1.0));
    assertEquals(-1, p.creerIngredient("   ", 1.0));
    assertEquals(-3, p.creerIngredient("Fromage", 0));
  }

  @Test
  public void testChangerPrixIngredient() {
    Pizzaiolo p = new Pizzaiolo(new GestionCommande());

    p.creerIngredient("Fromage", 1.0);

    assertEquals(0, p.changerPrixIngredient("Fromage", 2.0));
    assertEquals(-2, p.changerPrixIngredient("Fromage", -1));
    assertEquals(-3, p.changerPrixIngredient("Inconnu", 1.0));
    assertEquals(-1, p.changerPrixIngredient(null, 1.0));
  }

  /*
   * ======================= PIZZAS =======================
   */

  @Test
  public void testCreerPizza() {
    Pizzaiolo p = new Pizzaiolo(new GestionCommande());

    Pizza pizza = p.creerPizza("Reine", TypePizza.Viande);
    assertNotNull(pizza);

    assertNull(p.creerPizza("Reine", TypePizza.Viande)); // doublon
    assertNull(p.creerPizza("   ", TypePizza.Viande));
    assertNull(p.creerPizza(null, TypePizza.Viande));
  }

  @Test
  public void testAjouterEtRetirerIngredientPizza() {
    Pizzaiolo p = new Pizzaiolo(new GestionCommande());

    p.creerIngredient("Tomate", 1.0);
    p.creerIngredient("Fromage", 1.5);

    Pizza pizza = p.creerPizza("Margherita", TypePizza.Vegetarienne);

    assertEquals(0, p.ajouterIngredientPizza(pizza, "Tomate"));
    assertEquals(0, p.ajouterIngredientPizza(pizza, "Fromage"));

    assertEquals(0, p.retirerIngredientPizza(pizza, "Fromage"));
    assertEquals(-3, p.retirerIngredientPizza(pizza, "Fromage")); // déjà retiré
    assertEquals(-2, p.retirerIngredientPizza(pizza, "Inconnu"));
  }

  @Test
  public void testInterdictionIngredient() {
    Pizzaiolo p = new Pizzaiolo(new GestionCommande());

    p.creerIngredient("Jambon", 1.0);
    Pizza pizza = p.creerPizza("Veggie", TypePizza.Vegetarienne);

    assertTrue(p.interdireIngredient("Jambon", TypePizza.Vegetarienne));
    assertEquals(-3, p.ajouterIngredientPizza(pizza, "Jambon"));
  }

  @Test
  public void testVerifierIngredientsPizza() {
    Pizzaiolo p = new Pizzaiolo(new GestionCommande());

    p.creerIngredient("Jambon", 1.0);
    Pizza pizza = p.creerPizza("Test", TypePizza.Vegetarienne);

    // 1️⃣ on ajoute l’ingrédient
    assertEquals(0, p.ajouterIngredientPizza(pizza, "Jambon"));

    // 2️⃣ on l’interdit ensuite
    assertTrue(p.interdireIngredient("Jambon", TypePizza.Vegetarienne));

    // 3️⃣ on vérifie
    Set<String> interdits = p.verifierIngredientsPizza(pizza);
    assertNotNull(interdits);
    assertTrue(interdits.contains("Jambon"));
  }

  /*
   * ======================= PRIX =======================
   */

  @Test
  public void testPrixPizza() {
    Pizzaiolo p = new Pizzaiolo(new GestionCommande());

    p.creerIngredient("Tomate", 1.0);
    Pizza pizza = p.creerPizza("Simple", TypePizza.Vegetarienne);
    p.ajouterIngredientPizza(pizza, "Tomate");

    double prixMin = p.calculerPrixMinimalPizza(pizza);
    assertTrue(prixMin > 0);

    assertTrue(p.setPrixPizza(pizza, prixMin + 2));
    assertEquals(prixMin + 2, p.getPrixPizza(pizza));
    assertFalse(p.setPrixPizza(pizza, prixMin - 1));
  }

  /*
   * ======================= COMMANDES =======================
   */

  @Test
  public void testCommandesNonTraitees() throws CommandeException {
    GestionCommande gestion = new GestionCommande();
    Pizzaiolo p = new Pizzaiolo(gestion);

    InformationPersonnelle info = new InformationPersonnelle("A", "B");
    Commande c = new Commande(info);
    c.valider();

    gestion.ajouterCommandeValidee(c);

    List<Commande> liste = p.commandeNonTraitees();
    assertEquals(1, liste.size());
    assertEquals(Commande.EtatCommande.TRAITEE, liste.get(0).getEtat());
  }

  @Test
  public void testStatistiquesSimples() throws CommandeException {
    GestionCommande gestion = new GestionCommande();
    Pizzaiolo p = new Pizzaiolo(gestion);

    p.creerIngredient("Tomate", 1.0);
    Pizza pizza = p.creerPizza("Test", TypePizza.Vegetarienne);
    p.ajouterIngredientPizza(pizza, "Tomate");
    p.setPrixPizza(pizza, pizza.getPrixMinimal() + 2);

    InformationPersonnelle client = new InformationPersonnelle("Jean", "Dupont");
    Commande c = new Commande(client);
    c.ajouterPizza(pizza, 2);
    c.valider();

    gestion.ajouterCommandeValidee(c);
    p.commandeNonTraitees(); // déclenche le traitement

    List<Commande> traitees = p.commandesDejaTraitees();
    assertEquals(1, traitees.size());

    assertEquals(-1, p.beneficeCommandes(traitees.get(0)));

  }

}
