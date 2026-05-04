package pizzas;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe centrale de gestion des commandes de l’application.
 
 * Cette classe est responsable du suivi du cycle de vie des commandes validées
 * par les clients et traitées par le pizzaïolo.
 
 * Elle gère : - les commandes validées en attente de traitement, - les
 * commandes déjà traitées, - l’accès aux commandes d’un client donné.
 
 * Cette classe est implémentée selon le patron Singleton afin de garantir une
 * instance unique partagée dans toute l’application.
 *
 * @author Kamel BELKHIR
 */
public class GestionCommande {

  /** Instance unique de gestion des commandes. */
  private static final GestionCommande INSTANCE = new GestionCommande();

  /** Liste des commandes validées en attente de traitement. */
  private final List<Commande> commandesAtraiter = new ArrayList<>();

  /** Liste des commandes déjà traitées. */
  private final List<Commande> commandesTraitees = new ArrayList<>();

  /**
   * Ajoute une commande validée à la liste des commandes à traiter.
   
   * Seules les commandes dans l’état VALIDEE peuvent être ajoutées.
   *
   * @param c commande validée à ajouter
   */
  public void ajouterCommandeValidee(Commande c) {
    if (c != null && c.getEtat() == Commande.EtatCommande.VALIDEE) {
      commandesAtraiter.add(c);
    }
  }

  /**
   * Récupère les commandes non encore traitées.
   
   * Toutes les commandes récupérées sont automatiquement marquées comme traitées
   * et déplacées dans la liste correspondante.
   *
   * @return liste des commandes précédemment non traitées
   */
  public List<Commande> recupererCommandesNonTraitees() {
    List<Commande> res = new ArrayList<>(commandesAtraiter);
    for (Commande c : commandesAtraiter) {
      c.traiter();
      commandesTraitees.add(c);
    }
    commandesAtraiter.clear();
    return res;
  }

  /**
   * Retourne la liste des commandes déjà traitées.
   *
   * @return liste des commandes traitées
   */
  public List<Commande> getCommandesTraitees() {
    return new ArrayList<>(commandesTraitees);
  }

  /**
   * Retourne les commandes traitées associées à un client donné.
   *
   * @param client client concerné
   * @return liste des commandes traitées du client
   */
  public List<Commande> getCommandesClient(InformationPersonnelle client) {
    List<Commande> res = new ArrayList<>();
    for (Commande c : commandesTraitees) {
      if (c.getClient().equals(client)) {
        res.add(c);
      }
    }
    return res;
  }

  /**
   * Retourne l’instance unique de la classe GestionCommande.
   *
   * @return instance unique de gestion des commandes
   */
  public static GestionCommande getInstance() {
    return INSTANCE;
  }
}
