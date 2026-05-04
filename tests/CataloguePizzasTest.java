package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pizzas.*;

/**
 * Classe de tests unitaires pour la classe {@link pizzas.CataloguePizzas}.
 
 * Ces tests vérifient :
 * - l’ajout de pizzas dans le catalogue,
 * - le caractère statique et partagé du catalogue,
 * - le comportement du catalogue lors de l’ajout d’une valeur null.
 
 * Les tests sont écrits avec JUnit 5.
 *
 * @author Inas Hallal
 */
public class CataloguePizzasTest {

  /**
   * Teste l’ajout d’une pizza valide dans le catalogue.
   
   * Le test vérifie que :
   * - le catalogue contient une pizza après l’ajout,
   * - la pizza ajoutée est bien présente dans la liste.
   */
  @Test
  void testAjoutPizzaCatalogue() {
    // on part d’un catalogue vide
    CataloguePizzas.getPizzas().clear();

    Pizza p = new Pizza("Margherita", TypePizza.Vegetarienne);

    CataloguePizzas.ajouterPizza(p);

    assertEquals(1, CataloguePizzas.getPizzas().size());
    assertTrue(CataloguePizzas.getPizzas().contains(p));
  }

  /**
   * Teste le caractère statique et partagé du catalogue.
   
   * Le test vérifie que :
   * - plusieurs ajouts successifs sont conservés,
   * - l’ordre d’insertion des pizzas est respecté.
   */
  @Test
  void testCatalogueStatiquePartage() {
    // nettoyage
    CataloguePizzas.getPizzas().clear();

    Pizza p1 = new Pizza("Reine", TypePizza.Viande);
    Pizza p2 = new Pizza("Fromage", TypePizza.Vegetarienne);

    CataloguePizzas.ajouterPizza(p1);
    CataloguePizzas.ajouterPizza(p2);

    assertEquals(2, CataloguePizzas.getPizzas().size());
    assertEquals(p1, CataloguePizzas.getPizzas().get(0));
    assertEquals(p2, CataloguePizzas.getPizzas().get(1));
  }

  /**
   * Teste l’ajout d’une valeur null dans le catalogue.
   
   * Ce test vérifie le comportement actuel du catalogue
   * lorsque l’on ajoute une pizza null.
   */
  @Test
  void testAjoutPizzaNull() {
    CataloguePizzas.getPizzas().clear();

    CataloguePizzas.ajouterPizza(null);

    assertEquals(1, CataloguePizzas.getPizzas().size());
    assertNull(CataloguePizzas.getPizzas().get(0));
  }
}
