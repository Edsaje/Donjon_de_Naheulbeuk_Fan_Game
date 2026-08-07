package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.defensiveEquipment.ArchmageRobe;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.defensiveEquipment.BarbarianLoincloth;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.offensiveEquipment.DurandilAxe;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.usable.potion.Potion;

import java.util.*;

/**
 * Générateur procédural du Donjon.
 * S'occupe du creusement par Backtracking DFS, de la création de salles et du peuplement (Monstres, Coffres, Escaliers).
 *
 * @author Hibouxe
 * @version 1.0
 */
public class DungeonGenerator {
    private Random random = new Random();

    private List<int[]> currentRoomCells = new ArrayList<>();

    /**
     * Génère un donjon procédural unique et aléatoire style Pokémon Donjon Mystère.
     * Nombre de salles aléatoire (entre 5 et 8 par étage), tailles et emplacements variables,
     * et réseau de couloirs généré dynamiquement.
     *
     * @param dungeon Le donjon à générer
     */
    public void generatePMDDungeon(Dungeon dungeon) {
        int width = dungeon.getWidth();
        int height = dungeon.getHeight();
        Cell[][] grid = dungeon.getGrid();
        currentRoomCells.clear();

        // 1. Réinitialiser la grille : Tous les blocs sont de la roche/mur massif (isWall = true)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y].setWall(true);
            }
        }

        int sectorCols = 3;
        int sectorRows = 3;
        int sectorW = width / sectorCols;
        int sectorH = height / sectorRows;

        boolean[] activeSectors = new boolean[9];
        // Le secteur de départ (0,0) et le secteur d'arrivée (2,2) sont toujours actifs
        activeSectors[0] = true;
        activeSectors[8] = true;

        // Tirer au sort entre 5 et 8 salles actives
        int targetRooms = random.nextInt(4) + 5; // 5, 6, 7 ou 8 salles
        int currentActive = 2;

        while (currentActive < targetRooms) {
            int candidate = random.nextInt(9);
            if (!activeSectors[candidate]) {
                activeSectors[candidate] = true;
                currentActive++;
            }
        }

        int[][] roomCenters = new int[9][2];

        // 2. Creuser les salles dans les secteurs actifs avec tailles/positions aléatoires
        for (int sy = 0; sy < sectorRows; sy++) {
            for (int sx = 0; sx < sectorCols; sx++) {
                int idx = sy * sectorCols + sx;
                int secMinX = sx * sectorW + 1;
                int secMinY = sy * sectorH + 1;

                if (activeSectors[idx]) {
                    int maxRW = Math.max(3, sectorW - 2);
                    int maxRH = Math.max(3, sectorH - 2);

                    int rW = random.nextInt(Math.max(1, maxRW - 3 + 1)) + 3; // 3 à 5
                    int rH = random.nextInt(Math.max(1, maxRH - 3 + 1)) + 3; // 3 à 5

                    int startX = secMinX + random.nextInt(Math.max(1, sectorW - rW - 1));
                    int startY = secMinY + random.nextInt(Math.max(1, sectorH - rH - 1));

                    startX = Math.min(startX, (sx + 1) * sectorW - rW - 1);
                    startY = Math.min(startY, (sy + 1) * sectorH - rH - 1);
                    startX = Math.max(secMinX, startX);
                    startY = Math.max(secMinY, startY);

                    // Creuser les cases de sol de la salle et les enregistrer dans currentRoomCells
                    for (int x = startX; x < startX + rW; x++) {
                        for (int y = startY; y < startY + rH; y++) {
                            if (x > 0 && x < width - 1 && y > 0 && y < height - 1) {
                                grid[x][y].setWall(false);
                                grid[x][y].setVisited(true);
                                currentRoomCells.add(new int[]{x, y});
                            }
                        }
                    }

                    int cx = startX + rW / 2;
                    int cy = startY + rH / 2;
                    roomCenters[idx] = new int[]{cx, cy};
                } else {
                    // Secteur inactif : point de passage au centre du secteur pour les couloirs
                    roomCenters[idx] = new int[]{secMinX + sectorW / 2, secMinY + sectorH / 2};
                }
            }
        }

        // 3. Reliage procédural des secteurs par des couloirs de 1 case de large
        for (int sy = 0; sy < 3; sy++) {
            for (int sx = 0; sx < 3; sx++) {
                int idx = sy * 3 + sx;

                // Relier avec le voisin de droite si au moins un des deux est actif
                if (sx < 2) {
                    int rightIdx = sy * 3 + (sx + 1);
                    if (activeSectors[idx] || activeSectors[rightIdx] || random.nextBoolean()) {
                        carveCorridor(grid, roomCenters[idx], roomCenters[rightIdx]);
                    }
                }

                // Relier avec le voisin du bas si au moins un des deux est actif
                if (sy < 2) {
                    int downIdx = (sy + 1) * 3 + sx;
                    if (activeSectors[idx] || activeSectors[downIdx] || random.nextBoolean()) {
                        carveCorridor(grid, roomCenters[idx], roomCenters[downIdx]);
                    }
                }
            }
        }
    }

    private void carveCorridor(Cell[][] grid, int[] c1, int[] c2) {
        int x1 = c1[0], y1 = c1[1];
        int x2 = c2[0], y2 = c2[1];

        // Mouvement horizontal
        int startX = Math.min(x1, x2);
        int endX = Math.max(x1, x2);
        for (int x = startX; x <= endX; x++) {
            grid[x][y1].setWall(false);
            grid[x][y1].setVisited(true);
        }

        // Mouvement vertical
        int startY = Math.min(y1, y2);
        int endY = Math.max(y1, y2);
        for (int y = startY; y <= endY; y++) {
            grid[x2][y].setWall(false);
            grid[x2][y].setVisited(true);
        }
    }

    /**
     * Place aléatoirement un nombre d'ennemis sur les cases de sol du labyrinthe.
     *
     * @param dungeon Donjon
     * @param count   Nombre de monstres
     * @param startX  X du joueur à ne pas polluer
     * @param startY  Y du joueur à ne pas polluer
     */
    public void generateMonsters(Dungeon dungeon, int count, int startX, int startY) {
        int width = dungeon.getWidth();
        int height = dungeon.getHeight();
        Cell[][] grid = dungeon.getGrid();

        int placed = 0;
        int attempts = 0;
        while (placed < count && attempts < 1000) {
            attempts++;
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if ((x != startX || y != startY) && grid[x][y].isWalkable() && !grid[x][y].hasMonster()) {
                List<Character> enemyGroup = new ArrayList<>();
                int groupSize = random.nextInt(3) + 1;

                for (int j = 0; j < groupSize; j++) {
                    enemyGroup.add(getRandomMonster());
                }

                grid[x][y].setMonsters(enemyGroup);
                placed++;
            }
        }
    }

    /**
     * Place des coffres au trésor garantis sur les cases de sol du donjon.
     *
     * @param dungeon Donjon
     * @param count   Nombre de coffres
     */
    public void generateItems(Dungeon dungeon, int count) {
        int width = dungeon.getWidth();
        int height = dungeon.getHeight();
        Cell[][] grid = dungeon.getGrid();

        int placed = 0;
        int attempts = 0;
        while (placed < count && attempts < 1000) {
            attempts++;
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if ((x != 0 || y != 0) && grid[x][y].isWalkable() && !grid[x][y].hasMonster() && !grid[x][y].hasItem()) {
                int roll = random.nextInt(4);
                Item loot = switch (roll) {
                    case 1 -> new DurandilAxe();
                    case 2 -> new BarbarianLoincloth();
                    case 3 -> new ArchmageRobe();
                    default -> new Potion("Potion de soin", "une potion de vie simple. Rend +10 PV. Usage Unique.", 10);
                };
                grid[x][y].setItem(loot);
                placed++;
            }
        }
    }

    /**
     * Place les escaliers garantis vers l'étage suivant exclusivement à l'intérieur d'une salle.
     *
     * @param dungeon Donjon
     * @param count   Nombre d'escaliers
     */
    public void generateStairs(Dungeon dungeon, int count) {
        int width = dungeon.getWidth();
        int height = dungeon.getHeight();
        Cell[][] grid = dungeon.getGrid();

        int placed = 0;
        int attempts = 0;

        if (!currentRoomCells.isEmpty()) {
            while (placed < count && attempts < 1000) {
                attempts++;
                int[] pos = currentRoomCells.get(random.nextInt(currentRoomCells.size()));
                int x = pos[0];
                int y = pos[1];

                if ((x != 0 || y != 0) && grid[x][y].isWalkable() && !grid[x][y].hasMonster() && !grid[x][y].hasItem() && !grid[x][y].hasStairs()) {
                    grid[x][y].setStairs(true);
                    placed++;
                }
            }
        } else {
            while (placed < count && attempts < 1000) {
                attempts++;
                int x = random.nextInt(width);
                int y = random.nextInt(height);

                if ((x != 0 || y != 0) && grid[x][y].isWalkable() && !grid[x][y].hasMonster() && !grid[x][y].hasItem() && !grid[x][y].hasStairs()) {
                    grid[x][y].setStairs(true);
                    placed++;
                }
            }
        }
    }

    private Character getRandomMonster() {
        int roll = random.nextInt(6);
        return switch (roll) {
            case 0 -> new Orc();
            case 1 -> new Skeleton();
            case 2 -> new Spider();
            case 3 -> new Troll();
            case 4 -> new Undead();
            default -> new Goblin();
        };
    }

    private List<Cell> getUnvisitedNeighbors(Dungeon dungeon, Cell current) {
        int x = current.getX();
        int y = current.getY();
        int width = dungeon.getWidth();
        int height = dungeon.getHeight();
        Cell[][] grid = dungeon.getGrid();
        List<Cell> neighbors = new ArrayList<>();

        if (y - 1 >= 0 && !grid[x][y - 1].isVisited()) {
            neighbors.add(grid[x][y - 1]);
        }
        if (y + 1 < height && !grid[x][y + 1].isVisited()) {
            neighbors.add(grid[x][y + 1]);
        }
        if (x - 1 >= 0 && !grid[x - 1][y].isVisited()) {
            neighbors.add(grid[x - 1][y]);
        }
        if (x + 1 < width && !grid[x + 1][y].isVisited()) {
            neighbors.add(grid[x + 1][y]);
        }
        return neighbors;
    }
}
