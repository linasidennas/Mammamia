package pizzas;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implémentation de l’interface {@link InterPizzaiolo}.
 
 * Cette classe représente le rôle du pizzaïolo dans l’application. Elle permet
 * de gérer : - les ingrédients (création, modification de prix, interdictions),
 * - les pizzas (création, composition, prix, photos), - les commandes (en
 * attente et traitées), - les statistiques globales et par client.
 
 * Elle s’appuie sur la classe {@link GestionCommande} pour la gestion
 * centralisée des commandes validées par les clients.
 *
 * @author Lina Sidennas
 */
public class Pizzaiolo implements InterPizzaiolo {

  /** Ingrédients disponibles indexés par leur nom. */
  private final Map<String, Ingredient> ingredients = new HashMap<>();

  /** Ensemble des pizzas disponibles. */
  private final Set<Pizza> pizzas = new HashSet<>();


  /** Liste des commandes déjà traitées. */
  private final List<Commande> commandesTraitees = new ArrayList<>();

  /** Interdictions d’ingrédients selon le type de pizza. */
  private final Map<TypePizza, Set<String>> interdits = new HashMap<>();

  /** Ensemble des clients ayant passé au moins une commande. */
  private final Set<InformationPersonnelle> clients = new HashSet<>();

  /** Gestionnaire centralisé des commandes. */
  private final GestionCommande gestionCommande;

  /**
   * Construit un pizzaïolo avec un gestionnaire de commandes.
   *
   * @param gestionCommande gestionnaire centralisé des commandes
   * @throws IllegalArgumentException si gestionCommande est null
   */
  public Pizzaiolo(GestionCommande gestionCommande) {
    this.gestionCommande = gestionCommande;
    for (TypePizza t : TypePizza.values()) {
      interdits.put(t, new HashSet<>());
    }
  }

  /**
   * Crée un nouvel ingrédient.
   *
   * @param nom  nom de l’ingrédient
   * @param prix prix de l’ingrédient
   * @return 0 si succès, code d’erreur sinon
   */
  @Override
  public int creerIngredient(String nom, double prix) {
    if (nom == null || nom.isBlank()) {
      return -1;
    }
    if (prix <= 0) {
      return -3;
    }
    if (ingredients.containsKey(nom)) {
      return -2;
    }

    Ingredient i = new Ingredient(nom, prix);
    ingredients.put(nom, i);
    return 0;
  }

  /**
   * Modifie le prix d’un ingrédient existant.
   *
   * @param nom  nom de l’ingrédient
   * @param prix nouveau prix
   * @return 0 si succès, code d’erreur sinon
   */
  @Override
  public int changerPrixIngredient(String nom, double prix) {
    if (nom == null || nom.isBlank()) {
      return -1;
    }
    if (prix <= 0) {
      return -2;
    }
    Ingredient ing = ingredients.get(nom);
    if (ing == null) {
      return -3;
    }

    ing.setPrix(prix);
    return 0;
  }

  /**
   * Interdit un ingrédient pour un type de pizza donné.
   *
   * @param nomIngredient nom de l’ingrédient
   * @param type          type de pizza
   * @return true si l’interdiction est ajoutée
   */
  @Override
  public boolean interdireIngredient(String nomIngredient, TypePizza type) {
    if (!ingredients.containsKey(nomIngredient) || type == null) {
      return false;
    }
    return interdits.get(type).add(nomIngredient);
  }

  /**
   * Crée une nouvelle pizza.
   *
   * @param nom  nom de la pizza
   * @param type type de la pizza
   * @return la pizza créée ou null si échec
   */
  @Override
  public Pizza creerPizza(String nom, TypePizza type) {
    if (nom == null || nom.isBlank()) {
      return null;
    }
    for (Pizza p : pizzas) {
      if (p.getNom().equalsIgnoreCase(nom)) {
        return null;
      }
    }

    Pizza p = new Pizza(nom, type);
    pizzas.add(p);
    return p;
  }

  /**
   * Ajoute un ingrédient à une pizza.
   *
   * @param pizza  pizza concernée
   * @param nomIng nom de l’ingrédient
   * @return 0 si succès, code d’erreur sinon
   */
  @Override
  public int ajouterIngredientPizza(Pizza pizza, String nomIng) {
    if (pizza == null || !pizzas.contains(pizza)) {
      return -1;
    }
    if (nomIng == null || nomIng.isBlank() || !ingredients.containsKey(nomIng)) {
      return -2;
    }
    if (interdits.get(pizza.getType()).contains(nomIng)) {
      return -3;
    }

    pizza.ajouterIngredient(ingredients.get(nomIng));
    return 0;
  }

  /**
   * Retire un ingrédient d’une pizza.
   *
   * @param pizza  pizza concernée
   * @param nomIng nom de l’ingrédient
   * @return 0 si succès, code d’erreur sinon
   */
  @Override
  public int retirerIngredientPizza(Pizza pizza, String nomIng) {
    if (pizza == null || !pizzas.contains(pizza)) {
      return -1;
    }
    if (nomIng == null || nomIng.isBlank() || !ingredients.containsKey(nomIng)) {
      return -2;
    }

    Ingredient ing = ingredients.get(nomIng);
    if (!pizza.getIngredients().contains(ing)) {
      return -3;
    }

    pizza.retirerIngredient(ing);
    return 0;
  }

  /**
   * Vérifie les ingrédients interdits présents dans une pizza.
   *
   * @param pizza pizza concernée
   * @return ensemble des ingrédients interdits
   */
  @Override
  public Set<String> verifierIngredientsPizza(Pizza pizza) {
    if (pizza == null || !pizzas.contains(pizza)) {
      return null;
    }

    Set<String> interditsPizza = interdits.get(pizza.getType());
    Set<String> result = new HashSet<>();

    for (Ingredient ing : pizza.getIngredients()) {
      if (interditsPizza.contains(ing.getNom())) {
        result.add(ing.getNom());
      }
    }

    return result;
  }

  /**
   * Ajoute une photo à une pizza.
   *
   * @param pizza pizza concernée
   * @param file  chemin du fichier image
   * @return true si la photo est ajoutée
   * @throws IOException si erreur d’accès au fichier
   */
  @Override
  public boolean ajouterPhoto(Pizza pizza, String file) throws IOException {
    if (pizza == null || !pizzas.contains(pizza)) {
      return false;
    }
    File f = new File(file);

    if (!f.exists() || f.isDirectory()) {
      return false;
    }

    pizza.setCheminPhoto(file);
    return true;
  }

  /**
   * Retourne le prix d’une pizza.
   *
   * @param pizza pizza concernée
   * @return prix de vente ou prix minimal
   */
  @Override
  public double getPrixPizza(Pizza pizza) {
    if (pizza == null || !pizzas.contains(pizza)) {
      return -1;
    }
    double prix = pizza.getPrixVente();
    if (prix == 0) {
      return pizza.getPrixMinimal();
    }
    return prix;
  }

  /**
   * Fixe le prix de vente d’une pizza.
   *
   * @param pizza pizza concernée
   * @param prix  nouveau prix
   * @return true si le prix est fixé
   */
  @Override
  public boolean setPrixPizza(Pizza pizza, double prix) {
    if (pizza == null || !pizzas.contains(pizza)) {
      return false;
    }
    double min = pizza.getPrixMinimal();
    if (prix < min) {
      return false;
    }
    pizza.setPrixVente(prix);
    return true;
  }

  /**
   * Calcule le prix minimal d’une pizza.
   *
   * @param pizza pizza concernée
   * @return prix minimal
   */
  @Override
  public double calculerPrixMinimalPizza(Pizza pizza) {
    if (pizza == null || !pizzas.contains(pizza)) {
      return -1;
    }
    return pizza.getPrixMinimal();
  }

  /**
   * Retourne toutes les pizzas disponibles.
   *
   * @return ensemble des pizzas
   */
  @Override
  public Set<Pizza> getPizzas() {
    return new HashSet<>(pizzas);
  }

  /**
   * Retourne l’ensemble des clients ayant commandé.
   *
   * @return ensemble des clients
   */
  @Override
  public Set<InformationPersonnelle> ensembleClients() {
    return new HashSet<>(clients);
  }

  /**
   * Retourne les commandes non traitées et les marque comme traitées.
   *
   * @return liste des commandes non traitées
   */
  @Override
  public List<Commande> commandeNonTraitees() {
    return gestionCommande.recupererCommandesNonTraitees();
  }

  /**
   * Retourne les commandes déjà traitées.
   *
   * @return liste des commandes traitées
   */
  @Override
  public List<Commande> commandesDejaTraitees() {
    return gestionCommande.getCommandesTraitees();
  }

  /**
   * Retourne les commandes traitées d’un client.
   *
   * @param client client concerné
   * @return liste des commandes du client
   */
  @Override
  public List<Commande> commandesTraiteesClient(InformationPersonnelle client) {
    return gestionCommande.getCommandesClient(client);
  }

  /**
   * Calcule le bénéfice unitaire par pizza.
   *
   * @return map pizza → bénéfice unitaire
   */
  @Override
  public Map<Pizza, Double> beneficeParPizza() {
    Map<Pizza, Double> res = new HashMap<>();

    for (Pizza p : pizzas) {
      res.put(p, p.getBeneficeUnitaire());
    }

    return res;
  }

  /**
   * Calcule le bénéfice d’une commande.
   *
   * @param commande commande concernée
   * @return bénéfice ou -1 si invalide
   */
  @Override
  public double beneficeCommandes(Commande commande) {
    if (commande == null || !commandesTraitees.contains(commande)) {
      return -1;
    }
    double total = 0;
    for (Map.Entry<Pizza, Integer> e : commande.getPizzas().entrySet()) {
      total += e.getKey().getBeneficeUnitaire() * e.getValue();
    }
    return total;
  }

  /**
   * Calcule le bénéfice total de toutes les commandes.
   *
   * @return bénéfice total
   */
  @Override
  public double beneficeToutesCommandes() {
    double total = 0;
    for (Commande c : commandesTraitees) {
      total += beneficeCommandes(c);
    }
    return total;
  }

  /**
   * Calcule le nombre de pizzas commandées par client.
   *
   * @return map client → nombre de pizzas
   */
  @Override
  public Map<InformationPersonnelle, Integer> nombrePizzasCommandeesParClient() {
    Map<InformationPersonnelle, Integer> res = new HashMap<>();

    for (Commande c : commandesTraitees) {
      InformationPersonnelle cli = c.getClient();
      int count = res.getOrDefault(cli, 0);

      for (int n : c.getPizzas().values()) {
        count += n;
      }

      res.put(cli, count);
    }

    return res;
  }

  /**
   * Calcule le bénéfice total par client.
   *
   * @return map client → bénéfice
   */
  @Override
  public Map<InformationPersonnelle, Double> beneficeParClient() {
    Map<InformationPersonnelle, Double> res = new HashMap<>();

    for (Commande c : commandesTraitees) {
      InformationPersonnelle cli = c.getClient();
      double benef = res.getOrDefault(cli, 0.0);
      benef += beneficeCommandes(c);
      res.put(cli, benef);
    }

    return res;
  }

  /**
   * Retourne le nombre total de pizzas commandées pour une pizza donnée.
   *
   * @param pizza pizza concernée
   * @return nombre de pizzas commandées
   */
  @Override
  public int nombrePizzasCommandees(Pizza pizza) {
    if (pizza == null || !pizzas.contains(pizza)) {
      return -1;
    }

    int total = 0;
    for (Commande c : commandesTraitees) {
      for (Map.Entry<Pizza, Integer> e : c.getPizzas().entrySet()) {
        if (e.getKey().equals(pizza)) {
          total += e.getValue();
        }
      }
    }

    return total;
  }

  /**
   * Classe les pizzas par nombre de commandes décroissant.
   *
   * @return liste des pizzas triées
   */
  @Override
  public List<Pizza> classementPizzasParNombreCommandes() {
    List<Pizza> list = new ArrayList<>(pizzas);

    list.sort((p1, p2) -> Integer.compare(nombrePizzasCommandees(p2), nombrePizzasCommandees(p1)));

    return list;
  }
}
