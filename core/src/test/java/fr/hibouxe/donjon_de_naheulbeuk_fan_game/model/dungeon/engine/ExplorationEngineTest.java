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
        team.setX(1);
        team.setY(1);
        Dungeon maze = new TutorialDungeon();
        
        ExplorationEngine engine = new ExplorationEngine(maze, team);
        
        MoveResult result = engine.processPlayerMove(0, 1); // move Y+1
        
        assertNotNull(result, "MoveResult should not be null");
    }
}