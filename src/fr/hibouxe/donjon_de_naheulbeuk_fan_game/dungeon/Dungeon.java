package fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.enemy.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.usable.potion.Potion;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.defensiveEquipment.ArchmageRobe;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.defensiveEquipment.BarbarianLoincloth;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.offensiveEquipment.DurandilAxe;

import java.util.*;

/**
 * Moteur du Labyrinthe (Dungeon Map).
 * Gère la matrice 2D de cellules, la génération procédurale par Backtracking (DFS),
 * le creusement de salles ouvertes (Carving) et le placement aléatoire de monstres.
 *
 * @author Hibouxe
 * @version 1.0
 */
public abstract class Dungeon {
    // Attributs
    private int width;
    private int height;
    private Cell[][] grid;
    private Random random = new Random();

    /**
     * Construit une grille de labyrinthe de dimensions (width x height).
     * Instancie chaque cellule Cell(x, y).
     *
     * @param width  Largeur de la grille en nombre de colonnes
     * @param height Hauteur de la grille en nombre de lignes
     */
    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height]; // Alloue la grille

        // Double boucle for pour initialiser chaque cellule
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Cell(x, y);
            }
        }
    }

    /**
     * Génère un labyrinthe parfait à l'aide de l'algorithme de Backtracking récursif (DFS).
     * Utilise une pile (ArrayDeque) pour dérouler et rembobiner les couloirs du donjon.
     */
    public void generateMaze() {
        generateMaze(0, 0);
    }

    /**
     * Génère un labyrinthe parfait à partir d'une position de départ précise.
     * Utile pour la descente d'escalier sans déplacer le joueur.
     */
    public void generateMaze(int startX, int startY) {
        Cell startCell = grid[startX][startY]; // Case de départ
        startCell.setVisited(true); // Marquer comme visitée
        Deque<Cell> stack = new ArrayDeque<>(); // Création de la pile
        stack.push(startCell); // Pose la cellule au sommet de la pile

        while (!stack.isEmpty()) {
            Cell current = stack.peek(); // Regarde la cellule au sommet
            List<Cell> neighbors = getUnvisitedNeighbors(current);

            if (!neighbors.isEmpty()) {
                // Choisir un voisin au hasard
                Cell chosen = neighbors.get(random.nextInt(neighbors.size()));

                // Enlever le mur entre la case actuelle et le voisin
                current.removeWallBetween(chosen);

                // Marquer comme visité et empiler
                chosen.setVisited(true);
                stack.push(chosen);
            } else {
                // Impasse : backtrack
                stack.pop();
            }
        }
    }

    /**
     * Creuse une salle rectangulaire dans le labyrinthe en abattant les murs intérieurs.
     *
     * @param startX     Coordonnée X du coin supérieur gauche de la salle
     * @param startY     Coordonnée Y du coin supérieur gauche de la salle
     * @param roomWidth  Largeur de la salle
     * @param roomHeight Hauteur de la salle
     */
    public void createRoom(int startX, int startY, int roomWidth, int roomHeight) {
        for (int x = startX; x < startX + roomWidth; x++) {
            for (int y = startY; y < startY + roomHeight; y++) {
                if (x + 1 < startX + roomWidth && x + 1 < width) {
                    grid[x][y].removeWallBetween(grid[x + 1][y]);
                }
                if (y + 1 < startY + roomHeight && y + 1 < height) {
                    grid[x][y].removeWallBetween(grid[x][y + 1]);
                }
            }
        }
    }

    /**
     * Génère un nombre défini de salles rectangulaires de tailles et positions aléatoires.
     *
     * @param numberOfRooms Nombre de salles à creuser
     * @param minSize       Taille minimale d'une salle
     * @param maxSize       Taille maximale d'une salle
     */
    public void generateRandomRooms(int numberOfRooms, int minSize, int maxSize) {
        for (int i = 0; i < numberOfRooms; i++) {
            // 1. Taille aléatoire pour cette salle
            int roomWidth = random.nextInt(maxSize - minSize + 1) + minSize;
            int roomHeight = random.nextInt(maxSize - minSize + 1) + minSize;

            // 2. Position aléatoire valide (sans dépasser la grille)
            int startX = random.nextInt(width - roomWidth);
            int startY = random.nextInt(height - roomHeight);

            // 3. Creuser la salle !
            createRoom(startX, startY, roomWidth, roomHeight);
        }
    }

    /**
     * Retourne la liste des cellules voisines de 'current' qui n'ont pas encore été visitées.
     *
     * @param current La cellule analysée
     * @return Liste des cellules voisines non visitées
     */
    private List<Cell> getUnvisitedNeighbors(Cell current) {
        int x = current.getX();
        int y = current.getY();
        List<Cell> neighbors = new ArrayList<>();

        if (y - 1 >= 0 && grid[x][y - 1].isVisited() == false) {
            neighbors.add(grid[x][y - 1]);
        }
        if (y + 1 < height && grid[x][y + 1].isVisited() == false) {
            neighbors.add(grid[x][y + 1]);
        }
        if (x - 1 >= 0 && grid[x - 1][y].isVisited() == false) {
            neighbors.add(grid[x - 1][y]);
        }
        if (x + 1 < width && grid[x + 1][y].isVisited() == false) {
            neighbors.add(grid[x + 1][y]);
        }
        return neighbors;
    }

    /**
     * Place aléatoirement un nombre d'ennemis sur les cases du labyrinthe (hors case 0,0).
     *
     * @param count Nombre de monstres à faire apparaître
     */
    public void generateMonsters(int count, int startX, int startY) {
        for (int i = 0; i < count; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if (x != startX || y != startY) { // Pas de monstre sur la case du joueur
                // 1. Création du groupe vide
                List<Character> enemyGroup = new ArrayList<>();

                // 2. Taille aléatoire entre 1 et 3
                int groupSize = random.nextInt(3) + 1;

                // 3. Ajout des gobelins dans le groupe
                for(int j = 0; j < groupSize; j++) {
                    enemyGroup.add(getRandomMonster());
                }

                // 4. On place le groupe complet sur la case
                grid[x][y].setMonsters(enemyGroup);
            }
        }
    }

    /**
     * Table de rencontres aléatoires.
     * Renvoie un monstre au hasard parmi le bestiaire.
     */
    private Character getRandomMonster() {
        int roll = random.nextInt(6); // Car tu as 6 types de monstres de base

        switch (roll) {
            case 0: return new Orc();
            case 1: return new Skeleton();
            case 2: return new Spider();
            case 3: return new Troll();
            case 4: return new Undead();
            default: return new Goblin();
        }
    }

    public void generateStairs(int count) {
        for (int i = 0; i < count; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if (x != 0 || y != 0) { // Pas d'escalier sur la case de départ (0, 0)
                grid[x][y].setStairs(true); // Placement des escalier
            }
        }
    }

    /**
     * Place aléatoirement un nombre de coffres d'objets sur les cases du labyrinthe (hors case 0,0).
     *
     * @param count Nombre de coffres à générer
     */
    public void generateItems(int count) {
        for (int i = 0; i < count; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if (x != 0 || y != 0) {
                int roll = random.nextInt(4);
                Item loot;
                switch (roll) {
                    case 1:
                        loot = new DurandilAxe();
                        break;
                    case 2:
                        loot = new BarbarianLoincloth();
                        break;
                    case 3:
                        loot = new ArchmageRobe();
                        break;
                    default:
                        loot = new Potion("Potion de soin", "une potion de vie simple. Rend +10 PV. Usage Unique.", 10);
                        break;
                }
                grid[x][y].setItem(loot);
            }
        }
    }

    /**
     * Deplace tous les monstres du donjon de 1 case au tour par tour (Style Pokemon Donjon Mystere).
     * Si un monstre repere le joueur (distance &lt;= 4), il le traque. Sinon, il se balade.
     *
     * @param team L'equipe du joueur
     * @param menu La vue principale (Injectee)
     */
    public void moveMonsters(Team team, Menu menu) {
        List<Character> movedMonsters = new ArrayList<>();
        int teamX = team.getX();
        int teamY = team.getY();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell currentCell = grid[x][y];
                if (currentCell.hasMonster()) {
                    List<Character> monstersOnCell = new ArrayList<>(currentCell.getMonsters());

                    for (Character monster : monstersOnCell) {
                        if (movedMonsters.contains(monster)) {
                            continue; // Deja deplace pendant ce tour
                        }

                        int targetX = x;
                        int targetY = y;
                        int distance = Math.abs(x - teamX) + Math.abs(y - teamY);

                        if (distance <= 4) {
                            // Mode Traque : Se rapprocher du joueur
                            if (x < teamX && !currentCell.isWallEast()) {
                                targetX = x + 1;
                            } else if (x > teamX && !currentCell.isWallWest()) {
                                targetX = x - 1;
                            } else if (y < teamY && !currentCell.isWallSouth()) {
                                targetY = y + 1;
                            } else if (y > teamY && !currentCell.isWallNorth()) {
                                targetY = y - 1;
                            }
                        } else {
                            // Mode Balade : Deplacement aleatoire d'une case
                            List<int[]> possibleMoves = new ArrayList<>();
                            if (!currentCell.isWallNorth()) possibleMoves.add(new int[]{x, y - 1});
                            if (!currentCell.isWallSouth()) possibleMoves.add(new int[]{x, y + 1});
                            if (!currentCell.isWallWest()) possibleMoves.add(new int[]{x - 1, y});
                            if (!currentCell.isWallEast()) possibleMoves.add(new int[]{x + 1, y});

                            if (!possibleMoves.isEmpty()) {
                                int[] chosenMove = possibleMoves.get(random.nextInt(possibleMoves.size()));
                                targetX = chosenMove[0];
                                targetY = chosenMove[1];
                            }
                        }

                        // Effectuer le deplacement s'il a bouge
                        if (targetX != x || targetY != y) {
                            currentCell.getMonsters().remove(monster);
                            grid[targetX][targetY].getMonsters().add(monster);
                            movedMonsters.add(monster);
                        }
                    }
                }
            }
        }
    }

    /**
     * @return Largeur du labyrinthe (Nombre de colonnes)
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width Nouvelle largeur
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return Hauteur du labyrinthe (Nombre de lignes)
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height Nouvelle hauteur
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return La matrice 2D des cellules
     */
    public Cell[][] getGrid() {
        return grid;
    }

    /**
     * @param grid Nouvelle matrice de cellules
     */
    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    /**
     * Méthode générant le donjon.
     * Doit être implémentée par les classes filles.
     */
    public abstract void generate();
}
