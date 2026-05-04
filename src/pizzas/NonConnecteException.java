package pizzas;

/**
 * Exception levée lorsqu'aucun utilisateur n'est connecté à l'application.
 
 * Cette exception est utilisée pour signaler qu'une action
 * nécessitant un utilisateur connecté a été demandée alors
 * qu'aucun utilisateur n'est actuellement authentifié.
 *
 * @author inas hallal
 */
public class NonConnecteException extends Exception {

  /** Identifiant de version pour la sérialisation. */
  private static final long serialVersionUID = -2876441299971092712L;

  /**
   * Construit une nouvelle exception indiquant
   * qu'aucun utilisateur n'est connecté.
   */
  public NonConnecteException() {
    super("Aucun utilisateur n'est connecté.");
  }

  /**
   * Construit une nouvelle exception indiquant
   * qu'aucun utilisateur n'est connecté,
   * avec un message personnalisé.
   *
   * @param message message décrivant l'erreur
   */
  public NonConnecteException(String message) {
    super(message);
  }
}
