package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.combat;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import java.util.List;

public class BattleEngine {
    private Character[] turnOrderCache = new Character[20];
    private int turnOrderSize = 0;

    public void initRound(Team team, List<Character> monsters) {
        turnOrderSize = 0;
        for (Character c : team.getMembers()) {
            if (c.getHealthPoint() > 0 && turnOrderSize < 20) {
                turnOrderCache[turnOrderSize++] = c;
            }
        }
        for (Character c : monsters) {
            if (c.getHealthPoint() > 0 && turnOrderSize < 20) {
                turnOrderCache[turnOrderSize++] = c;
            }
        }
        
        // Tri d'initiative (Tri à bulles pour 0 allocation)
        for (int i = 0; i < turnOrderSize - 1; i++) {
            for (int j = 0; j < turnOrderSize - i - 1; j++) {
                if (turnOrderCache[j].getSpeed() < turnOrderCache[j + 1].getSpeed()) {
                    Character temp = turnOrderCache[j];
                    turnOrderCache[j] = turnOrderCache[j + 1];
                    turnOrderCache[j + 1] = temp;
                }
            }
        }
    }

    public Character[] getTurnOrderCache() {
        return turnOrderCache;
    }

    public int getTurnOrderSize() {
        return turnOrderSize;
    }

    public CombatResult executeAIAttack(Character currentCombatant, Team team) {
        Character target = null;
        for (Character p : team.getMembers()) {
            if (p.getHealthPoint() > 0) {
                target = p;
                break;
            }
        }
        if (target != null) {
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.combat.CombatResult sysResult = fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.combat.CombatSystem.executeAttack(currentCombatant, target, true);
            return new CombatResult(
                currentCombatant.getName() + " attaque ! (IA)",
                target.getName() + " perd " + sysResult.getDamage() + " PV !"
            );
        }
        return new CombatResult(currentCombatant.getName() + " attaque ! (IA)", null);
    }
}
