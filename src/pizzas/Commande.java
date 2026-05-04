package pizzas;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Représente une commande de pizzas passée par un client.
 
 * Une commande possède un client, une date de création, une liste de pizzas
 * avec leurs quantités et un état.
 
 * Les états possibles d'une commande sont : - CREEE : la commande est en cours
 * de création et modifiable - VALIDEE : la commande est validée par le client -
 * TRAITEE : la commande a été traitée par le pizzaïolo
 
 * Une commande est sérialisable afin de permettre la sauvegarde et le
 * chargement des données.
 *
 * @author Kamel BELKHIR
 */
public class Commande implements Serializable {

  /** Identifiant de version pour la sérialisation. */
  private static final long serialVersionUID = 1L;

  /**
   * États possibles d'une commande.
   */
  public enum EtatCommande {
    /** Commande en cours de création. */
    CREEE,

    /** Commande validée par le client. */
    VALIDEE,

    /** Commande traitée par le pizzaïolo. */
    TRAITEE
  }

  /** Client ayant passé la commande. */
  private final InformationPersonnelle client;

  /** Date et heure de création de la commande. */
  private final LocalDateTime dateCreation;

  /** Liste des pizzas commandées avec leurs quantités. */
  private final Map<Pizza, Integer> pizzas;

  /** État actuel de la commande. */
  private EtatCommande etat;

  /**
   * Construit une nouvelle commande pour un client donné.
   
   * La commande est initialement dans l'état CREEE et contient une liste de
   * pizzas vide.
   *
   * @param client client qui crée la commande (non null)
   *
   * @throws IllegalArgumentException si le client est nul
   */
  public Commande(InformationPersonnelle client) {
    if (client == null) {
      throw new IllegalArgumentException("Le client ne peut pas être null.");
    }

    this.client = client;
    this.dateCreation = LocalDateTime.now();
    this.pizzas = new LinkedHashMap<>();
    this.etat = EtatCommande.CREEE;
  }

  /**
   * Retourne le client ayant passé la commande.
   *
   * @return client de la commande
   */
  public InformationPersonnelle getClient() {
    return client;
  }

  /**
   * Retourne la date de création de la commande.
   *
   * @return date et heure de création
   */
  public LocalDateTime getDateCreation() {
    return dateCreation;
  }

  /**
   * Retourne l'état actuel de la commande.
   *
   * @return état de la commande
   */
  public EtatCommande getEtat() {
    return etat;
  }

  /**
   * Retourne la liste des pizzas commandées.
   
   * La map retournée est non modifiable.
   *
   * @return map pizza vers quantité
   */
  public Map<Pizza, Integer> getPizzas() {
    return Collections.unmodifiableMap(pizzas);
  }

  /**
   * Ajoute une pizza à la commande.
   
   * Cette opération est autorisée uniquement lorsque la commande est à l'état
   * CREEE.
   *
   * @param pizza    pizza à ajouter
   * @param quantite quantité strictement positive
   *
   * @throws CommandeException si la commande n'est pas modifiable
   */
  public void ajouterPizza(Pizza pizza, int quantite) throws CommandeException {
    if (etat != EtatCommande.CREEE) {
      throw new CommandeException();
    }
    if (pizza == null) {
      throw new CommandeException();
    }
    if (quantite <= 0) {
      throw new CommandeException();
    }

    pizzas.merge(pizza, quantite, Integer::sum);
  }

  /**
   * Calcule le prix total de la commande.
   
   * Le prix est calculé à partir du prix de vente de chaque pizza multiplié par
   * sa quantité.
   *
   * @return prix total de la commande
   */
  public double getPrixTotal() {
    double total = 0.0;
    for (Map.Entry<Pizza, Integer> entry : pizzas.entrySet()) {
      total += entry.getKey().getPrixVente() * entry.getValue();
    }
    return total;
  }

  /**
   * Valide la commande.
   
   * Une commande validée n'est plus modifiable par le client.
   *
   * @throws CommandeException si la commande n'est pas en cours de création
   */
  public void valider() throws CommandeException {
    if (etat != EtatCommande.CREEE) {
      throw new CommandeException();
    }
    etat = EtatCommande.VALIDEE;
  }

  /**
   * Marque la commande comme traitée.
   
   * Cette opération est réalisée par le pizzaïolo.
   */
  public void traiter() {
    etat = EtatCommande.TRAITEE;
  }

  /**
   * Annule la commande tant qu'elle n'est pas validée.
   
   * La commande est alors considérée comme traitée et son contenu est vidé.
   *
   * @throws CommandeException si la commande est déjà validée
   */
  public void annuler() throws CommandeException {
    if (etat != EtatCommande.CREEE) {
      throw new CommandeException();
    }
    pizzas.clear();
    etat = EtatCommande.TRAITEE;
  }

  /**
   * Calcule le code de hachage de la commande.
   *
   * @return code de hachage
   */
  @Override
  public int hashCode() {
    return Objects.hash(client, dateCreation);
  }

  /**
   * Compare cette commande à un autre objet.
   *
   * @param obj objet à comparer
   * @return true si les commandes sont égales, false sinon
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Commande)) {
      return false;
    }
    Commande other = (Commande) obj;
    return client.equals(other.client) && dateCreation.equals(other.dateCreation);
  }

  /**
   * Retourne une représentation textuelle de la commande.
   *
   * @return description de la commande
   */
  @Override
  public String toString() {
    return "Commande{" + "client=" + client.getNom() 
      + ", etat=" + etat + ", total=" + getPrixTotal() + '}';
  }
}
