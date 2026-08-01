package fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon;

public class NaheulbeukDungeon extends Dungeon {

    public NaheulbeukDungeon() {
        super(10, 10);
    }

    @Override
    public void generate() {
        generateMaze(0, 0);
        generateRandomRooms(6, 2, 4);
        generateMonsters(5, 0, 0);
        generateItems(3);
        generateStairs(1);
    }
}
