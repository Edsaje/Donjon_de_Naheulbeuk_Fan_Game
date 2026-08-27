package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.combat;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Ranger;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy.Monster;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.combat.StandardCombatEngine;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class BattleEngineTest {

    @Test
    void testEndConditionMonstersDead() {
        BattleEngine engine = new BattleEngine(new StandardCombatEngine());
        Team team = new Team();
        Character ranger = new Ranger();
        team.getMembers().add(ranger);
        
        Character goblin = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy.Monster(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.infrastructure.JsonMonsterLoader("../assets/data/monsters.json").getMonsterData("goblin"));
        goblin.setHealthPoint(0); // Dead goblin
        
        engine.initRound(team, Arrays.asList(goblin));
        
        assertTrue(engine.checkEndCondition(), "Battle should end when all monsters are dead");
        assertEquals(BattleEngine.BattleState.END, engine.getState(), "State should be END");
    }
}
