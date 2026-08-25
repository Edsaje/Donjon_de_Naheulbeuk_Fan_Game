package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

import java.io.Serializable;

/**
 * Modèle du Donjon (Carte du Labyrinthe).
 * Stocke la matrice 2D des cellules et délègue la génération à {@link DungeonGenerator}
 * ainsi que le déplacement des monstres à {@link MonsterAI}.
 *
 * @author Hibouxe
 * @version 2.0
 */
public abstract class Dungeon implements Serializable {
    private static final long serialVersionUID = 1L;

    private int width;
    private int height;
    private Cell[][] grid;

    private transient DungeonGenerator generator = new DungeonGenerator();
    private transient MonsterAI monsterAI = new MonsterAI();
    private java.util.List<RoamingMonsterGroup> roamingMonsters = new java.util.ArrayList<>();
    private java.util.List<DungeonGenerator.Room> prefabRooms = new java.util.ArrayList<>();
    public java.util.List<DungeonGenerator.Room> getPrefabRooms() { return prefabRooms; }
    public void setPrefabRooms(java.util.List<DungeonGenerator.Room> rooms) { this.prefabRooms = rooms; }

    public java.util.List<RoamingMonsterGroup> getRoamingMonsters() {
        return roamingMonsters;
    }

    public void setRoamingMonsters(java.util.List<RoamingMonsterGroup> roamingMonsters) {
        this.roamingMonsters = roamingMonsters;
    }

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

    private DungeonGenerator getGenerator() {
        if (generator == null) generator = new DungeonGenerator();
        return generator;
    }

    private MonsterAI getMonsterAI() {
        if (monsterAI == null) monsterAI = new MonsterAI();
        return monsterAI;
    }

    /**
     * Génère un donjon hybride avec l'algorithme des Salles et Couloirs.
     */
    public void generateHybridDungeon() {
        getGenerator().generateHybridDungeon(this);
    }


    /**
     * Place aléatoirement un nombre d'ennemis sur les cases du labyrinthe.
     *
     * @param count  Nombre de groupes de monstres à générer
     * @param startX Coordonnée X à épargner (joueur)
     * @param startY Coordonnée Y à épargner (joueur)
     * @param repository Le repository de monstres
     */
    public void generateMonsters(int count, int startX, int startY, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository repository) {
        getGenerator().generateMonsters(this, count, startX, startY, repository);
    }

    /**
     * Place des coffres d'objets dans le donjon.
     *
     * @param count Nombre de coffres à générer
     */
    public void generateItems(int count) {
        getGenerator().generateItems(this, count);
    }

    /**
     * Place des escaliers vers l'étage suivant.
     *
     * @param count Nombre d'escaliers à placer
     */
    public void generateStairs(int count) {
        getGenerator().generateStairs(this, count);
    }

    /**
     * Déplace tous les monstres du donjon d'une case au tour par tour (IA BFS et Line of Sight).
     *
     * @param team L'équipe de la compagnie
     * @param ConsoleMenu La vue principale (Injectée)
     */
    public void moveMonsters(Team team) {
        getMonsterAI().moveMonsters(this, team);
    }

    private transient FogOfWarManager fogManager = new FogOfWarManager();

    private FogOfWarManager getFogManager() {
        if (fogManager == null) fogManager = new FogOfWarManager();
        return fogManager;
    }

    /**
     * Révèle les cases autour du joueur (Brouillard de Guerre).
     * Délégué au FogOfWarManager (SRP).
     *
     * @param playerX Coordonnée X du joueur
     * @param playerY Coordonnée Y du joueur
     * @param radius Rayon de vision (ex: 2 ou 3 cases)
     */
    public void updateFogOfWar(int playerX, int playerY, int radius) {
        getFogManager().updateVisibility(grid, width, height, playerX, playerY, radius);
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
     * Recherche et renvoie les coordonnées (X, Y) de la première case de sol navigable.
     *
     * @return Tableau [X, Y] de la case navigable
     */
    public int[] getFirstWalkablePosition() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y].isWalkable()) {
                    return new int[]{x, y};
                }
            }
        }
        return new int[]{1, 1};
    }

    /**
     * Prépare le donjon pour un étage spécifique.
     * C'est ici que chaque sous-classe gère sa taille, son layout et son peuplement selon l'étage.
     *
     * @param floorNumber Numéro de l'étage actuel
     * @param team La compagnie du joueur
     * @param repository Le repository de monstres
     * @return true si l'expédition est terminée (ex: victoire finale sur un boss), false sinon
     */
    public abstract boolean prepareFloor(int floorNumber, Team team, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository repository);

    /**
     * Retourne les dialogues d'introduction d'un étage.
     * @param floorNumber Numéro de l'étage
     * @return Liste des textes à afficher
     */
    public abstract java.util.List<String> getFloorIntroDialogues(int floorNumber);

    /**
     * Vérifie si les conditions de complétion de l'expédition sont remplies (ex: Boss vaincu).
     *
     * @param floorNumber Numéro de l'étage actuel
     * @return true si l'expédition doit prendre fin avec succès
     */
    public abstract boolean isExpeditionComplete(int floorNumber);
}