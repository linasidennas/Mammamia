package pizzas;

/**
 * Exception levée en cas de problème lié à une commande.
 * Cette exception est utilisée pour signaler une erreur
 * lors des différentes opérations possibles sur une commande
 * (ajout de pizza, validation, annulation, etc.).
 *
 * @author inas hallal
 */
public class CommandeException extends Exception {

  /** Identifiant de version pour la sérialisation. */
  private static final long serialVersionUID = -2876441299971092712L;

  /**
   * Construit une nouvelle exception de commande
   * avec un message d'erreur par défaut.
   */
  public CommandeException() {
    super("Erreur lors de la gestion de la commande.");
  }

  /**
   * Construit une nouvelle exception de commande
   * avec un message personnalisé.
   *
   * @param message message décrivant l'erreur
   */
  public CommandeException(String message) {
    super(message);
  }
}
