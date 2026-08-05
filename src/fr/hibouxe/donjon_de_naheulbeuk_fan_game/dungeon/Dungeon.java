package fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

/**
 * Modèle du Donjon (Carte du Labyrinthe).
 * Stocke la matrice 2D des cellules et délègue la génération à {@link DungeonGenerator}
 * ainsi que le déplacement des monstres à {@link MonsterAI}.
 *
 * @author Hibouxe
 * @version 2.0
 */
public abstract class Dungeon {
    private int width;
    private int height;
    private Cell[][] grid;

    private DungeonGenerator generator = new DungeonGenerator();
    private MonsterAI monsterAI = new MonsterAI();

    /**
     * Construit une grille de labyrinthe aux dimensions spécifiées.
     * Instancie chaque cellule de la grille.
     *
     * @param width  Largeur du donjon en nombre de colonnes
     * @param height Hauteur du donjon en nombre de lignes
     */
    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Cell(x, y);
            }
        }
    }

    /**
     * Génère un labyrinthe parfait à partir de la case (0, 0).
     */
    public void generateMaze() {
        generator.generateMaze(this, 0, 0);
    }

    /**
     * Génère un labyrinthe parfait à partir d'une position de départ spécifique.
     *
     * @param startX Coordonnée X de départ
     * @param startY Coordonnée Y de départ
     */
    public void generateMaze(int startX, int startY) {
        generator.generateMaze(this, startX, startY);
    }

    /**
     * Creuse une salle rectangulaire dans le donjon.
     *
     * @param startX     Coordonnée X du coin haut-gauche
     * @param startY     Coordonnée Y du coin haut-gauche
     * @param roomWidth  Largeur de la salle
     * @param roomHeight Hauteur de la salle
     */
    public void createRoom(int startX, int startY, int roomWidth, int roomHeight) {
        generator.createRoom(this, startX, startY, roomWidth, roomHeight);
    }

    /**
     * Génère un nombre défini de salles aléatoires dans le donjon.
     *
     * @param numberOfRooms Nombre de salles à creuser
     * @param minSize       Taille minimale d'une salle
     * @param maxSize       Taille maximale d'une salle
     */
    public void generateRandomRooms(int numberOfRooms, int minSize, int maxSize) {
        generator.generateRandomRooms(this, numberOfRooms, minSize, maxSize);
    }

    /**
     * Place aléatoirement un nombre d'ennemis sur les cases du labyrinthe.
     *
     * @param count  Nombre de groupes de monstres à générer
     * @param startX Coordonnée X à épargner (joueur)
     * @param startY Coordonnée Y à épargner (joueur)
     */
    public void generateMonsters(int count, int startX, int startY) {
        generator.generateMonsters(this, count, startX, startY);
    }

    /**
     * Place des coffres d'objets dans le donjon.
     *
     * @param count Nombre de coffres à générer
     */
    public void generateItems(int count) {
        generator.generateItems(this, count);
    }

    /**
     * Place des escaliers vers l'étage suivant.
     *
     * @param count Nombre d'escaliers à placer
     */
    public void generateStairs(int count) {
        generator.generateStairs(this, count);
    }

    /**
     * Déplace tous les monstres du donjon d'une case au tour par tour (IA BFS et Line of Sight).
     *
     * @param team L'équipe de la compagnie
     * @param menu La vue principale (Injectée)
     */
    public void moveMonsters(Team team, Menu menu) {
        monsterAI.moveMonsters(this, team, menu);
    }

    /**
     * Obtenir la largeur du donjon.
     *
     * @return La largeur (colonnes)
     */
    public int getWidth() {
        return width;
    }

    /**
     * Définir la largeur du donjon.
     *
     * @param width La nouvelle largeur
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Obtenir la hauteur du donjon.
     *
     * @return La hauteur (lignes)
     */
    public int getHeight() {
        return height;
    }

    /**
     * Définir la hauteur du donjon.
     *
     * @param height La nouvelle hauteur
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Obtenir la matrice 2D des cellules.
     *
     * @return La grille 2D du donjon
     */
    public Cell[][] getGrid() {
        return grid;
    }

    /**
     * Définir la grille du donjon.
     *
     * @param grid La nouvelle grille 2D
     */
    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    /**
     * Méthode générant le donjon. Doit être implémentée par les sous-classes.
     */
    public abstract void generate();
}
