package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

public class NaheulbeukDungeon extends Dungeon {

    public NaheulbeukDungeon() {
        super(21, 21);
    }

    @Override
    public void generate() {
        generatePMDDungeon();
        // Monsters, items and stairs are injected by ExplorationController depending on the floor
    }
}
