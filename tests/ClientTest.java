package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pizzas.*;

/**
 * Tests unitaires de la classe {@link pizzas.Client}.
 
 * Ces tests vérifient :
 * - la validation des paramètres du constructeur,
 * - les getters et setters,
 * - la gestion des commandes,
 * - la séparation des commandes en cours et passées,
 * - l'encapsulation de la liste des commandes,
 * - les méthodes equals et hashCode.
 *
 * @author Inas Hallal
 */
public class ClientTest {

  /* ===========================
   * Tests du constructeur
   * =========================== */

  @Test
  void testConstructeurValide() {
    InformationPersonnelle info =
        new InformationPersonnelle("Dupont", "Jean");

    Client c = new Client("a@b.fr", "1234", info);

    assertEquals("a@b.fr", c.getEmail());
    assertEquals("1234", c.getMotDePasse());
    assertEquals(info, c.getInfos());
    assertTrue(c.getCommandes().isEmpty());
  }

  @Test
  void testConstructeurEmailNull() {
    assertThrows(IllegalArgumentException.class,
        () -> new Client(null, "1234",
            new InformationPersonnelle("Dupont", "Jean")));
  }

  @Test
  void testConstructeurEmailVide() {
    assertThrows(IllegalArgumentException.class,
        () -> new Client("   ", "1234",
            new InformationPersonnelle("Dupont", "Jean")));
  }

  @Test
  void testConstructeurMotDePasseNull() {
    assertThrows(IllegalArgumentException.class,
        () -> new Client("a@b.fr", null,
            new InformationPersonnelle("Dupont", "Jean")));
  }

  @Test
  void testConstructeurMotDePasseVide() {
    assertThrows(IllegalArgumentException.class,
        () -> new Client("a@b.fr", " ",
            new InformationPersonnelle("Dupont", "Jean")));
  }

  @Test
  void testConstructeurInfosNull() {
    assertThrows(IllegalArgumentException.class,
        () -> new Client("a@b.fr", "1234", null));
  }

  /* ===========================
   * Tests du setter mot de passe
   * =========================== */

  @Test
  void testSetMotDePasseValide() {
    Client c = new Client("a@b.fr", "1234",
        new InformationPersonnelle("Dupont", "Jean"));

    c.setMotDePasse("abcd");

    assertEquals("abcd", c.getMotDePasse());
  }

  @Test
  void testSetMotDePasseInvalide() {
    Client c = new Client("a@b.fr", "1234",
        new InformationPersonnelle("Dupont", "Jean"));

    assertThrows(IllegalArgumentException.class,
        () -> c.setMotDePasse(""));
  }

  /* ===========================
   * Tests gestion des commandes
   * =========================== */

  @Test
  void testAjoutCommandeValide() {
    InformationPersonnelle info =
        new InformationPersonnelle("Dupont", "Jean");
    Client c = new Client("a@b.fr", "1234", info);

    Commande cmd = new Commande(info);
    c.ajouterCommande(cmd);

    assertEquals(1, c.getCommandes().size());
    assertTrue(c.getCommandes().contains(cmd));
  }

  @Test
  void testAjoutCommandeNull() {
    Client c = new Client("a@b.fr", "1234",
        new InformationPersonnelle("Dupont", "Jean"));

    assertThrows(IllegalArgumentException.class,
        () -> c.ajouterCommande(null));
  }

  /* ===========================
   * Tests commandes en cours / passées
   * =========================== */

  @Test
  void testCommandesPasseesEtEnCours() throws CommandeException {
    InformationPersonnelle info =
        new InformationPersonnelle("Dupont", "Jean");
    Client c = new Client("a@b.fr", "1234", info);

    Commande c1 = new Commande(info);
    c1.valider();
    c1.traiter();
    Commande c2 = new Commande(info);

    c.ajouterCommande(c1);
    c.ajouterCommande(c2);

    assertEquals(1, c.getCommandesPassees().size());
    assertEquals(1, c.getCommandesEnCours().size());
  }

  /* ===========================
   * Test encapsulation
   * =========================== */

  @Test
  void testListeCommandesNonModifiable() {
    Client c = new Client("a@b.fr", "1234",
        new InformationPersonnelle("Dupont", "Jean"));

    assertThrows(UnsupportedOperationException.class,
        () -> c.getCommandes().add(new Commande(c.getInfos())));
  }

  /* ===========================
   * Tests equals / hashCode
   * =========================== */

  @Test
  void testEqualsClient() {
    InformationPersonnelle info =
        new InformationPersonnelle("Dupont", "Jean");

    Client c1 = new Client("a@b.fr", "1234", info);
    Client c2 = new Client("a@b.fr", "abcd", info);

    assertEquals(c1, c2);
    assertEquals(c1.hashCode(), c2.hashCode());
  }

  @Test
  void testNotEqualsClient() {
    InformationPersonnelle info =
        new InformationPersonnelle("Dupont", "Jean");

    Client c1 = new Client("a@b.fr", "1234", info);
    Client c2 = new Client("b@c.fr", "1234", info);

    assertNotEquals(c1, c2);
  }
}
