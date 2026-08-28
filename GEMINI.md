# Instructions Cruciales (Lues automatiquement par l'IA)
# Règles d'Or Architecturales - Donjon de Naheulbeuk Fan Game

Ces règles doivent STRICTEMENT dicter chaque décision de conception et de refactoring. Toute violation doit être corrigée immédiatement.

## 1. Zéro Allocation dans la Boucle de Rendu (Anti-GC Stuttering)
- **Règle :** Ne **JAMAIS** instancier de nouveaux objets (
ew ArrayList<>(), 
ew Vector3(), etc.) à l'intérieur d'une boucle ender() (Vue) ou update() (Moteur) appelée 60 fois par seconde.
- **Solution :** Utiliser des objets persistants, des tableaux (Array<>), et le système de Pool de LibGDX (obtain() / ree()) EN DEHORS des boucles itératives critiques. Les listes temporaires sont interdites.

## 2. Respect du Modèle Pur (SOLID - DIP / OCP)
- **Règle :** Les classes du package model/ (entités, armes, labyrinthe, Moteur) **NE DOIVENT JAMAIS** avoir d'imports liés au framework graphique (ex: pas de import com.badlogic.gdx.*). 
- **Solution :** Utiliser des DTO purs (Data Transfer Objects), des interfaces (ex: IMonsterRepository), et injecter les dépendances liées aux I/O ou au graphique depuis une couche d'infrastructure (ex: JsonMonsterLoader dans un package infrastructure). Pas de singletons cachés (DataManager).

## 3. Architecture MVC Stricte (Anti-God Controller)
- **Contrôleur (controller/) :** Se limite à lire les entrées utilisateurs (inputs), construire une intention de jeu (moveIntent), la déléguer au Moteur/Modèle, et mettre à jour la Vue si besoin.
- **Moteur/Modèle (model/) :** Contient 100% de la logique métier : calculs mathématiques (Pythagore, AABB), collisions, gestion des coordonnées flottantes (playerX, playerZ), et logique d'Intelligence Artificielle. Le Contrôleur ne doit pas faire de physique.
- **Vue (iew/) :** Se limite à dessiner les coordonnées fournies par le Modèle. Ne prend aucune décision logique.
## 4. Système de Déplacement (Libre vs Grille)
- **Règle :** L'exploration n'est PAS en case par case (style Pokémon Donjon Mystère). Le jeu utilise un déplacement libre et continu (façon Octopath Traveler / Dragon Quest classique).
- **Solution :** La logique de déplacement, de hitboxes et de collisions dans le Modèle doit se faire via des coordonnées flottantes continues (float x, y, z) et non par un système d'index de grille (Grid-based).


## ATTENTION AGENT:
Tu dois **toujours** respecter ces règles dans tes propositions de code. Aucune allocation dans les boucles de rendu, et maintient le MVC pur !
## 5. Infrastructure I/O & Injection de Dpendance
- **Rgle :** Toutes les classes effectuant des entres/sorties natives (lecture de fichiers, audio, sauvegarde) doivent tre isoles dans le package infrastructure/.
- **Solution :** Ne pas utiliser de Singletons (getInstance()) pour les managers techniques. Ils doivent tre instancis  la racine (DesktopLauncher / HD2DGameApp) et injects via le constructeur.

## 6. Chemins de Fichiers & DesktopLauncher
- **Rgle :** LibGDX sur desktop est sensible au Working Directory. Un lancement via gradlew desktop:run s'excute dans desktop/ (les assets sont dans ../assets/), tandis qu'un lancement via IntelliJ s'excute souvent  la racine (les assets sont dans assets/).
- **Solution :** Toujours vrifier l'existence du fichier avant l'injection : new java.io.File("assets/...").exists() ? "assets/..." : "../assets/..."


## 7. Rigueur Architecturale (Zéro "Vite Fait")
- **Règle :** Il est strictement interdit d'implémenter des fonctionnalités de manière précipitée ("quick and dirty") ou de court-circuiter l'architecture pour gagner du temps. Pas de "Magic Strings" dans la logique métier, pas de logique de craft/économie dans les contrôleurs de vue, et pas de switch géants (utiliser le pattern Strategy).
- **Solution :** Si une fonctionnalité demande de violer le SRP ou l'OCP (SOLID) pour être intégrée, le design DOIT être refactoré d'abord. Chaque nouvelle entité (bâtiment, monstre, objet) doit être ajoutée via un registre de données (Registry/Factory) ou un pattern dédié, sans jamais altérer le moteur central (Core Engine).
