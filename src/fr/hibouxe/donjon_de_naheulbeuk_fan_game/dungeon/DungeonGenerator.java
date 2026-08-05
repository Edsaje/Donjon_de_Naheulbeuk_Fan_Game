package fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.enemy.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.defensiveEquipment.ArchmageRobe;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.defensiveEquipment.BarbarianLoincloth;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.offensiveEquipment.DurandilAxe;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.usable.potion.Potion;

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

    /**
     * Génère un labyrinthe parfait à partir d'une position de départ à l'aide de l'algorithme DFS (Backtracking).
     *
     * @param dungeon Donjon à générer
     * @param startX  Coordonnée X de départ
     * @param startY  Coordonnée Y de départ
     */
    public void generateMaze(Dungeon dungeon, int startX, int startY) {
        Cell[][] grid = dungeon.getGrid();
        Cell startCell = grid[startX][startY];
        startCell.setVisited(true);
        Deque<Cell> stack = new ArrayDeque<>();
        stack.push(startCell);

        while (!stack.isEmpty()) {
            Cell current = stack.peek();
            List<Cell> neighbors = getUnvisitedNeighbors(dungeon, current);

            if (!neighbors.isEmpty()) {
                Cell chosen = neighbors.get(random.nextInt(neighbors.size()));
                current.removeWallBetween(chosen);
                chosen.setVisited(true);
                stack.push(chosen);
            } else {
                stack.pop();
            }
        }
    }

    /**
     * Creuse une salle rectangulaire dans le labyrinthe.
     *
     * @param dungeon    Donjon
     * @param startX     X du coin haut-gauche
     * @param startY     Y du coin haut-gauche
     * @param roomWidth  Largeur de la salle
     * @param roomHeight Hauteur de la salle
     */
    public void createRoom(Dungeon dungeon, int startX, int startY, int roomWidth, int roomHeight) {
        int width = dungeon.getWidth();
        int height = dungeon.getHeight();
        Cell[][] grid = dungeon.getGrid();

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
     * Génère un nombre défini de salles aléatoires dans le donjon.
     *
     * @param dungeon       Donjon
     * @param numberOfRooms Nombre de salles
     * @param minSize       Taille minimale
     * @param maxSize       Taille maximale
     */
    public void generateRandomRooms(Dungeon dungeon, int numberOfRooms, int minSize, int maxSize) {
        int width = dungeon.getWidth();
        int height = dungeon.getHeight();

        for (int i = 0; i < numberOfRooms; i++) {
            int roomWidth = random.nextInt(maxSize - minSize + 1) + minSize;
            int roomHeight = random.nextInt(maxSize - minSize + 1) + minSize;

            int startX = random.nextInt(width - roomWidth);
            int startY = random.nextInt(height - roomHeight);

            createRoom(dungeon, startX, startY, roomWidth, roomHeight);
        }
    }

    /**
     * Place aléatoirement un nombre d'ennemis sur les cases du labyrinthe.
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

        for (int i = 0; i < count; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if (x != startX || y != startY) {
                List<Character> enemyGroup = new ArrayList<>();
                int groupSize = random.nextInt(3) + 1;

                for (int j = 0; j < groupSize; j++) {
                    enemyGroup.add(getRandomMonster());
                }

                grid[x][y].setMonsters(enemyGroup);
            }
        }
    }

    /**
     * Place des coffres au trésor dans le donjon.
     *
     * @param dungeon Donjon
     * @param count   Nombre de coffres
     */
    public void generateItems(Dungeon dungeon, int count) {
        int width = dungeon.getWidth();
        int height = dungeon.getHeight();
        Cell[][] grid = dungeon.getGrid();

        for (int i = 0; i < count; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if (x != 0 || y != 0) {
                int roll = random.nextInt(4);
                Item loot = switch (roll) {
                    case 1 -> new DurandilAxe();
                    case 2 -> new BarbarianLoincloth();
                    case 3 -> new ArchmageRobe();
                    default -> new Potion("Potion de soin", "une potion de vie simple. Rend +10 PV. Usage Unique.", 10);
                };
                grid[x][y].setItem(loot);
            }
        }
    }

    /**
     * Place les escaliers vers l'étage suivant.
     *
     * @param dungeon Donjon
     * @param count   Nombre d'escaliers
     */
    public void generateStairs(Dungeon dungeon, int count) {
        int width = dungeon.getWidth();
        int height = dungeon.getHeight();
        Cell[][] grid = dungeon.getGrid();

        for (int i = 0; i < count; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if (x != 0 || y != 0) {
                grid[x][y].setStairs(true);
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
