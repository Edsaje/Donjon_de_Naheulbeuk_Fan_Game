package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

import java.io.Serializable;

/**
 * Modle du Donjon (Carte du Labyrinthe).
 * Stocke la matrice 2D des cellules et dlgue la gnration  {@link DungeonGenerator}
 * ainsi que le dplacement des monstres  {@link MonsterAI}.
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
    private boolean drawWalls = true;
    public boolean isDrawWalls() { return drawWalls; }
    public void setDrawWalls(boolean drawWalls) { this.drawWalls = drawWalls; }
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
     * Construit une grille de labyrinthe aux dimensions spcifies.
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
     * Gnre un donjon hybride avec l'algorithme des Salles et Couloirs.
     */
    public void generateHybridDungeon() {
        getGenerator().generateHybridDungeon(this);
    }


    /**
     * Place alatoirement un nombre d'ennemis sur les cases du labyrinthe.
     *
     * @param count  Nombre de groupes de monstres  gnrer
     * @param startX Coordonne X  pargner (joueur)
     * @param startY Coordonne Y  pargner (joueur)
     * @param repository Le repository de monstres
     */
    public void generateMonsters(int count, int startX, int startY, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository repository, int floorNumber) {
        getGenerator().generateMonsters(this, count, startX, startY, repository, floorNumber);
    }

    /**
     * Place des coffres d'objets dans le donjon.
     *
     * @param count Nombre de coffres  gnrer
     */
    public void generateItems(int count) {
        getGenerator().generateItems(this, count);
    }

    /**
     * Place des escaliers vers l'tage suivant.
     *
     * @param count Nombre d'escaliers  placer
     */
    public void generateStairs(int count) {
        getGenerator().generateStairs(this, count);
    }

    /**
     * Dplace tous les monstres du donjon d'une case au tour par tour (IA BFS et Line of Sight).
     *
     * @param team L'quipe de la compagnie
     * @param ConsoleMenu La vue principale (Injecte)
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
     * Rvle les cases autour du joueur (Brouillard de Guerre).
     * Dlgu au FogOfWarManager (SRP).
     *
     * @param playerX Coordonne X du joueur
     * @param playerY Coordonne Y du joueur
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
     * Dfinir la largeur du donjon.
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
     * Dfinir la hauteur du donjon.
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
     * Dfinir la grille du donjon.
     *
     * @param grid La nouvelle grille 2D
     */
    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    /**
     * Recherche et renvoie les coordonnes (X, Y) de la premire case de sol navigable.
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
     * Recherche et renvoie le point de spawn ideal (au centre de la premiere salle).
     * Evite de spawner dans un coin de prefab (ce qui cause parfois des "voids").
     */
    public int[] getSpawnPosition() {
        if (prefabRooms != null && !prefabRooms.isEmpty()) {
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.DungeonGenerator.Room firstRoom = prefabRooms.get(0);
            return new int[]{firstRoom.getCenterX(), firstRoom.getCenterY()};
        }
        return getFirstWalkablePosition();
    }

    public int[] getBossSpawnPosition() {
        if (prefabRooms != null && !prefabRooms.isEmpty()) {
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.DungeonGenerator.Room lastRoom = prefabRooms.get(prefabRooms.size() - 1);
            return new int[]{lastRoom.getCenterX(), lastRoom.getCenterY()};
        }
        return getFirstWalkablePosition();
    }

    /**
     * Prpare le donjon pour un tage spcifique.
     * C'est ici que chaque sous-classe gre sa taille, son layout et son peuplement selon l'tage.
     *
     * @param floorNumber Numro de l'tage actuel
     * @param team La compagnie du joueur
     * @param repository Le repository de monstres
     * @return true si l'expdition est termine (ex: victoire finale sur un boss), false sinon
     */
    public abstract boolean prepareFloor(int floorNumber, Team team, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository repository);

    /**
     * Retourne les dialogues d'introduction d'un tage.
     * @param floorNumber Numro de l'tage
     * @return Liste des textes  afficher
     */
    public abstract java.util.List<String> getFloorIntroDialogues(int floorNumber);

    /**
     * Vrifie si les conditions de compltion de l'expdition sont remplies (ex: Boss vaincu).
     *
     * @param floorNumber Numro de l'tage actuel
     * @return true si l'expdition doit prendre fin avec succs
     */
    public abstract boolean isExpeditionComplete(int floorNumber);

    /**
     * Choisit un monstre alatoire selon la table de spawn de ce donjon pour cet tage.
     */
    public abstract fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character getRandomMonster(int floorNumber, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.random.IRandomProvider random, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository repository);
}