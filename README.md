# Le Donjon de Naheulbeuk - HD-2D Custom Engine

Bienvenue sur le depot du **Donjon de Naheulbeuk Fan Game**, un projet monumental de RPG au tour par tour. 

Ce projet est une **vitrine technologique et architecturale** : il s'agit d'un moteur de jeu **HD-2D** (melange de modeles 3D et de sprites 2D animes type Billboard) entierement programme "from scratch" en Java en utilisant le framework bas niveau **LibGDX**. 

Plutot que d'utiliser un moteur grand public (Unity, Godot, Unreal), l'integralite de la logique de jeu, des pipelines de rendu, de la gestion de la memoire et de l'architecture a ete concue sur mesure.

---

## 🎯 Fonctionnalites du Moteur (Engine Features)

### 1. Rendu HD-2D (LibGDX 3D API)
- **Environnements 3D** : Chargement de scenes 3D complexes exportees depuis Blender (format .obj ou g3db).
- **Sprites Billboards** : Moteur de rendu de sprites 2D (DecalBatch) projetes dans le monde 3D, toujours face a la camera (technique Billboard).
- **Camera Ortho-Perspective** : Systeme de camera isometrique/perspective inclinee a -45 degres pour reproduire le style visuel de jeux comme *Octopath Traveler*.
- **UI Custom** : Moteur de rendu d'interface (HUD) performant dessinant fenetres, textes, jauges et menus par-dessus le rendu 3D.

### 2. Architecture Logicielle (SOLID & MVC)
Le code respecte rigoureusement les principes **SOLID** et l'architecture **Modele-Vue-Controleur (MVC)**.
- **Separation Stricte** : La logique metier (model) est 100% decouplee de l'interface graphique (iew). 
- **Interfaces et Contrats** : La vue communique avec le controleur uniquement via des interfaces (IGameView, ICombatView). 
- **Design Patterns** : 
  - *State Machine* : Gestion du cycle de vie des combats (BattleState) et de l'exploration.
  - *Strategy Pattern* : Comportements de l'intelligence artificielle (IRoamingBehavior) evitant les longues suites de if/else.
  - *Service Locator / Registry* : Gestion de l'economie du Hub via le HubBuildingRegistry.

### 3. Optimisation Extreme (Zero Allocation)
Pour garantir une frequence d'affichage fluide (60 FPS constants) sans micro-saccades (stutters) dues au Garbage Collector de Java, le moteur applique la regle d'or du **Zero Allocation** dans ses boucles principales :
- Les listes temporaires sont pre-instanciees et nettoyees avec .clear() au lieu d'utiliser 
ew ArrayList<>().
- Utilisation de wrappers (comme des tableaux virtuels AbstractList) pour eviter de dupliquer ou copier des tableaux en memoire lors de la verification de l'ordre d'initiative.

### 4. Systemes de Jeu (Game Design)
- **Combats au Tour par Tour** : Systeme tactique riche avec gestion d'initiative, esquives critiques, sorts, et types de degats.
- **Dungeon Crawler (PMD)** : Deplacement libre en 3D avec detection de collisions physique continue (ExplorationEngine). 
- **Economie de Hub** : Construction et amelioration de batiments (Taverne, Forge) dans le campement en recoltant des materiaux dans le donjon.
- **Generation Aleatoire** : Labyrinthes proceduraux avec monstres errants (Roaming Monsters).

---

## 🏗️ Structure du Projet

`	ext
src/fr/hibouxe/donjon_de_naheulbeuk_fan_game/
├── controller/            # Orchestration (Game, Exploration, Battle)
├── model/                 # Coeur du moteur (Agnostique de LibGDX)
│   ├── combat/            # Moteur de resolution des attaques et IA
│   ├── dungeon/           # Logique physique, collisions, labyrinthe procedural
│   ├── entity/            # Classes du JDR (Ogre, Nain, Elfe, Golem)
│   ├── hub/               # Economie du village, batiments, couts
│   ├── item/              # Inventaire, equipements, materiaux
│   ├── save/              # Serialisation et sauvegarde binaire
│   └── random/            # Generateurs de nombres aleatoires pour l'aleatoire deterministe
└── view/                  # Implementation LibGDX 3D
    ├── contract/          # Interfaces (IGameView, ICombatView)
    └── graphic/           # Rendu visuel
        └── renderers/     # SRP : BattleArenaRenderer, DungeonSceneRenderer, HUDRenderer
`

---

## 🛠️ Compilation et Lancement

Ce projet utilise **Gradle** comme gestionnaire de taches. 

### Prerequis
- Java JDK 17 ou superieur.

### Commandes
1. **Lancer le jeu :**
   `ash
   ./gradlew desktop:run
   `
2. **Compiler les tests unitaires :**
   `ash
   ./gradlew test
   `
3. **Generer la documentation Javadoc complete :**
   `ash
   ./gradlew javadoc
   `
   *La documentation generee se trouvera dans core/build/docs/javadoc/.*

---

## 📜 Historique et Avenir

Ce projet a demarre comme un exercice d'apprentissage du langage Java et de la creation de jeux video a bas niveau. Les defis techniques releves (creation d'un moteur HD-2D complet, nettoyage des problematiques d'encodage de caracteres, architecture MVC) en font une base de code exemplaire.

Ce projet servira desormais de **plan d'architecture detaille** (blueprint) pour migrer le developpement vers le moteur **Godot (C#)**, permettant ainsi d'exploiter la puissance des editeurs visuels tout en conservant toute la logique metier developpee ici.