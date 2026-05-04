package pizzas;

/**
 * Représente un ingrédient utilisable dans la composition d'une pizza.
 
 * Un ingrédient est caractérisé par : - un nom, - un prix exprimé en euros.
 
 * Le prix d’un ingrédient peut être modifié après sa création, mais il ne peut
 * jamais être négatif.
 
 * Cette classe est utilisée lors de la composition des pizzas afin de calculer
 * le prix minimal et le bénéfice.
 *
 * @author Inas Hallal
 */
public class Ingredient {

  /** Nom de l'ingrédient. */
  private final String nom;

  /** Prix de l'ingrédient en euros. */
  private double prix;

  /**
   * Construit un ingrédient avec un nom et un prix.
   *
   * @param nom  nom de l'ingrédient, non nul et non vide
   * @param prix prix initial de l'ingrédient, supérieur ou égal à zéro
   * @throws IllegalArgumentException si le nom est nul ou vide, ou si le prix est
   *                                  négatif
   */
  public Ingredient(String nom, double prix) {
    if (nom == null || nom.isBlank()) {
      throw new IllegalArgumentException("Le nom de l'ingrédient ne peut pas être vide.");
    }
    if (prix < 0) {
      throw new IllegalArgumentException("Le prix ne peut pas être négatif.");
    }
    this.nom = nom;
    this.prix = prix;
  }

  /**
   * Retourne le nom de l'ingrédient.
   *
   * @return nom de l'ingrédient
   */
  public String getNom() {
    return this.nom;
  }

  /**
   * Retourne le prix actuel de l'ingrédient.
   *
   * @return prix de l'ingrédient
   */
  public double getPrix() {
    return this.prix;
  }

  /**
   * Modifie le prix de l'ingrédient.
   *
   * @param prix nouveau prix de l'ingrédient, supérieur ou égal à zéro
   * @throws IllegalArgumentException si le prix est négatif
   */
  public void setPrix(double prix) {
    if (prix < 0) {
      throw new IllegalArgumentException("Le prix ne peut pas être négatif.");
    }
    this.prix = prix;
  }

  @Override
  public String toString() {
    return "Ingredient{" + "nom='" + nom + '\'' + ", prix=" + prix + '}';
  }
}
