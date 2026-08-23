package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.defensiveEquipment.ArchmageRobe;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.defensiveEquipment.BarbarianLoincloth;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.offensiveEquipment.DurandilAxe;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.usable.potion.Potion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Générateur procédural du Donjon avec algorithme Salles & Couloirs.
 */
public class DungeonGenerator {
    private final Random random = new Random();
    private final List<int[]> currentRoomCells = new ArrayList<>();

    public static class Room {
        public int x, y, width, height;

        public Room(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public int getCenterX() {
            return x + width / 2;
        }

        public int getCenterY() {
            return y + height / 2;
        }

        public boolean intersects(Room other) {
            return this.x <= other.x + other.width && this.x + this.width >= other.x &&
                   this.y <= other.y + other.height && this.y + this.height >= other.y;
        }
    }

    public void generateHybridDungeon(Dungeon dungeon) {
        int width = dungeon.getWidth();
        int height = dungeon.getHeight();
        Cell[][] grid = dungeon.getGrid();
        currentRoomCells.clear();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y].setWall(true);
            }
        }

        List<Room> rooms = new ArrayList<>();
        int targetRooms = random.nextInt(5) + 8; // 8 à 12 salles

        for (int i = 0; i < targetRooms * 3; i++) {
            if (rooms.size() >= targetRooms) break;

            int rw = random.nextInt(6) + 5; // 5 à 10
            int rh = random.nextInt(6) + 5; // 5 à 10
            
            if (width - rw - 2 <= 0 || height - rh - 2 <= 0) continue;
            
            int rx = random.nextInt(width - rw - 2) + 1;
            int ry = random.nextInt(height - rh - 2) + 1;

            Room newRoom = new Room(rx, ry, rw, rh);
            boolean failed = false;
            for (Room otherRoom : rooms) {
                if (newRoom.intersects(otherRoom)) {
                    failed = true;
                    break;
                }
            }

            if (!failed) {
                carveRoom(grid, newRoom, rooms.size() + 1);
                rooms.add(newRoom);
            }
        }

        for (int i = 1; i < rooms.size(); i++) {
            Room prev = rooms.get(i - 1);
            Room curr = rooms.get(i);
            carveCorridor(grid, prev.getCenterX(), prev.getCenterY(), curr.getCenterX(), curr.getCenterY());
        }
    }

    private void carveRoom(Cell[][] grid, Room room, int roomId) {
        for (int x = room.x; x < room.x + room.width; x++) {
            for (int y = room.y; y < room.y + room.height; y++) {
                grid[x][y].setWall(false);
                grid[x][y].setVisited(true);
                grid[x][y].setRoomId(roomId);
                currentRoomCells.add(new int[]{x, y});
            }
        }
    }

    private void carveCorridor(Cell[][] grid, int x1, int y1, int x2, int y2) {
        if (random.nextBoolean()) {
            carveHorizontalCorridor(grid, x1, x2, y1);
            carveVerticalCorridor(grid, y1, y2, x2);
        } else {
            carveVerticalCorridor(grid, y1, y2, x1);
            carveHorizontalCorridor(grid, x1, x2, y2);
        }
    }

    private void carveHorizontalCorridor(Cell[][] grid, int x1, int x2, int y) {
        int startX = Math.min(x1, x2);
        int endX = Math.max(x1, x2);
        for (int x = startX; x <= endX; x++) {
            grid[x][y].setWall(false);
            grid[x][y].setVisited(true);
        }
    }

    private void carveVerticalCorridor(Cell[][] grid, int y1, int y2, int x) {
        int startY = Math.min(y1, y2);
        int endY = Math.max(y1, y2);
        for (int y = startY; y <= endY; y++) {
            grid[x][y].setWall(false);
            grid[x][y].setVisited(true);
        }
    }

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

            if ((x != startX || y != startY) && grid[x][y].isWalkable()) {
                // Check if already a roaming monster here
                boolean taken = false;
                for (RoamingMonsterGroup mg : dungeon.getRoamingMonsters()) {
                    if ((int)mg.getX() == x && (int)mg.getZ() == y) {
                        taken = true;
                        break;
                    }
                }
                if (!taken) {
                    List<Character> enemyGroup = new ArrayList<>();
                    int groupSize = random.nextInt(3) + 1;

                    for (int j = 0; j < groupSize; j++) {
                        enemyGroup.add(getRandomMonster());
                    }

                    dungeon.getRoamingMonsters().add(new RoamingMonsterGroup(x, y, enemyGroup, false));
                    placed++;
                }
            }
        }
    }

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

            if ((x != 0 || y != 0) && grid[x][y].isWalkable() && !grid[x][y].hasItem()) {
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

                if ((x != 0 || y != 0) && grid[x][y].isWalkable() && !grid[x][y].hasItem() && !grid[x][y].hasStairs()) {
                    grid[x][y].setStairs(true);
                    placed++;
                }
            }
        } else {
            while (placed < count && attempts < 1000) {
                attempts++;
                int x = random.nextInt(width);
                int y = random.nextInt(height);

                if ((x != 0 || y != 0) && grid[x][y].isWalkable() && !grid[x][y].hasItem() && !grid[x][y].hasStairs()) {
                    grid[x][y].setStairs(true);
                    placed++;
                }
            }
        }
    }

    private Character getRandomMonster() {
        int roll = random.nextInt(3);
        return switch (roll) {
            case 0 -> fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.DataManager.createMonster("orc");
            case 1 -> fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.DataManager.createMonster("skeleton");
            default -> fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.DataManager.createMonster("goblin");
        };
    }
}
