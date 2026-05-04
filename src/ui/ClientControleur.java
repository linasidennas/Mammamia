
package ui;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import pizzas.Client;
import pizzas.Commande;
import pizzas.Evaluation;
import pizzas.InformationPersonnelle;
import pizzas.Ingredient;
import pizzas.GestionCommande;
import pizzas.CataloguePizzas;
import pizzas.Pizza;
import pizzas.TypePizza;


/**
 * Contrôleur JavaFX de la fenêtre client.
 *
 * Cette classe permet au client de :
 * - s’inscrire et se connecter,
 * - consulter les pizzas disponibles,
 * - filtrer les pizzas,
 * - créer, modifier et valider des commandes,
 * - consulter ses commandes passées,
 * - évaluer des pizzas déjà commandées.
 *
 * @author Kamel BELKHIR
 */
public class ClientControleur {

	/**
	 * Liste des commandes actuellement affichées à l'écran.
	 * Elle permet de stocker temporairement les commandes visibles
	 * pour l'utilisateur (par exemple dans une interface graphique).
	 */
	private List<Commande> commandesAffichees = new ArrayList<>();

	/**
	 * Liste des clients inscrits dans l'application.
	 * Cette liste est partagée par toutes les instances de la classe
	 * et représente une mémoire simple des clients existants.
	 */
	private static final List<Client> clients = new ArrayList<>();

	/**
	 * Client actuellement connecté à l'application.
	 * Il représente l'utilisateur actif pendant la session en cours.
	 */
	private Client clientConnecte;

	/**
	 * Commande en cours de création ou de traitement.
	 * Elle correspond à la commande que le client connecté est
	 * en train de préparer ou de modifier.
	 */
	private Commande commandeEnCours;


	/**
	 * Affiche une fenêtre popup d'information, d'erreur ou de confirmation.
	 *
	 * @param titre   titre de la fenêtre
	 * @param message message affiché dans la popup
	 * @param type    type de popup (INFORMATION, ERROR, etc.)
	 */
  private void afficherPopup(String titre, String message, Alert.AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(titre);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private String filtreType = null;
  private String filtreIngredient = null;
  private Double filtrePrixMax = null;

  /**
   * Applique les filtres sélectionnés (type, ingrédient, prix max)
   * sur la liste des pizzas affichées.
   */
  private void appliquerFiltres() {
    listePizzas.getItems().clear();

    for (Pizza p : CataloguePizzas.getPizzas()) {

      // Filtre par type
      if (filtreType != null && !p.getType().toString().equals(filtreType)) {
        continue;
      }

      // Filtre par ingrédient
      if (filtreIngredient != null && !filtreIngredient.isBlank()) {
        boolean trouve = false;
        for (var ing : p.getIngredients()) {
          if (ing.getNom().toLowerCase().contains(filtreIngredient.toLowerCase())) {
            trouve = true;
            break;
          }
        }
        if (!trouve)
          continue;
      }

      // Filtre par prix max
      if (filtrePrixMax != null && p.getPrixVente() > filtrePrixMax) {
        continue;
      }

      // Si la pizza passe tous les filtres → on l'affiche
      listePizzas.getItems().add(p.getNom());
      labelListePizzas.setText("Pizzas filtrées");

    }
  }

  @FXML
  private ChoiceBox<String> choiceBoxFiltreType;

  @FXML
  private TextField entreeAdresseClient;

  @FXML
  private TextField entreeAgeClient;

  @FXML
  private TextField entreeAuteurEvaluation;

  @FXML
  private TextField entreeEmailClient;

  @FXML
  private TextField entreeEvaluationMoyenneEvaluations;

  @FXML
  private TextField entreeFiltreContientIngredient;

  @FXML
  private TextField entreeFiltrePrixMax;

  @FXML
  private TextField entreeMotDePasseClient;

  @FXML
  private TextField entreeNomClient;

  @FXML
  private TextField entreeNomPizza;

  @FXML
  private TextField entreeNomPizzaEvaluee;

  @FXML
  private TextField entreeNoteMoyennePizza;

  @FXML
  private TextField entreePrenomClient;

  @FXML
  private TextField entreePrixPizza;

  @FXML
  private TextField entreeTypePizza;

  @FXML
  private Label labelListeCommandes;

  @FXML
  private Label labelListePizzas;

  @FXML
  private ListView<String> listeCommandes;

  @FXML
  private ListView<String> listeEvaluations;

  @FXML
  private ListView<String> listeIngredients;

  @FXML
  private ChoiceBox<Integer> choiceBoxNoteEvaluation;

  @FXML
  private ListView<String> listePizzas;

  @FXML
  private StackPane panePhotoPizza;

  @FXML
  private TextArea texteCommentaireEvaluation;

  /**
   * Affiche les commandes en cours du client connecté
   * (commandes créées ou validées mais non traitées).
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonAfficherCommandesEnCours(ActionEvent event) {

    if (clientConnecte == null) {
      afficherPopup("Erreur", "Vous devez être connecté", AlertType.ERROR);
      return;
    }

    listeCommandes.getItems().clear();
    commandesAffichees.clear();

    labelListeCommandes.setText("Commandes en cours");

    for (Commande c : clientConnecte.getCommandes()) {
      if (c.getEtat() == Commande.EtatCommande.CREEE || c.getEtat() == Commande.EtatCommande.VALIDEE) {

        commandesAffichees.add(c);
        listeCommandes.getItems().add(c.toString());
      }
    }
  }

  /**
   * Affiche les commandes déjà traitées du client connecté.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonAfficherCommandesTtraitees(ActionEvent event) {

    if (clientConnecte == null) {
      afficherPopup("Erreur", "Vous devez être connecté", AlertType.ERROR);
      return;
    }

    listeCommandes.getItems().clear();
    commandesAffichees.clear();

    labelListeCommandes.setText("Commandes traitées");

    for (Commande c : clientConnecte.getCommandes()) {
      if (c.getEtat() == Commande.EtatCommande.TRAITEE) {

        commandesAffichees.add(c);
        listeCommandes.getItems().add(c.toString());
      }
    }
  }

  /**
   * Affiche les évaluations associées à la pizza sélectionnée.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonAfficherEvaluationPizzas(ActionEvent event) {

    String nomPizza = listePizzas.getSelectionModel().getSelectedItem();
    if (nomPizza == null) {
      afficherPopup("Erreur", "Aucune pizza sélectionnée", Alert.AlertType.ERROR);
      return;
    }

    for (Pizza p : CataloguePizzas.getPizzas()) {
      if (p.getNom().equals(nomPizza)) {

        entreeNomPizzaEvaluee.setText(p.getNom());
        entreeEvaluationMoyenneEvaluations.setText(String.valueOf(p.getNoteMoyenne()));

        listeEvaluations.getItems().clear();
        for (Evaluation e : p.getEvaluations()) {
          listeEvaluations.getItems().add("Note : " + e.getNote());
        }

        return;
      }
    }
  }

  /**
   * Affiche toutes les pizzas disponibles à la vente.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonAfficherToutesPizzas(ActionEvent event) {

    listePizzas.getItems().clear();

    for (Pizza p : CataloguePizzas.getPizzas()) {
      listePizzas.getItems().add(p.getNom());
    }

    labelListePizzas.setText("Toutes les pizzas en vente");
  }

  /**
   * Ajoute une évaluation sur une pizza déjà commandée
   * et traitée par le client connecté.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonAjouterMonEvaluation(ActionEvent event) {

    if (clientConnecte == null) {
      afficherPopup("Erreur", "Vous devez être connecté", Alert.AlertType.ERROR);
      return;
    }

    String nomPizza = entreeNomPizza.getText();
    if (nomPizza == null || nomPizza.isBlank()) {
      afficherPopup("Erreur", "Aucune pizza sélectionnée", Alert.AlertType.ERROR);
      return;
    }

    Integer note = choiceBoxNoteEvaluation.getValue();
    if (note == null) {
      afficherPopup("Erreur", "Veuillez choisir une note", Alert.AlertType.ERROR);
      return;
    }

    String commentaire = texteCommentaireEvaluation.getText();

    // 1️⃣ Retrouver la pizza
    Pizza pizza = null;
    for (Pizza p : CataloguePizzas.getPizzas()) {
      if (p.getNom().equals(nomPizza)) {
        pizza = p;
        break;
      }
    }

    if (pizza == null) {
      afficherPopup("Erreur", "Pizza introuvable", Alert.AlertType.ERROR);
      return;
    }

    // 2️⃣ Vérifier que le client a déjà commandé cette pizza (commande TRAITÉE)
    boolean aDejaCommande = false;

    for (Commande c : clientConnecte.getCommandes()) {
      if (c.getEtat() == Commande.EtatCommande.TRAITEE && c.getPizzas().containsKey(pizza)) {
        aDejaCommande = true;
        break;
      }
    }

    if (!aDejaCommande) {
      afficherPopup("Erreur", "Vous ne pouvez évaluer une pizza que si vous l'avez commandée", Alert.AlertType.ERROR);
      return;
    }

    String auteur = clientConnecte.getEmail(); // ou getInfos().getNom()

    Evaluation evaluation = new Evaluation(pizza, auteur, note, commentaire);

    pizza.ajouterEvaluation(evaluation);
 // 🔄 Rafraîchissement de la liste des évaluations
    listeEvaluations.getItems().clear();
    for (Evaluation e : pizza.getEvaluations()) {
        listeEvaluations.getItems().add("Note : " + e.getNote());
    }

    afficherPopup("Succès", "Évaluation ajoutée avec succès", Alert.AlertType.INFORMATION);
  }

  /**
   * Ajoute la pizza sélectionnée à la commande en cours du client.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonAjouterPizzaSelectionneeCommande(ActionEvent event) {

    if (clientConnecte == null) {
      afficherPopup("Erreur", "Vous devez être connecté", AlertType.ERROR);
      return;
    }

    if (commandeEnCours == null || commandeEnCours.getEtat() != Commande.EtatCommande.CREEE) {
      afficherPopup("Erreur", "Aucune commande en cours", AlertType.ERROR);
      return;
    }

    String nomPizza = listePizzas.getSelectionModel().getSelectedItem();
    if (nomPizza == null) {
      afficherPopup("Erreur", "Aucune pizza sélectionnée", AlertType.ERROR);
      return;
    }

    for (Pizza p : CataloguePizzas.getPizzas()) {
      if (p.getNom().equals(nomPizza)) {
        try {
          commandeEnCours.ajouterPizza(p, 1);
          afficherPopup("Commande", "Pizza ajoutée", AlertType.INFORMATION);
        } catch (Exception e) {
          afficherPopup("Erreur", "Impossible d’ajouter la pizza", AlertType.ERROR);
        }
        return;
      }
    }
  }

  /**
   * Applique le filtre de recherche par ingrédient.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonAppliquerFiltreContientngredient(ActionEvent event) {
    filtreIngredient = entreeFiltreContientIngredient.getText();
    appliquerFiltres();
    labelListePizzas.setText("Pizzas filtrées (ingrédient)");
  }

  /**
   * Applique le filtre de prix maximum sur les pizzas.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonAppliquerFiltrePrixMax(ActionEvent event) {
    try {
      filtrePrixMax = Double.parseDouble(entreeFiltrePrixMax.getText());
      appliquerFiltres();
      labelListePizzas.setText("Pizzas filtrées (prix max)");
    } catch (NumberFormatException e) {
      afficherPopup("Erreur", "Prix maximum invalide", Alert.AlertType.ERROR);
    }
  }

  /**
   * Applique le filtre par type de pizza.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonAppliquerFiltreType(ActionEvent event) {
    filtreType = choiceBoxFiltreType.getValue();
    appliquerFiltres();
    labelListePizzas.setText("Pizzas filtrées (type)");
  }

  /**
   * Connecte un client existant à partir de son email et mot de passe.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonConnexion(ActionEvent event) {

    String email = entreeEmailClient.getText();
    String motDePasse = entreeMotDePasseClient.getText();

    for (Client c : clients) {
      if (c.getEmail().equals(email) && c.getMotDePasse().equals(motDePasse)) {

        clientConnecte = c;

        entreeNomClient.setText(c.getInfos().getNom());
        entreePrenomClient.setText(c.getInfos().getPrenom());
        entreeAdresseClient.setText(c.getInfos().getAdresse());
        entreeAgeClient.setText(String.valueOf(c.getInfos().getAge()));

        afficherPopup("Connexion", "Connexion réussie", AlertType.INFORMATION);
        return;
      }
    }

    afficherPopup("Erreur", "Email ou mot de passe incorrect", AlertType.ERROR);
  }

  /**
   * Crée une nouvelle commande pour le client connecté.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonCreerNouvelleCommande(ActionEvent event) {

    if (clientConnecte == null) {
      afficherPopup("Erreur", "Vous devez être connecté", AlertType.ERROR);
      return;
    }

    commandeEnCours = new Commande(clientConnecte.getInfos());
    clientConnecte.ajouterCommande(commandeEnCours);
    PizzaioloControleur.ajouterCommande(commandeEnCours);

    actionBoutonAfficherCommandesEnCours(null);
    listeCommandes.getSelectionModel().selectLast();

    afficherPopup("Commande", "Nouvelle commande créée", AlertType.INFORMATION);
  }

  /**
   * Déconnecte le client actuellement connecté
   * et réinitialise l'interface.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonDeconnexion(ActionEvent event) {

    clientConnecte = null;

    entreeNomClient.clear();
    entreePrenomClient.clear();
    entreeAdresseClient.clear();
    entreeAgeClient.clear();
    entreeEmailClient.clear();
    entreeMotDePasseClient.clear();

    afficherPopup("Déconnexion", "Client déconnecté", AlertType.INFORMATION);
  }

  /**
   * Inscrit un nouveau client dans l'application.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonInscription(ActionEvent event) {

    String nom = entreeNomClient.getText();
    String prenom = entreePrenomClient.getText();
    String adresse = entreeAdresseClient.getText();
    String ageTexte = entreeAgeClient.getText();
    String email = entreeEmailClient.getText();
    String motDePasse = entreeMotDePasseClient.getText();

    if (nom.isBlank() || prenom.isBlank() || email.isBlank() || motDePasse.isBlank()) {
      afficherPopup("Erreur", "Champs obligatoires manquants", AlertType.ERROR);
      return;
    }

    for (Client c : clients) {
      if (c.getEmail().equals(email)) {
        afficherPopup("Erreur", "Email déjà utilisé", AlertType.ERROR);
        return;
      }
    }

    int age;
    try {
        age = Integer.parseInt(ageTexte);
    } catch (NumberFormatException e) {
        afficherPopup("Erreur", "Âge invalide", AlertType.ERROR);
        return;
    }

    // 🔍 Vérification âge réaliste
    if (age < 0 || age > 120) {
        afficherPopup("Erreur", "Âge invalide", AlertType.ERROR);
        return;
    }


    InformationPersonnelle infos = new InformationPersonnelle(nom, prenom, adresse, age);

    Client nouveauClient = new Client(email, motDePasse, infos);
    clients.add(nouveauClient);

    afficherPopup("Succès", "Inscription réussie", AlertType.INFORMATION);
  }

  /**
   * Réinitialise tous les filtres appliqués sur les pizzas.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonReinitialiserFiltre(ActionEvent event) {

    filtreType = null;
    filtreIngredient = null;
    filtrePrixMax = null;

    entreeFiltreContientIngredient.clear();
    entreeFiltrePrixMax.clear();
    choiceBoxFiltreType.setValue(null);

    listePizzas.getItems().clear();
    for (Pizza p : CataloguePizzas.getPizzas()) {
      listePizzas.getItems().add(p.getNom());
    }

    labelListePizzas.setText("Toutes les pizzas en vente");
  }

  /**
   * Valide la commande en cours du client connecté.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonValiderCommandeEnCours(ActionEvent event) {

    if (clientConnecte == null) {
      afficherPopup("Erreur", "Vous devez être connecté", AlertType.ERROR);
      return;
    }

    if (commandeEnCours == null || commandeEnCours.getEtat() != Commande.EtatCommande.CREEE) {
      afficherPopup("Erreur", "Aucune commande valide à valider", AlertType.ERROR);
      return;
    }

    try {
      commandeEnCours.valider();
      GestionCommande.getInstance().ajouterCommandeValidee(commandeEnCours);

      afficherPopup("Commande", "Commande validée", AlertType.INFORMATION);
      actionBoutonAfficherCommandesEnCours(null);
    } catch (Exception e) {
      afficherPopup("Erreur", "Validation impossible", AlertType.ERROR);
    }
  }

  /**
   * Sélectionne une commande et affiche les pizzas associées.
   *
   * @param event événement souris
   */
  @FXML
  void actionSelectionCommnade(MouseEvent event) {

    int index = listeCommandes.getSelectionModel().getSelectedIndex();
    if (index < 0)
      return;

    Commande c = commandesAffichees.get(index);
    commandeEnCours = c;

    listePizzas.getItems().clear();

    for (Pizza p : c.getPizzas().keySet()) {
      listePizzas.getItems().add(p.getNom());
    }
  }

  /**
   * Sélectionne une évaluation existante
   * et affiche son contenu dans les champs.
   *
   * @param event événement souris
   */
  @FXML
  void actionSelectionEvaluation(MouseEvent event) {

    int index = listeEvaluations.getSelectionModel().getSelectedIndex();
    if (index < 0)
      return;

    String nomPizza = entreeNomPizzaEvaluee.getText();

    for (Pizza p : CataloguePizzas.getPizzas()) {
      if (p.getNom().equals(nomPizza)) {

        Evaluation e = p.getEvaluations().get(index);

        choiceBoxNoteEvaluation.setValue(e.getNote());
        texteCommentaireEvaluation.setText(e.getCommentaire() == null ? "" : e.getCommentaire());

        return;
      }
    }
  }

  /**
   * Sélectionne une pizza et affiche ses informations détaillées.
   *
   * @param event événement souris
   */
  @FXML
  void actionSelectionPizza(MouseEvent event) {

    String nomPizza = listePizzas.getSelectionModel().getSelectedItem();
    if (nomPizza == null)
      return;

    for (Pizza p : CataloguePizzas.getPizzas()) {
      if (p.getNom().equals(nomPizza)) {

        entreeNomPizza.setText(p.getNom());
        entreePrixPizza.setText(String.valueOf(p.getPrixVente()));
        entreeTypePizza.setText(p.getType().toString());
        entreeNoteMoyennePizza.setText(String.valueOf(p.getNoteMoyenne()));

        listeIngredients.getItems().clear();
        for (var ing : p.getIngredients()) {
          listeIngredients.getItems().add(ing.getNom());
        }

        return;
      }
    }
  }

  /**
   * Retourne la liste des clients inscrits.
   *
   * @return liste des clients
   */
  public static List<Client> getClients() {
    return clients;
  }

  
  /**
   * Initialise l'interface graphique du client :
   * - chargement des pizzas
   * - initialisation des filtres
   * - réinitialisation de l'état de connexion
   */
  @FXML
  void initialize() {

      // ===============================
      // Notes possibles pour les évaluations
      // ===============================
      choiceBoxNoteEvaluation.getItems().addAll(0, 1, 2, 3, 4, 5);

      // ===============================
      // Types de pizzas pour le filtre
      // ===============================
      choiceBoxFiltreType.getItems().clear();
      for (TypePizza t : TypePizza.values()) {
          choiceBoxFiltreType.getItems().add(t.toString());
      }

      // ===============================
      // Affichage initial des pizzas
      // ===============================
      listePizzas.getItems().clear();
      for (Pizza p : CataloguePizzas.getPizzas()) {
          listePizzas.getItems().add(p.getNom());
      }

      labelListePizzas.setText("Toutes les pizzas en vente");

      // ===============================
      // Affichage initial des ingrédients
      // ===============================
      listeIngredients.getItems().clear();
      List<String> ingredientsAjoutes = new ArrayList<>();

      for (Pizza p : CataloguePizzas.getPizzas()) {
          for (Ingredient ing : p.getIngredients()) {
              if (!ingredientsAjoutes.contains(ing.getNom())) {
                  ingredientsAjoutes.add(ing.getNom());
                  listeIngredients.getItems().add(ing.getNom());
              }
          }
      }

      // ===============================
      // État initial
      // ===============================
      listeCommandes.getItems().clear();
      listeEvaluations.getItems().clear();

      labelListeCommandes.setText("Aucune commande affichée");

      clientConnecte = null;
      commandeEnCours = null;
  }

  

}