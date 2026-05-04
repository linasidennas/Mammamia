package pizzas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Représente un client de l'application de commande de pizzas.
 
 * Un client est identifié par une adresse email unique. Il possède un mot de
 * passe, des informations personnelles et une liste de commandes associées.
 
 * Les commandes d’un client peuvent être : - en cours (état CREEE ou VALIDEE) -
 * traitées (état TRAITEE)
 
 * Cette classe est sérialisable afin de permettre la sauvegarde et le
 * chargement des données clients.
 *
 * @author Inas Hallal
 */
public class Client implements Serializable {

  /** Identifiant de version pour la sérialisation. */
  private static final long serialVersionUID = 1L;

  /** Adresse email du client, utilisée comme identifiant unique. */
  private final String email;

  /** Mot de passe du client. */
  private String motDePasse;

  /** Informations personnelles du client. */
  private final InformationPersonnelle infos;

  /** Liste de toutes les commandes du client. */
  private final List<Commande> commandes;

  /**
   * Construit un nouveau client.
   *
   * @param email      adresse email du client (non null, non vide)
   * @param motDePasse mot de passe du client (non null, non vide)
   * @param infos      informations personnelles du client (non null)
   *
   * @throws IllegalArgumentException si un paramètre est invalide
   */
  public Client(String email, String motDePasse, InformationPersonnelle infos) {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("L'email ne peut pas être vide.");
    }
    if (motDePasse == null || motDePasse.isBlank()) {
      throw new IllegalArgumentException("Le mot de passe ne peut pas être vide.");
    }
    if (infos == null) {
      throw new IllegalArgumentException("Les informations personnelles ne peuvent pas être null.");
    }

    this.email = email;
    this.motDePasse = motDePasse;
    this.infos = infos;
    this.commandes = new ArrayList<>();
  }

  /**
   * Retourne l'adresse email du client.
   *
   * @return email du client
   */
  public String getEmail() {
    return email;
  }

  /**
   * Retourne le mot de passe du client.
   *
   * @return mot de passe
   */
  public String getMotDePasse() {
    return motDePasse;
  }

  /**
   * Modifie le mot de passe du client.
   *
   * @param nouveau nouveau mot de passe (non null, non vide)
   *
   * @throws IllegalArgumentException si le mot de passe est invalide
   */
  public void setMotDePasse(String nouveau) {
    if (nouveau == null || nouveau.isBlank()) {
      throw new IllegalArgumentException("Le mot de passe ne peut pas être vide.");
    }
    this.motDePasse = nouveau;
  }

  /**
   * Retourne les informations personnelles du client.
   *
   * @return informations personnelles
   */
  public InformationPersonnelle getInfos() {
    return infos;
  }

  /**
   * Retourne la liste des commandes du client.
   
   * La liste retournée est non modifiable.
   * 
   * @return liste non modifiable des commandes
   */
  public List<Commande> getCommandes() {
    return Collections.unmodifiableList(commandes);
  }

  /**
   * Ajoute une commande à la liste des commandes du client.
   *
   * @param commande commande à ajouter
   *
   * @throws IllegalArgumentException si la commande est nulle
   */
  public void ajouterCommande(Commande commande) {
    if (commande == null) {
      throw new IllegalArgumentException("Commande invalide.");
    }
    commandes.add(commande);
  }

  /**
   * Retourne les commandes déjà traitées du client.
   *
   * @return liste des commandes traitées
   */
  public List<Commande> getCommandesPassees() {
    List<Commande> res = new ArrayList<>();
    for (Commande c : commandes) {
      if (c.getEtat() == Commande.EtatCommande.TRAITEE) {
        res.add(c);
      }
    }
    return res;
  }

  /**
   * Retourne les commandes en cours du client.
   *
   * @return liste des commandes en cours
   */
  public List<Commande> getCommandesEnCours() {
    List<Commande> res = new ArrayList<>();
    for (Commande c : commandes) {
      if (c.getEtat() != Commande.EtatCommande.TRAITEE) {
        res.add(c);
      }
    }
    return res;
  }

  /**
   * Calcule le code de hachage du client à partir de son email.
   *
   * @return code de hachage
   */
  @Override
  public int hashCode() {
    return Objects.hash(email);
  }

  /**
   * Compare ce client à un autre objet.
   *
   * @param obj objet à comparer
   * @return true si les clients sont égaux, false sinon
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Client)) {
      return false;
    }
    Client other = (Client) obj;
    return email.equals(other.email);
  }

  /**
   * Retourne une représentation textuelle du client.
   *
   * @return description du client
   */
  @Override
  public String toString() {
    return "Client{" + "email='" + email + '\'' + ", nom='" + infos.getNom() + '\'' + '}';
  }
}
