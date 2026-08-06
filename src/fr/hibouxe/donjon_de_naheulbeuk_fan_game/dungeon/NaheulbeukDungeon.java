package fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon;

public class NaheulbeukDungeon extends Dungeon {

    public NaheulbeukDungeon() {
        super(21, 21);
    }

    @Override
    public void generate() {
        generatePMDDungeon();
        generateMonsters(6, 0, 0);
        generateItems(4);
        generateStairs(1);
    }
}
