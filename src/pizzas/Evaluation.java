package pizzas;

import java.io.Serializable;

/**
 * Représente une évaluation laissée par un client pour une pizza.
 
 * Une évaluation est composée : - d’une pizza évaluée, - d’une note entière
 * comprise entre 0 et 5, - d’un commentaire optionnel, - d’un auteur optionnel.
 
 * Une évaluation ne peut être créée que pour une pizza valide. La vérification
 * du droit d’évaluer (commande déjà traitée) n’est pas effectuée dans cette
 * classe mais dans les services de gestion des clients.
 
 * Cette classe est sérialisable afin de permettre la sauvegarde et le
 * chargement des données de l’application.
 *
 * @author Inas Hallal
 */
public class Evaluation implements Serializable {

  /** Identifiant de version pour la sérialisation. */
  private static final long serialVersionUID = 1L;

  /** Note attribuée à la pizza, comprise entre 0 et 5. */
  private final int note;

  /** Commentaire associé à l’évaluation (optionnel). */
  private final String commentaire;

  /** Pizza évaluée. */
  private final Pizza pizza;

  /** Auteur de l’évaluation (optionnel selon le contexte). */
  private final String auteur;

  /**
   * Construit une évaluation sans auteur explicite.
   * Ce constructeur est utilisé par les services métier lorsque l’auteur n’est
   * pas nécessairement affiché.
   *
   * @param pizza       pizza évaluée (non null)
   * @param note        note attribuée, comprise entre 0 et 5
   * @param commentaire commentaire optionnel
   *
   * @throws IllegalArgumentException si la pizza est nulle ou si la note est hors
   *                                  intervalle
   */
  public Evaluation(Pizza pizza, int note, String commentaire) {
    if (pizza == null) {
      throw new IllegalArgumentException("Pizza nulle");
    }
    if (note < 0 || note > 5) {
      throw new IllegalArgumentException("Note invalide");
    }
    this.pizza = pizza;
    this.note = note;
    this.commentaire = commentaire;
    this.auteur = null;
  }

  /**
   * Construit une évaluation avec un auteur explicite.
   
   * Ce constructeur est utilisé par l’interface graphique lorsque le client
   * souhaite indiquer un nom d’auteur.
   *
   * @param pizza       pizza évaluée (non null)
   * @param auteur      auteur de l’évaluation (non null, non vide)
   * @param note        note attribuée, comprise entre 0 et 5
   * @param commentaire commentaire optionnel
   *
   * @throws IllegalArgumentException si la pizza est nulle, si l’auteur est
   *                                  invalide ou si la note est hors intervalle
   */
  public Evaluation(Pizza pizza, String auteur, int note, String commentaire) {
    if (pizza == null) {
      throw new IllegalArgumentException("Pizza nulle");
    }
    if (auteur == null || auteur.isBlank()) {
      throw new IllegalArgumentException("Auteur invalide");
    }
    if (note < 0 || note > 5) {
      throw new IllegalArgumentException("Note invalide");
    }
    this.pizza = pizza;
    this.auteur = auteur;
    this.note = note;
    this.commentaire = commentaire;
  }

  /**
   * Retourne la note attribuée à la pizza.
   *
   * @return note comprise entre 0 et 5
   */
  public int getNote() {
    return note;
  }

  /**
   * Retourne le commentaire de l’évaluation.
   *
   * @return commentaire ou null
   */
  public String getCommentaire() {
    return commentaire;
  }

  /**
   * Retourne la pizza évaluée.
   *
   * @return pizza concernée par l’évaluation
   */
  public Pizza getPizza() {
    return pizza;
  }

  /**
   * Retourne l’auteur de l’évaluation.
   *
   * @return auteur ou null si non défini
   */
  public String getAuteur() {
    return auteur;
  }

  /**
   * Retourne une représentation textuelle de l’évaluation.
   *
   * @return description de l’évaluation
   */
  @Override
  public String toString() {
    return "Evaluation{" + "pizza=" + pizza.getNom() + ", auteur='" 
        + auteur + '\'' + ", note=" + note
        + ", commentaire='" + commentaire + '\'' + '}';
  }
}
