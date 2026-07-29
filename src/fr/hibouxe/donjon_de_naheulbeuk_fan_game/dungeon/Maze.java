package fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.enemy.Goblin;
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
public class Maze {
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
    public Maze(int width, int height) {
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
        Cell startCell = grid[0][0]; // Case de départ
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
    public void generateMonsters(int count) {
        for (int i = 0; i < count; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if (x != 0 || y != 0) { // Pas de monstre sur la case de départ (0, 0)
                grid[x][y].setMonster(new Goblin()); // Placement d'un Gobelin par défaut
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
}
