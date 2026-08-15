package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.ai;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.MonsterAI;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

public class AmbushTactics implements IMonsterTactics {
    private static final long serialVersionUID = 1L;
    @Override
    public int[] determineTargetStep(MonsterAI ai, Dungeon dungeon, Character monster, int groupSize, int x, int y, Team team) {
        int teamX = team.getX();
        int teamY = team.getY();
        int distance = Math.abs(x - teamX) + Math.abs(y - teamY);
        boolean hasSight = ai.hasLineOfSight(dungeon, x, y, teamX, teamY);

        if (distance <= 2 && hasSight) {
            return ai.getNextStepBFS(dungeon, x, y, teamX, teamY);
        } else {
            return new int[]{x, y}; // Reste en embuscade
        }
    }
}
