#  Mammamia – Application de gestion de pizzeria

##  Description

Ce projet a été réalisé dans le cadre de l’UE **Conception d’applications (L3 Informatique – UBO)**.

L’objectif est de développer une application Java permettant :

* la création et gestion de pizzas
* la gestion des commandes clients
* le suivi des statistiques de vente



---

##  Utilisateurs

###  Pizzaïolo

* Création de pizzas à partir d’ingrédients
* Définition des prix
* Consultation des commandes
* Accès aux statistiques (bénéfices, ventes…)

###  Client

* Consultation des pizzas
* Passage de commandes
* Gestion de compte (connexion)
* Évaluation des pizzas (notes et commentaires)

---

##  Fonctionnalités principales

###  Gestion des pizzas

* Création avec :

  * nom
  * type (viande, végétarienne, régionale)
  * ingrédients
* Calcul automatique du prix minimum
* Ajout d’image

###  Commandes

* Création de commandes
* Ajout de pizzas
* Validation / annulation
* Suivi des états :

  * créée
  * validée
  * traitée

###  Évaluations

* Note de 0 à 5
* Commentaire optionnel
* Moyenne des notes affichée

###  Statistiques

* Bénéfices par pizza / client / total
* Classement des pizzas
* Nombre de commandes

---

##  Architecture du projet

Le projet suit une architecture orientée objet avec plusieurs packages :

```bash
pizzas/      # logique métier (Pizza, Commande, Evaluation…)
io/          # gestion de la sauvegarde
tests/       # tests unitaires et d’intégration
```

---

##  Tests

* Tests unitaires (JUnit)
* Tests d’intégration
* Plans de tests fournis en PDF

---

##  Exécution

Le projet peut être lancé via :

* la classe `MainPizzas`
* ou une interface utilisateur (console ou JavaFX)

---

##  Technologies utilisées

* Java
* JavaFX
* JUnit
* Git / GitLab
* Checkstyle (qualité du code)

---

##  Objectifs pédagogiques

* Travail en groupe avec Git
* Qualité du code (tests, Javadoc, formatage)
* Conception orientée objet

---

##  Remarques

* Projet réalisé en groupe de 3
* Code structuré et documenté (Javadoc)
* Application testée via scénarios fonctionnels


