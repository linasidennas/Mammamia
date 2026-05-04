package pizzas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Représente une pizza proposée à la vente dans l’application.
 
 * Une pizza est définie par : - un nom, - un type (viande, végétarienne ou
 * régionale), - une liste d’ingrédients, - un prix de vente, - une liste
 * d’évaluations laissées par les clients.
 
 * Le prix minimal d’une pizza est calculé comme suit : somme des prix des
 * ingrédients augmentée de 40 %, puis arrondie à la dizaine de centimes
 * supérieure.
 
 * Une pizza peut recevoir plusieurs évaluations et fournit des méthodes
 * permettant de calculer la note moyenne et le bénéfice unitaire.
 *
 * @author Lina Sidennas
 */
public class Pizza implements Serializable {

  /** Identifiant de sérialisation. */
  private static final long serialVersionUID = 1L;

  /** Nom de la pizza. */
  private final String nom;

  /** Type de la pizza. */
  private final TypePizza type;

  /** Liste des ingrédients composant la pizza. */
  private final List<Ingredient> ingredients;

  /** Liste des évaluations associées à la pizza. */
  private final List<Evaluation> evaluations;

  /** Prix de vente actuel de la pizza. */
  private double prixVente;

  /** Chemin ou URL de la photo associée à la pizza. */
  private String cheminPhoto;

  /**
   * Construit une pizza à partir de son nom et de son type.
   *
   * @param nom  nom de la pizza, non nul et non vide
   * @param type type de la pizza, non nul
   * @throws IllegalArgumentException si le nom est vide ou si le type est nul
   */
  public Pizza(String nom, TypePizza type) {
    if (nom == null || nom.isBlank()) {
      throw new IllegalArgumentException("Le nom de la pizza ne peut pas être vide.");
    }
    if (type == null) {
      throw new IllegalArgumentException("Le type ne peut pas être null.");
    }

    this.nom = nom;
    this.type = type;
    this.ingredients = new ArrayList<>();
    this.evaluations = new ArrayList<>();
    this.prixVente = 0.0;
    this.cheminPhoto = null;
  }

  /**
   * Retourne le nom de la pizza.
   *
   * @return nom de la pizza
   */
  public String getNom() {
    return nom;
  }

  /**
   * Retourne le type de la pizza.
   *
   * @return type de la pizza
   */
  public TypePizza getType() {
    return type;
  }

  /**
   * Retourne la liste des ingrédients de la pizza.
   
   * La liste retournée n’est pas modifiable.
   *
   * @return liste des ingrédients
   */
  public List<Ingredient> getIngredients() {
    return Collections.unmodifiableList(ingredients);
  }

  /**
   * Retourne la liste des évaluations associées à la pizza.
   
   * La liste retournée n’est pas modifiable.
   *
   * @return liste des évaluations
   */
  public List<Evaluation> getEvaluations() {
    return Collections.unmodifiableList(evaluations);
  }

  /**
   * Ajoute un ingrédient à la pizza.
   *
   * @param ingredient ingrédient à ajouter
   * @throws IllegalArgumentException si l’ingrédient est nul
   */
  public void ajouterIngredient(Ingredient ingredient) {
    if (ingredient == null) {
      throw new IllegalArgumentException("L'ingrédient ne peut pas être null.");
    }
    ingredients.add(ingredient);
  }

  /**
   * Retire un ingrédient de la pizza.
   *
   * @param ingredient ingrédient à retirer
   * @return true si l’ingrédient a été retiré, false sinon
   */
  public boolean retirerIngredient(Ingredient ingredient) {
    if (ingredient == null) {
      return false;
    }
    return ingredients.remove(ingredient);
  }

  /**
   * Calcule le prix minimal de la pizza.
   *
   * @return prix minimal arrondi à la dizaine de centimes supérieure
   */
  public double getPrixMinimal() {
    double somme = 0.0;
    for (Ingredient ing : ingredients) {
      somme += ing.getPrix();
    }
    double brut = somme * 1.40;
    return Math.ceil(brut * 10.0) / 10.0;
  }

  /**
   * Retourne le prix de vente de la pizza.
   *
   * @return prix de vente
   */
  public double getPrixVente() {
    return prixVente;
  }

  /**
   * Définit le prix de vente de la pizza.
   
   * Le prix doit être supérieur ou égal au prix minimal.
   *
   * @param prix nouveau prix de vente
   * @throws IllegalArgumentException si le prix est inférieur au prix minimal
   */
  public void setPrixVente(double prix) {
    double min = getPrixMinimal();
    if (prix < min) {
      throw new IllegalArgumentException("Prix trop bas : minimum " + min + "€.");
    }
    this.prixVente = prix;
  }

  /**
   * Retourne le chemin ou l’URL de la photo associée à la pizza.
   *
   * @return chemin de la photo
   */
  public String getCheminPhoto() {
    return cheminPhoto;
  }

  /**
   * Définit le chemin ou l’URL de la photo associée à la pizza.
   *
   * @param cheminPhoto chemin ou URL de la photo
   */
  public void setCheminPhoto(String cheminPhoto) {
    this.cheminPhoto = cheminPhoto;
  }

  /**
   * Calcule le bénéfice unitaire de la pizza.
   *
   * @return bénéfice unitaire
   */
  public double getBeneficeUnitaire() {
    return prixVente - getPrixMinimal();
  }

  /**
   * Ajoute une évaluation à la pizza.
   *
   * @param eval évaluation à ajouter
   * @throws IllegalArgumentException si l’évaluation est nulle ou ne concerne pas
   *                                  cette pizza
   */
  public void ajouterEvaluation(Evaluation eval) {
    if (eval == null) {
      throw new IllegalArgumentException("L'évaluation ne peut pas être null.");
    }
    if (eval.getPizza() != this) {
      throw new IllegalArgumentException("L'évaluation n'appartient pas à cette pizza.");
    }
    evaluations.add(eval);
  }

  /**
   * Calcule la note moyenne des évaluations de la pizza.
   *
   * @return note moyenne ou 0 s’il n’y a aucune évaluation
   */
  public double getNoteMoyenne() {
    if (evaluations.isEmpty()) {
      return 0.0;
    }
    double total = 0.0;
    for (Evaluation e : evaluations) {
      total += e.getNote();
    }
    return total / evaluations.size();
  }

  @Override
  public int hashCode() {
    return Objects.hash(nom, type);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Pizza)) {
      return false;
    }
    Pizza other = (Pizza) obj;
    return nom.equals(other.nom) && type == other.type;
  }

  @Override
  public String toString() {
    return "Pizza{" + "nom='" + nom + '\'' + ", type=" + type + ", prixVente=" + prixVente + '}';
  }
}
