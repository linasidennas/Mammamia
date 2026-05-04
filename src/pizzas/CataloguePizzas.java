package pizzas;

import java.util.ArrayList;
import java.util.List;

/**
 * Catalogue centralisé des pizzas disponibles dans l’application.
 * Cette classe fournit un accès global à la liste des pizzas proposées à la
 * vente. Elle est conçue comme une classe utilitaire avec uniquement des
 * méthodes statiques.
 * L’instanciation de cette classe est volontairement interdite.
 *
 * @author Lina Sidennas
 */
public class CataloguePizzas {

  /** Liste statique contenant toutes les pizzas du catalogue. */
  private static final List<Pizza> pizzas = new ArrayList<>();

  /**
   * Constructeur privé empêchant l’instanciation de la classe.
   */
  private CataloguePizzas() {
    // empêche l'instanciation
  }

  /**
   * Retourne la liste des pizzas du catalogue.
   *
   * @return liste des pizzas
   */
  public static List<Pizza> getPizzas() {
    return pizzas;
  }

  /**
   * Ajoute une pizza au catalogue.
   *
   * @param p pizza à ajouter
   */
  public static void ajouterPizza(Pizza p) {
    pizzas.add(p);
  }
}
