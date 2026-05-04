package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pizzas.Commande;
import pizzas.Pizza;
import java.io.File;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javafx.scene.control.Alert;
import pizzas.GestionCommande;
import pizzas.CataloguePizzas;
import pizzas.Ingredient;
import pizzas.Client;

import pizzas.TypePizza;

/**
 * Contrôleur JavaFX de la fenêtre du pizzaïolo.
 *
 * Cette classe permet au pizzaïolo de :
 * <ul>
 *   <li>gérer les ingrédients (création, modification, interdiction)</li>
 *   <li>créer et modifier les pizzas</li>
 *   <li>consulter les commandes (traitées ou non)</li>
 *   <li>consulter des statistiques globales et par client</li>
 * </ul>
 *
 * @author Lina SIDENNAS
 */
public class PizzaioloControleur {

  @FXML
  private ChoiceBox<String> choiceBoxTypeIngredient;

  @FXML
  private ChoiceBox<String> choiceBoxTypePizza;

  @FXML
  private ComboBox<String> comboBoxClients;

  @FXML
  private TextField entreeBeneficeClient;

  @FXML
  private TextField entreeBeneficeCommande;

  @FXML
  private TextField entreeBeneficeTotalCommandes;

  @FXML
  private TextField entreeBeneficeTotalPizza;

  @FXML
  private TextField entreeBeneficeUnitairePizza;

  @FXML
  private TextField entreeNbCommandesPizza;

  @FXML
  private TextField entreeNbPizzasClient;

  @FXML
  private TextField entreeNomIngredient;

  @FXML
  private TextField entreeNomPizza;

  @FXML
  private TextField entreeNombreTotalCommandes;

  @FXML
  private TextField entreePhotoPizza;

  @FXML
  private TextField entreePrixIngredient;

  @FXML
  private TextField entreePrixMinimalPizza;

  @FXML
  private TextField entreePrixVentePizza;

  @FXML
  private Label labelListeCommandes;

  @FXML
  private Label labelListeIngredients;

  @FXML
  private Label labelListePizzas;

  @FXML
  private ListView<String> listeCommandes;

  @FXML
  private ListView<String> listeIngredients;

  @FXML
  private ListView<String> listePizzas;

  /**
   * Liste de tous les ingrédients disponibles dans l'application.
   * Elle contient l'ensemble des ingrédients pouvant être utilisés
   * pour la composition des pizzas.
   */
  private List<String> ingredientsDisponibles = new ArrayList<>();

  /**
   * Association entre chaque type de pizza et la liste de ses ingrédients.
   * La clé représente le nom de la pizza, et la valeur correspond
   * aux ingrédients qui la composent.
   */
  private Map<String, List<String>> ingredientsParPizza = new HashMap<>();

  /**
   * Liste des commandes actuellement affichées à l'écran.
   * Elle permet de stocker temporairement les commandes visibles
   * dans l'interface utilisateur.
   */
  private List<Commande> commandesAffichees = new ArrayList<>();

  /**
   * Indique l'état courant de l'affichage des ingrédients d'une pizza.
   * Si {@code true}, les ingrédients de la pizza sélectionnée sont affichés.
   * Si {@code false}, ils sont masqués.
   */
  private boolean affichageIngredientsPizza = false;

  /**
   * Association entre chaque type de pizza et la liste des ingrédients interdits.
   * Ces ingrédients ne peuvent pas être sélectionnés ou ajoutés
   * pour la pizza correspondante.
   */
  private Map<String, List<String>> ingredientsInterdits = new HashMap<>();

  /**
   * Recalcule le prix minimal et le bénéfice unitaire
   * d'une pizza à partir de ses ingrédients.
   *
   * @param pizza nom de la pizza concernée
   */
  private void recalculerPrixEtBenefice(String pizza) {

    List<String> ingredients = ingredientsParPizza.get(pizza);
    if (ingredients == null)
      return;

    double prixMinimal = 0.0;

    for (String ingredient : ingredients) {
      int debut = ingredient.indexOf("(");
      int fin = ingredient.indexOf("€");

      if (debut > 0 && fin > debut) {
        String prixStr = ingredient.substring(debut + 1, fin).trim();
        prixMinimal += Double.parseDouble(prixStr);
      }
    }

    entreePrixMinimalPizza.setText(String.format("%.2f", prixMinimal));

    String pizzaStr = pizza;
    int tiret = pizzaStr.indexOf("-");
    int euro = pizzaStr.indexOf("€");

    if (tiret > 0 && euro > tiret) {
      double prixVente = Double.parseDouble(pizzaStr.substring(tiret + 1, euro).trim());

      double benefice = prixVente - prixMinimal;
      entreeBeneficeUnitairePizza.setText(String.format("%.2f", benefice));
    }
  }
  
  /**
   * Calcule les statistiques d'une pizza :
   * - nombre total de pizzas commandées
   * - bénéfice total généré
   *
   * @param pizza pizza dont on calcule les statistiques
   */
  private void calculerStatsPizza(Pizza pizza) {

	    int nbCommandes = 0;
	    double beneficeTotal = 0.0;

	    for (Commande c : GestionCommande.getInstance().getCommandesTraitees()) {

	        for (Map.Entry<Pizza, Integer> e : c.getPizzas().entrySet()) {

	            if (e.getKey().equals(pizza)) {
	                int quantite = e.getValue();

	                nbCommandes += quantite;
	                beneficeTotal += pizza.getBeneficeUnitaire() * quantite;
	            }
	        }
	    }

	    entreeNbCommandesPizza.setText(String.valueOf(nbCommandes));
	    entreeBeneficeTotalPizza.setText(String.format("%.2f", beneficeTotal));
	}

  /**
   * Affiche la liste des pizzas triées par nombre de commandes
   * décroissant.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonAfficherListeTrieePizzas(ActionEvent event) {

    Map<String, Integer> compteur = new HashMap<>();

    for (Commande c : GestionCommande.getInstance().getCommandesTraitees()) {
      for (Map.Entry<Pizza, Integer> e : c.getPizzas().entrySet()) {
        compteur.merge(e.getKey().getNom(), e.getValue(), Integer::sum);
      }
    }

    List<String> pizzasTriees = new ArrayList<>(compteur.keySet());
    pizzasTriees.sort((a, b) -> compteur.get(b) - compteur.get(a));

    listePizzas.getItems().setAll(pizzasTriees);
    labelListePizzas.setText("Pizzas triées par nombre de commandes");
  }

  /**
   * Affiche tous les ingrédients disponibles.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonAfficherTousIngredients(ActionEvent event) {
    listeIngredients.getItems().setAll(ingredientsDisponibles);
    labelListeIngredients.setText("Tous les ingrédients disponibles");
    affichageIngredientsPizza = false;
  }

  /**
   * Affiche toutes les pizzas du catalogue.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonAfficherToutesPizzas(ActionEvent event) {
    listePizzas.getItems().clear();
    for (Pizza p : CataloguePizzas.getPizzas()) {
      listePizzas.getItems().add(p.getNom());
    }
    labelListePizzas.setText("Toutes les pizzas");
  }

  /**
   * Ajoute un ingrédient sélectionné à la pizza sélectionnée.
   * Empêche l'ajout de doublons.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonAjouterIngredientPizza(ActionEvent event) {

      String pizza = listePizzas.getSelectionModel().getSelectedItem();
      String ingredient = listeIngredients.getSelectionModel().getSelectedItem();

      if (pizza == null || ingredient == null)
          return;

      // Récupération / création de la liste d’ingrédients de la pizza
      List<String> ingredientsPizza =
              ingredientsParPizza.computeIfAbsent(pizza, k -> new ArrayList<>());

      // 🔍 Vérification du doublon
      if (ingredientsPizza.contains(ingredient)) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Erreur");
          alert.setHeaderText("Ingrédient déjà présent");
          alert.setContentText(
                  "L'ingrédient \"" + ingredient + "\" est déjà présent dans la pizza \"" + pizza + "\"."
          );
          alert.showAndWait();
          return; // ❌ on bloque l’ajout
      }

      // ✅ Ajout si non présent
      ingredientsPizza.add(ingredient);

      listeIngredients.getItems().setAll(ingredientsPizza);
      labelListeIngredients.setText("Ingrédients de la pizza sélectionnée");
      affichageIngredientsPizza = true;

      recalculerPrixEtBenefice(pizza);
  }

  /**
   * Affiche toutes les commandes déjà traitées
   * et met à jour les statistiques globales.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonCommandesDejaTraitees(ActionEvent event) {

    List<Commande> traitees = GestionCommande.getInstance().getCommandesTraitees();

    listeCommandes.getItems().clear();
    commandesAffichees.clear();

    for (Commande c : traitees) {
      commandesAffichees.add(c);
      listeCommandes.getItems().add(c.toString());
    }
    // ===== STATISTIQUES GLOBALES =====

    // Nombre total de commandes
    entreeNombreTotalCommandes.setText(String.valueOf(GestionCommande.getInstance().getCommandesTraitees().size()));

    // Bénéfice total des commandes
    double beneficeTotal = 0;
    for (Commande cmd : GestionCommande.getInstance().getCommandesTraitees()) {
      beneficeTotal += cmd.getPrixTotal();
    }
    entreeBeneficeTotalCommandes.setText(String.format("%.2f", beneficeTotal));

  }

  /**
   * Affiche les commandes non traitées afin de pouvoir les valider.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonCommandesNonTraitees(ActionEvent event) {

      List<Commande> nonTraitees =
          GestionCommande.getInstance().recupererCommandesNonTraitees();

      listeCommandes.getItems().clear();
      commandesAffichees.clear();

      for (Commande c : nonTraitees) {
          commandesAffichees.add(c);
          listeCommandes.getItems().add(c.toString());
      }

      labelListeCommandes.setText("Commandes non traitées (cliquer pour valider)");
  }

  /**
   * Affiche les commandes traitées d'un client sélectionné
   * et calcule ses statistiques.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonCommandesTraiteesClient(ActionEvent event) {

    String nomClient = comboBoxClients.getValue();
    if (nomClient == null)
      return;

    listeCommandes.getItems().clear();
    commandesAffichees.clear();

    labelListeCommandes.setText("Commandes traitées de " + nomClient);

    for (Commande c : GestionCommande.getInstance().getCommandesTraitees()) {
      if (c.getClient().getNom().equals(nomClient)) {
        commandesAffichees.add(c);
        listeCommandes.getItems().add(c.toString());
      }
    }

    // ======================
    // 📊 STATISTIQUES CLIENT
    // ======================
    int nbPizzas = 0;
    double beneficeClient = 0;

    for (Commande c : commandesAffichees) {
      for (Map.Entry<Pizza, Integer> e : c.getPizzas().entrySet()) {
        nbPizzas += e.getValue();
        beneficeClient += e.getKey().getBeneficeUnitaire() * e.getValue();
      }
    }

    entreeNbPizzasClient.setText(String.valueOf(nbPizzas));
    entreeBeneficeClient.setText(String.format("%.2f", beneficeClient));
  }

  /**
   * Crée un nouvel ingrédient après vérification
   * de l'absence de doublon.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonCreerIngredient(ActionEvent event) {

      String nom = entreeNomIngredient.getText();
      String prixStr = entreePrixIngredient.getText();

      if (nom == null || nom.isBlank() || prixStr == null || prixStr.isBlank()) {
          return;
      }

      double prix;
      try {
          prix = Double.parseDouble(prixStr);
      } catch (NumberFormatException e) {
          return;
      }

      // 🔍 Vérification des doublons (nom + prix)
      for (String ing : ingredientsDisponibles) {

          // Exemple : "Tomate (1.5 €)"
          int i = ing.indexOf("(");
          if (i > 0) {
              String nomExistant = ing.substring(0, i).trim();
              double prixExistant = Double.parseDouble(
                      ing.substring(i + 1, ing.indexOf("€")).trim()
              );

              if (nomExistant.equalsIgnoreCase(nom) && prixExistant == prix) {
                  Alert alert = new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("Erreur");
                  alert.setHeaderText("Ingrédient déjà existant");
                  alert.setContentText(
                          "L'ingrédient \"" + nom + "\" avec le prix " + prix + " € existe déjà."
                  );
                  alert.showAndWait();
                  return; // ❌ on bloque l’ajout
              }
          }
      }

      // ✅ Création si aucun doublon
      String ingredient = nom + " (" + prix + " €)";
      ingredientsDisponibles.add(ingredient);

      if (!affichageIngredientsPizza) {
          listeIngredients.getItems().setAll(ingredientsDisponibles);
          labelListeIngredients.setText("Tous les ingrédients disponibles");
      }
  }

  /**
   * Crée une nouvelle pizza et l'ajoute au catalogue
   * après vérification de l'unicité du nom.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonCreerPizza(ActionEvent event) {

      String nom = entreeNomPizza.getText();
      String type = choiceBoxTypePizza.getValue();
      String prixStr = entreePrixVentePizza.getText();

      if (nom == null || nom.isBlank() || type == null || prixStr.isBlank()) {
          return;
      }

      // 🔍 Vérification : nom déjà utilisé
      for (Pizza pizzaExistante : CataloguePizzas.getPizzas()) {
          if (pizzaExistante.getNom().equalsIgnoreCase(nom)) {

              Alert alert = new Alert(Alert.AlertType.ERROR);
              alert.setTitle("Erreur");
              alert.setHeaderText("Nom de pizza déjà utilisé");
              alert.setContentText(
                      "Une pizza nommée \"" + nom + "\" existe déjà.\n"
                    + "Veuillez choisir un autre nom."
              );
              alert.showAndWait();
              return; // ❌ on bloque la création
          }
      }

      double prix;
      try {
          prix = Double.parseDouble(prixStr);
      } catch (NumberFormatException e) {
          return;
      }

      // ✅ Création de la pizza
      Pizza p = new Pizza(nom, TypePizza.valueOf(type));
      p.setPrixVente(prix);

      CataloguePizzas.ajouterPizza(p);

      listePizzas.getItems().add(p.getNom());

      // Nettoyage champs
      entreeNomPizza.clear();
      entreePrixVentePizza.clear();
  }

  /**
   * Interdit un ingrédient pour un type de pizza donné.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonInterdireIngredient(ActionEvent event) {
    String ingredientStr = listeIngredients.getSelectionModel().getSelectedItem();
    if (ingredientStr == null)
      return;

    int i = ingredientStr.indexOf("(");
    String ingredient = (i > 0) ? ingredientStr.substring(0, i).trim() : ingredientStr;
    String type = choiceBoxTypeIngredient.getValue();

    if (ingredient == null || type == null)
      return;

    ingredientsInterdits.computeIfAbsent(ingredient, k -> new ArrayList<>()).add(type);

    System.out.println("Ingrédient " + ingredient + " interdit pour " + type);
  }

  /**
   * Modifie le prix d'un ingrédient sélectionné.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonModifierPrixIngredient(ActionEvent event) {
    int index = listeIngredients.getSelectionModel().getSelectedIndex();
    if (index < 0)
      return;

    String nom = entreeNomIngredient.getText();
    String prixStr = entreePrixIngredient.getText();
    if (nom.isBlank() || prixStr.isBlank())
      return;

    double prix = Double.parseDouble(prixStr);
    String nouveau = nom + " (" + prix + " €)";

    ingredientsDisponibles.set(index, nouveau);
    listeIngredients.getItems().setAll(ingredientsDisponibles);
  }

  /**
   * Modifie le prix de vente d'une pizza sélectionnée
   * après vérification du prix minimal.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonModifierPrixPizza(ActionEvent event) {

    int index = listePizzas.getSelectionModel().getSelectedIndex();
    if (index < 0)
      return;

    String anciennePizza = listePizzas.getItems().get(index);

    String nom = entreeNomPizza.getText();
    String type = choiceBoxTypePizza.getValue();
    String prixStr = entreePrixVentePizza.getText();

    if (nom.isBlank() || type == null || prixStr.isBlank())
      return;

    double prix;

    try {
      prix = Double.parseDouble(prixStr);
    } catch (NumberFormatException e) {
      return;
    }
    double prixMinimal = Double.parseDouble(entreePrixMinimalPizza.getText().replace(',', '.'));
    if (prix < prixMinimal) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Erreur");
      alert.setHeaderText("Prix invalide");
      alert.setContentText("Le prix de vente doit être supérieur ou égal au prix minimal ("
          + String.format("%.2f", prixMinimal) + " €)");
      alert.showAndWait();
      return;
    }

    // 🔥 récupération sécurisée des ingrédients
    List<String> ingredients = ingredientsParPizza.get(anciennePizza);
    if (ingredients == null) {
      ingredients = new ArrayList<>();
    }

    // 🔍 retrouver la vraie pizza
    Pizza p = null;
    for (Pizza pizza : CataloguePizzas.getPizzas()) {
      if (pizza.getNom().equals(anciennePizza)) {
        p = pizza;
        break;
      }
    }
    if (p == null)
      return;

    // ✅ mise à jour de l'objet métier
    p.setPrixVente(prix);

    // 🔄 rafraîchir l'affichage (le nom NE CHANGE PAS)
    listePizzas.getItems().set(index, p.getNom());

    // Rafraîchissement si la pizza modifiée est sélectionnée
    listePizzas.getSelectionModel().select(index);
    listeIngredients.getItems().setAll(ingredients);
    labelListeIngredients.setText("Ingrédients de la pizza sélectionnée");
  }

  /**
   * Permet de sélectionner une image associée à une pizza.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonParcourirPhotoPizza(ActionEvent event) {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Choisir une image de pizza");
    chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
    File file = chooser.showOpenDialog(null);
    if (file != null)
      entreePhotoPizza.setText(file.getAbsolutePath());
  }

  /**
   * Supprime un ingrédient de la pizza sélectionnée.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonSupprimerIngredientPizza(ActionEvent event) {
    String pizza = listePizzas.getSelectionModel().getSelectedItem();
    String ingredient = listeIngredients.getSelectionModel().getSelectedItem();
    if (pizza == null || ingredient == null)
      return;

    ingredientsParPizza.get(pizza).remove(ingredient);
    listeIngredients.getItems().setAll(ingredientsParPizza.get(pizza));
    recalculerPrixEtBenefice(pizza);

  }

  /**
   * Vérifie les ingrédients interdits pour le type
   * de la pizza sélectionnée.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionBoutonVerifierValiditeIngredientsPizza(ActionEvent event) {

    String nomPizza = listePizzas.getSelectionModel().getSelectedItem();
    if (nomPizza == null)
      return;

    Pizza pizza = null;
    for (Pizza p : CataloguePizzas.getPizzas()) {
      if (p.getNom().equals(nomPizza)) {
        pizza = p;
        break;
      }
    }
    if (pizza == null)
      return;

    String typePizza = pizza.getType().name();

    List<String> ingredientsInvalides = new ArrayList<>();

    List<String> ingredientsPizza = ingredientsParPizza.get(nomPizza);
    if (ingredientsPizza == null)
      return;

    for (String ingStr : ingredientsPizza) {

      int i = ingStr.indexOf("(");
      String nomIng = (i > 0) ? ingStr.substring(0, i).trim() : ingStr;

      List<String> typesInterdits = ingredientsInterdits.get(nomIng);

      if (typesInterdits != null && typesInterdits.contains(typePizza)) {
        ingredientsInvalides.add(ingStr);
      }
    }

    listeIngredients.getItems().setAll(ingredientsInvalides);
    labelListeIngredients.setText("Ingrédients invalides pour la pizza sélectionnée");
    affichageIngredientsPizza = true;
  }

  /**
   * Sélectionne une commande, la traite si nécessaire
   * et met à jour les statistiques associées.
   *
   * @param event événement souris
   */
  @FXML
  void actionListeSelectionCommande(MouseEvent event) {

      int index = listeCommandes.getSelectionModel().getSelectedIndex();
      if (index < 0)
          return;

      Commande c = commandesAffichees.get(index);

      // ✅ TRAITEMENT DE LA COMMANDE
      if (c.getEtat() == Commande.EtatCommande.VALIDEE) {
          c.traiter();
      }

      // Mise à jour affichage
      entreeBeneficeCommande.setText(
          String.format("%.2f", c.getPrixTotal())
      );

      // Mettre à jour la ligne
      listeCommandes.getItems().set(index, c.toString());

      // ✅ AJOUT DU CLIENT DANS LA COMBOBOX
      String nomClient = c.getClient().getNom();
      if (!comboBoxClients.getItems().contains(nomClient)) {
          comboBoxClients.getItems().add(nomClient);
      }
  }

  /**
   * Sélectionne un ingrédient et affiche ses informations.
   *
   * @param event événement souris
   */
  @FXML
  void actionListeSelectionIngredient(MouseEvent event) {
    String ingredient = listeIngredients.getSelectionModel().getSelectedItem();
    if (ingredient == null)
      return;

    // Exemple : "Tomate (1.5 €)"
    int indexParenthese = ingredient.indexOf("(");

    if (indexParenthese > 0) {
      String nom = ingredient.substring(0, indexParenthese).trim();
      String prix = ingredient.substring(indexParenthese + 1, ingredient.indexOf("€")).trim();

      entreeNomIngredient.setText(nom);
      entreePrixIngredient.setText(prix);
    }
  }

  /**
   * Sélectionne une pizza et affiche ses informations,
   * ingrédients et statistiques.
   *
   * @param event événement souris
   */
  @FXML
  void actionListeSelectionPizza(MouseEvent event) {

    String nomPizza = listePizzas.getSelectionModel().getSelectedItem();
    if (nomPizza == null)
      return;

    Pizza p = null;
    for (Pizza pizza : CataloguePizzas.getPizzas()) {
      if (pizza.getNom().equals(nomPizza)) {
        p = pizza;
        break;
      }
    }
    if (p == null)
      return;

    // Champs pizza
    entreeNomPizza.setText(p.getNom());
    choiceBoxTypePizza.setValue(p.getType().name());
    entreePrixVentePizza.setText(String.valueOf(p.getPrixVente()));
    calculerStatsPizza(p);
    // Ingrédients (priorité au cache UI, sinon modèle métier)
    List<String> ingredients = ingredientsParPizza.get(nomPizza);

    listeIngredients.getItems().clear();

    if (ingredients != null) {
      listeIngredients.getItems().addAll(ingredients);
    } else {
      // 🔁 fallback : modèle métier (anciennes pizzas)
      for (Ingredient ing : p.getIngredients()) {
        listeIngredients.getItems().add(ing.getNom() + " (" + ing.getPrix() + " €)");
      }
    }

    // Prix / bénéfices
    entreePrixMinimalPizza.setText(String.format("%.2f", p.getPrixMinimal()));
    entreeBeneficeUnitairePizza.setText(String.format("%.2f", p.getBeneficeUnitaire()));
    labelListeIngredients.setText("Ingrédients de la pizza sélectionnée");
    affichageIngredientsPizza = true;

  }

  /**
   * Affiche la fenêtre "À propos" de l'application.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionMenuApropos(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("À propos");
    alert.setHeaderText("Application Pizzeria");
    alert.setContentText("Projet JAVA – L3 Informatique");
    alert.showAndWait();
  }

  /**
   * Action de chargement (fonctionnalité future).
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionMenuCharger(ActionEvent event) {
    System.out.println("Chargement effectué");
  }

  /**
   * Ferme l'application.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionMenuQuitter(ActionEvent event) {
    System.exit(0);
  }

  /**
   * Action de sauvegarde (fonctionnalité future).
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionMenuSauvegarder(ActionEvent event) {
    System.out.println("Sauvegarde effectuée");
  }

  /**
   * Affiche les statistiques d'un client sélectionné.
   *
   * @param event événement JavaFX
   */
  @FXML
  void actionSelectionClient(ActionEvent event) {

    String nomClient = comboBoxClients.getValue();
    if (nomClient == null)
      return;

    int nbPizzas = 0;
    double beneficeClient = 0;

    for (Commande c : GestionCommande.getInstance().getCommandesTraitees()) {

      if (c.getClient().getNom().equals(nomClient)) {
        for (Map.Entry<Pizza, Integer> e : c.getPizzas().entrySet()) {
          nbPizzas += e.getValue();
          beneficeClient += e.getKey().getBeneficeUnitaire() * e.getValue();
        }
      }
    }

    entreeNbPizzasClient.setText(String.valueOf(nbPizzas));
    entreeBeneficeClient.setText(String.format("%.2f", beneficeClient));
  }

  /**
   * Ajoute une commande à la liste des commandes traitées.
   *
   * @param c commande à ajouter
   */
  @FXML
  public static void ajouterCommande(Commande c) {
    GestionCommande.getInstance().getCommandesTraitees().add(c);
  }

  /**
   * Initialise l'interface pizzaïolo :
   * - chargement des pizzas, ingrédients et commandes
   * - initialisation des listes et choix
   */
  @FXML
  void initialize() {

    // 1️⃣ Types autorisés
    for (TypePizza t : TypePizza.values()) {
      choiceBoxTypeIngredient.getItems().add(t.name());
      choiceBoxTypePizza.getItems().add(t.name());
    }

    // 2️⃣ Labels initiaux
    labelListeIngredients.setText("Tous les ingrédients disponibles");
    labelListePizzas.setText("Toutes les pizzas");
    labelListeCommandes.setText("Toutes les commandes");

    // 3️⃣ Listes vides
    listeIngredients.getItems().clear();
    listePizzas.getItems().clear();
    listeCommandes.getItems().clear();

    // 4️⃣ Sélections nettoyées
    listeIngredients.getSelectionModel().clearSelection();
    listePizzas.getSelectionModel().clearSelection();
    listeCommandes.getSelectionModel().clearSelection();

    // 5️⃣ Commandes existantes
    commandesAffichees.clear();
    for (Commande c : GestionCommande.getInstance().getCommandesTraitees()) {
      commandesAffichees.add(c);
      listeCommandes.getItems().add(c.toString());

      String nomClient = c.getClient().getNom();
      if (!comboBoxClients.getItems().contains(nomClient)) {
        comboBoxClients.getItems().add(nomClient);
      }
    }

    // =================================================
    // ✅ 6️⃣ INITIALISATION DES INGREDIENTS (IMPORTANT)
    // =================================================

    
    ingredientsParPizza.clear();
    ingredientsDisponibles.clear();

    for (Pizza p : CataloguePizzas.getPizzas()) {

      List<String> list = new ArrayList<>();

      for (Ingredient ing : p.getIngredients()) {
        String ingUI = ing.getNom() + " (" + ing.getPrix() + " €)";
        list.add(ingUI);

        if (!ingredientsDisponibles.contains(ingUI)) {
          ingredientsDisponibles.add(ingUI);
        }
      }

      ingredientsParPizza.put(p.getNom(), list);
    }

    // Affichage initial des ingrédients
    listeIngredients.getItems().setAll(ingredientsDisponibles);


    comboBoxClients.getItems().clear();

    for (Commande c : GestionCommande.getInstance().getCommandesTraitees()) {
        String nomClient = c.getClient().getNom();

        if (!comboBoxClients.getItems().contains(nomClient)) {
            comboBoxClients.getItems().add(nomClient);
        }
    }

  }

}