package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy.Goblin;

import java.util.ArrayList;
import java.util.List;

public class TutorialDungeon extends Dungeon {

    public TutorialDungeon() {
        super(3, 1);
    }

    @Override
    public void generate() {
        // 1. Initialiser les cellules d'abord et les rendre marchables (sol au lieu d'un mur massif)
        for (int x = 0; x < 3; x++) {
            this.getGrid()[x][0] = new Cell(x, 0);
            this.getGrid()[x][0].setWall(false);
        }

        // 2. Casser les murs séparateurs (Est/Ouest) pour faire un couloir continu
        for (int x = 1; x < 3; x++) {
            this.getGrid()[x - 1][0].removeWallBetween(this.getGrid()[x][0]);
        }

        // 3. Ajouter le gobelin
        List<Character> tutorialMonsters = new ArrayList<>();
        tutorialMonsters.add(new Goblin());
        this.getGrid()[1][0].setMonsters(tutorialMonsters);

        // 4. Ajouter l'escalier/la sortie
        this.getGrid()[2][0].setStairs(true);
    }
}
