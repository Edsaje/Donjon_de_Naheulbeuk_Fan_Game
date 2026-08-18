package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.combat;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import java.util.List;

public class BattleEngine {
    public enum BattleState { INIT, ROUND_START, NEXT_COMBATANT, PLAYER_ACTION_CHOICE, EXECUTING_ACTION, ENEMY_TURN, RESOLVE, END }
    
    private Character[] turnOrderCache = new Character[20];
    private int turnOrderSize = 0;
    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.combat.ICombatEngine combatEngine;
    
    private BattleState state = BattleState.INIT;
    private Team team;
    private List<Character> monsters;

    public BattleEngine(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.combat.ICombatEngine combatEngine) {
        this.combatEngine = combatEngine;
    }

    public void initRound(Team team, List<Character> monsters) {
        this.team = team;
        this.monsters = monsters;
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

    public Iterable<Character> getTurnOrderCache() {
        return java.util.Arrays.asList(java.util.Arrays.copyOf(turnOrderCache, turnOrderSize));
    }
    
    public Character getCombatant(int index) {
        if (index >= 0 && index < turnOrderSize) {
            return turnOrderCache[index];
        }
        return null;
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
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.combat.CombatResult sysResult = this.combatEngine.executeAttack(currentCombatant, target, true);
            return new CombatResult(
                currentCombatant.getName() + " attaque ! (IA)",
                target.getName() + " perd " + sysResult.getDamage() + " PV !"
            );
        }
        return new CombatResult(currentCombatant.getName() + " attaque ! (IA)", null);
    }
    
    public BattleState getState() {
        return state;
    }

    public void setState(BattleState state) {
        this.state = state;
    }

    public boolean checkEndCondition() {
        if (team == null || monsters == null) return false;
        
        boolean allMonstersDead = true;
        for (Character m : monsters) {
            if (m.getHealthPoint() > 0) allMonstersDead = false;
        }
        if (allMonstersDead) {
            state = BattleState.END;
            return true;
        }

        boolean allPlayersDead = true;
        for (Character p : team.getMembers()) {
            if (p.getHealthPoint() > 0) allPlayersDead = false;
        }
        if (allPlayersDead) {
            state = BattleState.END;
            return true;
        }
        return false;
    }

    public boolean isVictory() {
        if (monsters == null) return false;
        for (Character m : monsters) {
            if (m.getHealthPoint() > 0) return false;
        }
        return true;
    }
}

