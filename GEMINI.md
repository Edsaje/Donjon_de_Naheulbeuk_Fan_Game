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


## ATTENTION AGENT:
Tu dois **toujours** respecter ces règles dans tes propositions de code. Aucune allocation dans les boucles de rendu, et maintient le MVC pur !