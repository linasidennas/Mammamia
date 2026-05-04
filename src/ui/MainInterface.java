package ui;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pizzas.Ingredient;
import pizzas.Pizza;
import pizzas.TypePizza;
import pizzas.CataloguePizzas;
import pizzas.GestionCommande;
import pizzas.Client;
import pizzas.Commande;
import pizzas.Evaluation;
import pizzas.InformationPersonnelle;

/**
 * Classe principale exécutable de l'application de pizzeria.
 *
 * Cette classe initialise les données de démonstration de l'application
 * (ingrédients, pizzas, clients, commandes, évaluations) puis lance
 * les interfaces graphiques JavaFX :
 * - la fenêtre du pizzaïolo,
 * - la fenêtre du client.
 *
 * Elle hérite de la classe Application de JavaFX et constitue
 * le point d’entrée graphique du programme.
 *
 * @author Kamel BELKHIR
 * @author Lina Sidennas
 * @author Inas Hallal
 */
public final class MainInterface extends Application {

  /**
   * Lance la fenêtre graphique dédiée au client.
   *
   * Cette fenêtre permet au client de :
   * - consulter les pizzas,
   * - créer et gérer des commandes,
   * - appliquer des filtres,
   * - ajouter des évaluations.
   */
  public void startFenetreClient() {
    try {
      URL url = getClass().getResource("client.fxml");
      FXMLLoader fxmlLoader = new FXMLLoader(url);
      VBox root = (VBox) fxmlLoader.load();

      Scene scene = new Scene(root, 1210, 620);

      Stage stage = new Stage();
      stage.setResizable(true);
      stage.setTitle("Commandes de pizzas");
      stage.setScene(scene);
      stage.show();

    } catch (IOException e) {
      System.err.println("Erreur au chargement de la fenêtre du client : " + e);
    }
  }

  /**
   * Lance la fenêtre graphique dédiée au pizzaïolo.
   *
   * Cette fenêtre permet au pizzaïolo de :
   * - gérer les pizzas et ingrédients,
   * - consulter et traiter les commandes,
   * - afficher les statistiques de vente.
   *
   * @param primaryStage fenêtre principale fournie par JavaFX
   */
  public void startFenetrePizzaiolo(Stage primaryStage) {
    try {
      URL url = getClass().getResource("pizzaiolo.fxml");
      FXMLLoader fxmlLoader = new FXMLLoader(url);
      VBox root = (VBox) fxmlLoader.load();

      Scene scene = new Scene(root, 985, 630);

      primaryStage.setScene(scene);
      primaryStage.setResizable(true);
      primaryStage.setTitle("Gestion des pizzas");
      primaryStage.show();

    } catch (IOException e) {
      System.err.println("Erreur au chargement de la fenêtre du pizzaïolo : " + e);
    }
  }

  /**
   * Méthode appelée automatiquement par JavaFX au démarrage.
   *
   * Elle initialise :
   * - le catalogue de pizzas,
   * - les ingrédients,
   * - un client de démonstration,
   * - des commandes traitées et non traitées,
   * - des évaluations,
   * puis lance les deux fenêtres graphiques de l'application.
   *
   * @param primaryStage fenêtre principale JavaFX
   */
  @Override
  public void start(Stage primaryStage) {

    // Nettoyage des données existantes
    CataloguePizzas.getPizzas().clear();
    GestionCommande.getInstance().getCommandesTraitees().clear();

    // Création des ingrédients
    Ingredient tomate = new Ingredient("Tomate", 1.0);
    Ingredient mozzarella = new Ingredient("Mozzarella", 1.5);
    Ingredient jambon = new Ingredient("Jambon", 2.0);
    Ingredient champignon = new Ingredient("Champignon", 1.2);
    Ingredient creme = new Ingredient("Crème", 1.0);
    Ingredient lardon = new Ingredient("Lardon", 2.0);

    // Création des pizzas
    Pizza margherita = new Pizza("Margherita", TypePizza.Vegetarienne);
    margherita.ajouterIngredient(tomate);
    margherita.ajouterIngredient(mozzarella);
    margherita.setPrixVente(8.5);

    Pizza regina = new Pizza("Regina", TypePizza.Viande);
    regina.ajouterIngredient(tomate);
    regina.ajouterIngredient(mozzarella);
    regina.ajouterIngredient(jambon);
    regina.ajouterIngredient(champignon);
    regina.setPrixVente(10.0);

    Pizza campagnarde = new Pizza("Campagnarde", TypePizza.Regionale);
    campagnarde.ajouterIngredient(creme);
    campagnarde.ajouterIngredient(mozzarella);
    campagnarde.ajouterIngredient(lardon);
    campagnarde.setPrixVente(11.5);

    CataloguePizzas.getPizzas().add(margherita);
    CataloguePizzas.getPizzas().add(regina);
    CataloguePizzas.getPizzas().add(campagnarde);

    // Création d’un client de démonstration
    InformationPersonnelle infos =
        new InformationPersonnelle("Dupont", "Jean", "1 rue Java", 25);

    Client client = new Client("jean@mail.com", "1234", infos);
    ClientControleur.getClients().add(client);

    try {
      // Commande traitée
      Commande cmdTraitee = new Commande(infos);
      cmdTraitee.ajouterPizza(margherita, 1);
      cmdTraitee.ajouterPizza(regina, 2);
      cmdTraitee.valider();
      cmdTraitee.traiter();

      client.ajouterCommande(cmdTraitee);
      GestionCommande.getInstance().ajouterCommandeValidee(cmdTraitee);

      // Commande non traitée
      Commande cmdNonTraitee = new Commande(infos);
      cmdNonTraitee.ajouterPizza(campagnarde, 1);
      cmdNonTraitee.valider();

      client.ajouterCommande(cmdNonTraitee);
      GestionCommande.getInstance().ajouterCommandeValidee(cmdNonTraitee);

    } catch (Exception e) {
      e.printStackTrace();
    }

    // Ajout d’évaluations
    Evaluation e1 = new Evaluation(margherita, "Jean", 4, "Très bonne pizza");
    Evaluation e2 = new Evaluation(regina, "Jean", 5, "Ma préférée");

    margherita.ajouterEvaluation(e1);
    regina.ajouterEvaluation(e2);

    // Lancement des fenêtres
    this.startFenetrePizzaiolo(primaryStage);
    this.startFenetreClient();
  }

  /**
   * Méthode principale de lancement de l'application JavaFX.
   *
   * @param args arguments de la ligne de commande (non utilisés)
   */
  public static void main(String[] args) {
    launch(args);
  }
}
