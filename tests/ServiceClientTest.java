package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import pizzas.*;



/**
 * Classe de tests unitaires pour la classe {@link pizzas.ServiceClient}.
 
 * Ces tests vérifient : - l’inscription et la connexion des clients, - la
 * gestion des erreurs de connexion, - le cycle de vie des commandes (création,
 * ajout, validation, annulation), - l’accès aux commandes en cours et passées,
 * - l’application des filtres sur le catalogue, - l’ajout d’évaluations par un
 * client ayant commandé.
 
 * Tests réalisés avec JUnit 5.
 *
 * @author Inas Hallal
 */
public class ServiceClientTest {

  /*
   * ======================= INSCRIPTION / CONNEXION =======================
   */

  @Test
  public void testInscriptionEtConnexion() {
    ServiceClient sc = new ServiceClient(new GestionCommande(), new HashSet<>());

    InformationPersonnelle info = new InformationPersonnelle("Doe", "John");

    assertEquals(0, sc.inscription("a@a", "1234", info));
    assertEquals(1, sc.inscription("a@a", "xxxx", info)); // email déjà utilisé

    assertTrue(sc.connexion("a@a", "1234"));
    assertFalse(sc.connexion("a@a", "mauvais"));
    assertFalse(sc.connexion("inconnu", "1234"));
  }

  @Test
  public void testDeconnexion() throws Exception {
    ServiceClient sc = new ServiceClient(new GestionCommande(), new HashSet<>());

    InformationPersonnelle info = new InformationPersonnelle("Doe", "John");

    sc.inscription("a@a", "1234", info);
    sc.connexion("a@a", "1234");

    sc.deconnexion();

    assertThrows(NonConnecteException.class, () -> sc.debuterCommande());
  }

  /*
   * ======================= COMMANDES =======================
   */

  @Test
  public void testDebuterCommande() throws Exception {
    ServiceClient sc = new ServiceClient(new GestionCommande(), new HashSet<>());

    InformationPersonnelle info = new InformationPersonnelle("Doe", "John");

    sc.inscription("a@a", "1234", info);
    sc.connexion("a@a", "1234");

    Commande c = sc.debuterCommande();
    assertNotNull(c);
    assertEquals(Commande.EtatCommande.CREEE, c.getEtat());
  }

  @Test
  public void testDebuterCommandeSansConnexion() {
    ServiceClient sc = new ServiceClient(new GestionCommande(), new HashSet<>());

    assertThrows(NonConnecteException.class, () -> sc.debuterCommande());
  }

  @Test
  public void testAjouterEtValiderCommande() throws Exception {

    Pizza pizza = new Pizza("Test", TypePizza.Vegetarienne);
    pizza.ajouterIngredient(new Ingredient("Fromage", 2.0));
    pizza.setPrixVente(pizza.getPrixMinimal() + 2);

    Set<Pizza> catalogue = new HashSet<>();
    catalogue.add(pizza);
    GestionCommande gestion = new GestionCommande();

    ServiceClient sc = new ServiceClient(gestion, catalogue);

    InformationPersonnelle info = new InformationPersonnelle("Doe", "John");

    sc.inscription("a@a", "1234", info);
    sc.connexion("a@a", "1234");

    Commande c = sc.debuterCommande();
    sc.ajouterPizza(pizza, 2, c);
    sc.validerCommande(c);

    // le pizzaiolo traite la commande
    Pizzaiolo pizzaiolo = new Pizzaiolo(gestion);
    pizzaiolo.commandeNonTraitees();

    List<Commande> passees = sc.getCommandePassees();
    assertEquals(1, passees.size());
    assertEquals(Commande.EtatCommande.TRAITEE, passees.get(0).getEtat());

  }

  @Test
  public void testAnnulerCommande() throws Exception {
    ServiceClient sc = new ServiceClient(new GestionCommande(), new HashSet<>());

    InformationPersonnelle info = new InformationPersonnelle("Doe", "John");

    sc.inscription("a@a", "1234", info);
    sc.connexion("a@a", "1234");

    Commande c = sc.debuterCommande();
    sc.annulerCommande(c);

    assertNotEquals(Commande.EtatCommande.VALIDEE, c.getEtat());
  }

  /*
   * ======================= FILTRES =======================
   */

  @Test
  public void testFiltresCatalogue() {
    Pizza p1 = new Pizza("V1", TypePizza.Vegetarienne);
    p1.ajouterIngredient(new Ingredient("Fromage", 2.0));
    p1.setPrixVente(5.0);

    Pizza p2 = new Pizza("V2", TypePizza.Viande);
    p2.ajouterIngredient(new Ingredient("Jambon", 3.0));
    p2.setPrixVente(8.0);

    Set<Pizza> catalogue = new HashSet<>();
    catalogue.add(p1);
    catalogue.add(p2);

    ServiceClient sc = new ServiceClient(new GestionCommande(), catalogue);

    sc.ajouterFiltre(TypePizza.Vegetarienne);
    Set<Pizza> res = sc.selectionPizzaFiltres();

    assertEquals(1, res.size());
    assertTrue(res.contains(p1));

    sc.supprimerFiltres();
    sc.ajouterFiltre(6.0);
    res = sc.selectionPizzaFiltres();

    assertEquals(1, res.size());
    assertTrue(res.contains(p1));
  }

  /*
   * ======================= EVALUATIONS =======================
   */

  @Test
  public void testAjouterEvaluationValide() throws Exception {

    Pizza pizza = new Pizza("Test", TypePizza.Vegetarienne);
    pizza.ajouterIngredient(new Ingredient("Fromage", 2.0));
    pizza.setPrixVente(pizza.getPrixMinimal() + 2);

    Set<Pizza> catalogue = new HashSet<>();
    catalogue.add(pizza);
    GestionCommande gestion = new GestionCommande();

    ServiceClient sc = new ServiceClient(gestion, catalogue);

    InformationPersonnelle info = new InformationPersonnelle("Doe", "John");

    sc.inscription("a@a", "1234", info);
    sc.connexion("a@a", "1234");

    Commande c = sc.debuterCommande();
    sc.ajouterPizza(pizza, 1, c);
    sc.validerCommande(c);

    assertTrue(sc.ajouterEvaluation(pizza, 5, "Excellent"));
    assertEquals(5.0, sc.getNoteMoyenne(pizza));
  }

  @Test
  public void testAjouterEvaluationSansCommande() throws Exception {
    Pizza pizza = new Pizza("Test", TypePizza.Vegetarienne);

    Set<Pizza> catalogue = new HashSet<>();
    catalogue.add(pizza);

    ServiceClient sc = new ServiceClient(new GestionCommande(), catalogue);

    InformationPersonnelle info = new InformationPersonnelle("Doe", "John");

    sc.inscription("a@a", "1234", info);
    sc.connexion("a@a", "1234");

    assertThrows(CommandeException.class, () -> sc.ajouterEvaluation(pizza, 3, "Bof"));
  }
}
