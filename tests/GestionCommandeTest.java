package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pizzas.*;
/**
 * Classe de tests unitaires pour la classe GestionCommande.
 *
 * Cette classe vérifie le bon fonctionnement du mécanisme central
 * de gestion des commandes de l'application.
 *
 * Les tests couvrent notamment :
 * - le respect du patron de conception Singleton,
 * - l’ajout des commandes validées uniquement,
 * - le refus des commandes non validées ou nulles,
 * - le traitement automatique des commandes,
 * - le nettoyage des commandes à traiter après traitement,
 * - la récupération des commandes traitées d’un client,
 * - le comportement pour un client sans commande.
 *
 * Les tests sont écrits avec JUnit 5 et tiennent compte du fait que
 * GestionCommande est implémentée comme un Singleton.
 *
 * @author Inas Hallal
 */
public class GestionCommandeTest {

  /** Instance unique de gestion des commandes (Singleton). */
  private GestionCommande gestion;

  /** Premier client utilisé dans les tests. */
  private InformationPersonnelle client1;

  /** Second client utilisé dans les tests. */
  private InformationPersonnelle client2;

  /**
   * Méthode exécutée avant chaque test.
   *
   * Elle permet de récupérer l’instance unique de GestionCommande,
   * de traiter toutes les commandes restantes afin d’éviter
   * les effets de bord liés au Singleton,
   * et d’initialiser des clients de test.
   */
  @BeforeEach
  void setUp() {
    gestion = GestionCommande.getInstance();
    gestion.recupererCommandesNonTraitees();

    client1 = new InformationPersonnelle("Dupont", "Jean");
    client2 = new InformationPersonnelle("Martin", "Alice");
  }

  /**
   * Vérifie que la méthode getInstance retourne toujours
   * la même instance de GestionCommande.
   *
   * Ce test valide l’implémentation correcte du patron Singleton.
   */
  @Test
  void testSingleton() {
    GestionCommande g1 = GestionCommande.getInstance();
    GestionCommande g2 = GestionCommande.getInstance();

    assertSame(g1, g2);
  }

  /**
   * Vérifie qu’une commande validée est bien ajoutée
   * à la liste des commandes à traiter.
   *
   * @throws CommandeException en cas d’erreur lors de la validation
   */
  @Test
  void testAjoutCommandeValidee() throws CommandeException {
    Commande c = new Commande(client1);
    c.valider();

    gestion.ajouterCommandeValidee(c);

    List<Commande> nonTraitees = gestion.recupererCommandesNonTraitees();
    assertEquals(1, nonTraitees.size());
  }

  /**
   * Vérifie qu’une commande non validée n’est pas ajoutée
   * à la liste des commandes à traiter.
   */
  @Test
  void testRefusCommandeNonValidee() {
    Commande c = new Commande(client1);

    gestion.ajouterCommandeValidee(c);

    assertTrue(gestion.recupererCommandesNonTraitees().isEmpty());
  }

  /**
   * Vérifie qu’une commande nulle est ignorée
   * et ne provoque aucune erreur.
   */
  @Test
  void testAjoutCommandeNulle() {
    gestion.ajouterCommandeValidee(null);

    assertTrue(gestion.recupererCommandesNonTraitees().isEmpty());
  }

  /**
   * Vérifie que les commandes récupérées sont automatiquement
   * marquées comme traitées.
   *
   * @throws CommandeException en cas d’erreur lors de la validation
   */
  @Test
  void testTraitementAutomatique() throws CommandeException {
    Commande c = new Commande(client1);
    c.valider();

    gestion.ajouterCommandeValidee(c);
    List<Commande> nonTraitees = gestion.recupererCommandesNonTraitees();

    assertEquals(Commande.EtatCommande.TRAITEE, nonTraitees.get(0).getEtat());
  }

  /**
   * Vérifie que la liste des commandes à traiter
   * est vidée après le traitement.
   *
   * @throws CommandeException en cas d’erreur lors de la validation
   */
  @Test
  void testNettoyageCommandesATraiter() throws CommandeException {
    Commande c = new Commande(client1);
    c.valider();

    gestion.ajouterCommandeValidee(c);
    gestion.recupererCommandesNonTraitees();

    assertTrue(gestion.recupererCommandesNonTraitees().isEmpty());
  }

  /**
   * Vérifie que les commandes traitées d’un client donné
   * sont correctement récupérées.
   *
   * @throws CommandeException en cas d’erreur lors de la validation
   */
  @Test
  void testGetCommandesClient() throws CommandeException {
    Commande c1 = new Commande(client1);
    Commande c2 = new Commande(client2);

    c1.valider();
    c2.valider();

    gestion.ajouterCommandeValidee(c1);
    gestion.ajouterCommandeValidee(c2);
    gestion.recupererCommandesNonTraitees();

    List<Commande> commandesClient1 =
        gestion.getCommandesClient(client1);

    assertEquals(1, commandesClient1.size());
    assertEquals(client1, commandesClient1.get(0).getClient());
  }

  /**
   * Vérifie qu’un client n’ayant jamais passé de commande
   * obtient une liste vide.
   */
  @Test
  void testClientSansCommandes() {
    InformationPersonnelle clientSansCommande =
        new InformationPersonnelle("Durand", "Paul");

    List<Commande> commandes =
        gestion.getCommandesClient(clientSansCommande);

    assertNotNull(commandes);
    assertTrue(commandes.isEmpty());
  }
}
