package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.engine;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.TutorialDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExplorationEngineTest {

    @Test
    void testProcessPlayerMove() {
        Team team = new Team();
        team.setPlayerX(1.5f);
        team.setPlayerZ(1.5f);
        Dungeon maze = new TutorialDungeon();
        maze.prepareFloor(1, team, null); // initialize grid
        // Remove wall at 1,2 so player can move UP (Z decreases in 3D usually, but physics handles up/down)
        maze.getGrid()[1][2].setWall(false);
        
        ExplorationEngine engine = new ExplorationEngine(maze, team);
        
        // Simuler 1 seconde de deplacement vers le BAS (Z augmente vers 2)
        boolean hasEncounter = engine.updatePhysics(1.0f, false, true, false, false);
        
        // Le joueur s'est deplace, donc sa position doit avoir change
        assertTrue(team.getPlayerZ() != 1.5f || team.getPlayerX() != 1.5f, "Player should have moved");
    }
}