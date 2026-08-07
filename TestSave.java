import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.*;

public class TestSave {
    public static void main(String[] args) {
        Team team = new Team();
        team.getMembers().add(new Ranger());
        TutorialDungeon tutorialMaze = new TutorialDungeon();
        tutorialMaze.generate();
        boolean success = SaveManager.saveQuickSave(1, team, tutorialMaze, 1);
        System.out.println("Save success: " + success);
    }
}
