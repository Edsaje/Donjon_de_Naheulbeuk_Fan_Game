package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.ai;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.MonsterAI;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import java.io.Serializable;

public interface IMonsterTactics extends Serializable {
    int[] determineTargetStep(MonsterAI ai, Dungeon dungeon, Character monster, int groupSize, int x, int y, Team team);
}
