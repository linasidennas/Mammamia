package pizzas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implémentation du service client permettant à un utilisateur de s’inscrire,
 * se connecter, gérer ses commandes, consulter le catalogue des pizzas,
 * appliquer des filtres et laisser des évaluations.
 
 * Cette classe implémente l’interface {@link InterClient} et constitue la
 * couche métier utilisée par l’interface graphique côté client.
 
 * Un seul client peut être connecté à la fois.
 *
 * @author Kamel BELKHIR
 */
public class ServiceClient implements InterClient {

  /** Gestionnaire central des commandes validées. */
  private final GestionCommande gestionCommande;

  /** Liste de tous les clients inscrits dans l’application. */
  private final List<Client> clients = new ArrayList<>();

  /** Catalogue des pizzas disponibles à la commande. */
  private final Set<Pizza> catalogue = new HashSet<>();

  /** Client actuellement connecté. */
  private Client connecte;

  /** Filtre sur le type de pizza. */
  private TypePizza filtreType;

  /** Filtre sur les ingrédients. */
  private Set<String> filtreIngr;

  /** Filtre sur le prix maximum. */
  private Double filtrePrixMax;

  /**
   * Construit un service client avec un gestionnaire de commandes et un catalogue
   * initial de pizzas.
   *
   * @param gestionCommande gestionnaire des commandes validées
   * @param catalogue       ensemble des pizzas disponibles
   */
  public ServiceClient(GestionCommande gestionCommande, Set<Pizza> catalogue) {
    this.gestionCommande = gestionCommande;
    this.catalogue.addAll(catalogue);
  }

  /**
   * Inscrit un nouveau client dans l’application.
   *
   * @param email email du client
   * @param mdp   mot de passe du client
   * @param info  informations personnelles du client
   * @return 0 si l’inscription est réussie, 1 si l’email est déjà utilisé
   */
  @Override
  public int inscription(String email, String mdp, InformationPersonnelle info) {
    for (Client c : clients) {
      if (c.getEmail().equalsIgnoreCase(email)) {
        return 1;
      }
    }
    Client c = new Client(email, mdp, info);
    clients.add(c);
    return 0;
  }

  /**
   * Connecte un client existant à l’application.
   *
   * @param email email du client
   * @param mdp   mot de passe du client
   * @return true si la connexion réussit, false sinon
   */
  @Override
  public boolean connexion(String email, String mdp) {
    for (Client c : clients) {
      if (c.getEmail().equalsIgnoreCase(email) && c.getMotDePasse().equals(mdp)) {
        connecte = c;
        return true;
      }
    }
    return false;
  }

  /**
   * Déconnecte le client actuellement connecté.
   *
   * @throws NonConnecteException si aucun client n’est connecté
   */
  @Override
  public void deconnexion() throws NonConnecteException {
    if (connecte == null) {
      throw new NonConnecteException();
    }
    connecte = null;
  }

  /**
   * Vérifie qu’un client est connecté.
   *
   * @throws NonConnecteException si aucun client n’est connecté
   */
  private void checkConnecte() throws NonConnecteException {
    if (connecte == null) {
      throw new NonConnecteException();
    }
  }

  /**
   * Démarre une nouvelle commande pour le client connecté.
   *
   * @return la nouvelle commande créée
   * @throws NonConnecteException si aucun client n’est connecté
   */
  @Override
  public Commande debuterCommande() throws NonConnecteException {
    checkConnecte();
    Commande c = new Commande(connecte.getInfos());
    connecte.ajouterCommande(c);
    return c;
  }

  /**
   * Ajoute une pizza à une commande existante.
   *
   * @param pizza  pizza à ajouter
   * @param nombre quantité
   * @param cmd    commande concernée
   * @throws NonConnecteException si aucun client n’est connecté
   * @throws CommandeException    si la commande n’appartient pas au client
   */
  @Override
  public void ajouterPizza(
        Pizza pizza, int nombre, Commande cmd) throws NonConnecteException, CommandeException {
    checkConnecte();

    if (!connecte.getCommandes().contains(cmd)) {
      throw new CommandeException();
    }

    cmd.ajouterPizza(pizza, nombre);
  }

  /**
   * Valide une commande en cours.
   *
   * @param cmd commande à valider
   * @throws NonConnecteException si aucun client n’est connecté
   * @throws CommandeException    si la commande est invalide
   */
  @Override
  public void validerCommande(Commande cmd) throws NonConnecteException, CommandeException {
    checkConnecte();

    if (!connecte.getCommandes().contains(cmd)) {
      throw new CommandeException();
    }

    cmd.valider();
    gestionCommande.ajouterCommandeValidee(cmd);
  }

  /**
   * Annule une commande en cours.
   *
   * @param cmd commande à annuler
   * @throws NonConnecteException si aucun client n’est connecté
   * @throws CommandeException    si la commande est invalide
   */
  @Override
  public void annulerCommande(Commande cmd) throws NonConnecteException, CommandeException {
    checkConnecte();

    if (!connecte.getCommandes().contains(cmd)) {
      throw new CommandeException();
    }

    cmd.annuler();
  }

  /**
   * Retourne les commandes en cours du client connecté.
   *
   * @return liste des commandes en cours
   * @throws NonConnecteException si aucun client n’est connecté
   */
  @Override
  public List<Commande> getCommandesEncours() throws NonConnecteException {
    checkConnecte();
    return connecte.getCommandesEnCours();
  }

  /**
   * Retourne les commandes passées du client connecté.
   *
   * @return liste des commandes traitées
   * @throws NonConnecteException si aucun client n’est connecté
   */
  @Override
  public List<Commande> getCommandePassees() throws NonConnecteException {
    checkConnecte();
    return connecte.getCommandesPassees();
  }

  /**
   * Retourne l’ensemble des pizzas du catalogue.
   *
   * @return ensemble des pizzas disponibles
   */
  @Override
  public Set<Pizza> getPizzas() {
    return new HashSet<>(catalogue);
  }

  /**
   * Ajoute un filtre sur le type de pizza.
   *
   * @param type type de pizza
   */
  @Override
  public void ajouterFiltre(TypePizza type) {
    this.filtreType = type;
  }

  /**
   * Ajoute un filtre sur les ingrédients.
   *
   * @param ingredients noms des ingrédients
   */
  @Override
  public void ajouterFiltre(String... ingredients) {
    if (filtreIngr == null) {
      filtreIngr = new HashSet<>();
    }
    for (String s : ingredients) {
      filtreIngr.add(s);
    }
  }

  /**
   * Ajoute un filtre sur le prix maximum.
   *
   * @param prixMaximum prix maximum autorisé
   */
  @Override
  public void ajouterFiltre(double prixMaximum) {
    filtrePrixMax = prixMaximum;
  }

  /**
   * Retourne les pizzas correspondant aux filtres actifs.
   *
   * @return ensemble de pizzas filtrées
   */
  @Override
  public Set<Pizza> selectionPizzaFiltres() {
    Set<Pizza> res = new HashSet<>(catalogue);

    if (filtreType != null) {
      res.removeIf(p -> p.getType() != filtreType);
    }

    if (filtreIngr != null && !filtreIngr.isEmpty()) {
      res.removeIf(
           p -> p.getIngredients().stream().noneMatch(i -> filtreIngr.contains(i.getNom())));
    }

    if (filtrePrixMax != null) {
      res.removeIf(p -> p.getPrixVente() > filtrePrixMax);
    }

    return res;
  }

  /**
   * Supprime tous les filtres appliqués.
   */
  @Override
  public void supprimerFiltres() {
    filtreType = null;
    filtreIngr = null;
    filtrePrixMax = null;
  }

  /**
   * Retourne les évaluations associées à une pizza.
   *
   * @param pizza pizza concernée
   * @return ensemble des évaluations
   */
  @Override
  public Set<Evaluation> getEvaluationsPizza(Pizza pizza) {
    return new HashSet<>(pizza.getEvaluations());
  }

  /**
   * Retourne la note moyenne d’une pizza.
   *
   * @param pizza pizza concernée
   * @return note moyenne
   */
  @Override
  public double getNoteMoyenne(Pizza pizza) {
    return pizza.getNoteMoyenne();
  }

  /**
   * Ajoute une évaluation à une pizza si le client l’a commandée.
   *
   * @param pizza       pizza évaluée
   * @param note        note attribuée
   * @param commentaire commentaire optionnel
   * @return true si l’évaluation est ajoutée
   * @throws NonConnecteException si aucun client n’est connecté
   * @throws CommandeException    si la pizza n’a jamais été commandée
   */
  @Override
  public boolean ajouterEvaluation(Pizza pizza, int note, String commentaire)
      throws NonConnecteException, CommandeException {

    checkConnecte();

    boolean aMange =
        connecte.getCommandes().stream().flatMap(cmd -> cmd.getPizzas().keySet().stream())
        .anyMatch(p -> p.equals(pizza));

    if (!aMange) {
      throw new CommandeException();
    }

    Evaluation e = new Evaluation(pizza, note, commentaire);
    pizza.ajouterEvaluation(e);
    return true;
  }
}
