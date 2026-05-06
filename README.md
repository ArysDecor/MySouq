# MySouq 🏺 - L'Artisanat Marocain au bout des doigts

**MySouq** est une application Android moderne dédiée à la promotion de l'artisanat marocain traditionnel. Elle permet aux utilisateurs de découvrir des trésors authentiques, de gérer leurs coups de cœur et de commander des pièces uniques de l'artisanat local (Fès, Marrakech, etc.).

---

## ✨ Fonctionnalités Clés

### 🛍️ Catalogue & Shopping
*   **Accueil Dynamique :** Bannières d'offres, carrousel de catégories et ventes flash.
*   **Grille de Produits :** Navigation fluide dans le catalogue avec badges d'authenticité.
*   **Détails Produits :** Informations complètes (origine, artisan), bouton d'achat persistant et gestion des favoris.
*   **Panier Interactif :** Gestion des quantités, calcul du total en temps réel et processus de commande simplifié.

### 👤 Profil & Utilisateur
*   **Profil Dynamique :** Gestion des informations utilisateur (nom, email, ville) persistées localement.
*   **Historique des Commandes :** Suivi des commandes passées avec statuts (En cours, Livré).
*   **Gestion des Adresses :** Ajout et suppression d'adresses de livraison.
*   **Modes de Paiement :** Enregistrement sécurisé des moyens de paiement.
*   **Mode Invité :** Expérience utilisateur adaptée pour les utilisateurs non connectés.

### 🎨 Design & Expérience
*   **Material 3 :** Utilisation des derniers standards de Google pour une UI moderne et aérée.
*   **Dark Mode Natif :** L'application s'adapte automatiquement au thème du système.
*   **UX Mobile :** Navigation intuitive via Bottom Bar et boutons d'action fixés pour une ergonomie optimale.

---

## 🛠️ Stack Technique

L'application repose sur les technologies les plus modernes de l'écosystème Android :

*   **Langage :** [Kotlin 2.0](https://kotlinlang.org/)
*   **UI :** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Entièrement déclaratif)
*   **Architecture :** Clean Architecture + MVVM (Model-View-ViewModel)
*   **Injection de Dépendances :** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
*   **Persistance de Données :** 
    *   **Room :** Pour la base de données locale (Produits, Favoris).
    *   **DataStore (Preferences) :** Pour les paramètres utilisateur et le profil.
*   **Chargement d'Images :** [Coil](https://coil-kt.github.io/coil/) (Optimisé pour Compose)
*   **Navigation :** Jetpack Navigation Compose

---

## 🏗️ Architecture du Projet

Le code est organisé de manière modulaire pour garantir sa maintenabilité :

```text
com.example.mysouq
├── data/           # Implémentations des repositories, Room, DataStore
├── domain/         # Modèles métier et interfaces des repositories
├── ui/
│   ├── common/     # États UI (UiState)
│   ├── components/ # Composants réutilisables (ProductCard, etc.)
│   ├── navigation/ # Routes et graphe de navigation
│   ├── screens/    # Écrans de l'application (Home, Cart, Profile, etc.)
│   ├── theme/      # Système de design (Couleurs, Typo)
│   └── viewmodel/  # Logique métier liée à la vue
└── MainActivity.kt # Point d'entrée de l'application
```

---

## 🚀 Installation & Lancement

1.  Clonez le dépôt :
    ```bash
    git clone https://github.com/ArysDecor/MySouq.git
    ```
2.  Ouvrez le projet dans **Android Studio (Ladybug ou plus récent)**.
3.  Synchronisez le projet avec les fichiers Gradle.
4.  Lancez l'application sur un émulateur ou un appareil physique (API 24+).

---

## 📝 Auteur
Développé par **Yahya & Équipe**.
Version : **1.0.2**
