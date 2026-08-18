package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.ICombatView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.state.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class BattleController implements GameState {
    public enum BattleState { INIT, ROUND_START, NEXT_COMBATANT, PLAYER_ACTION_CHOICE, EXECUTING_ACTION, ENEMY_TURN, RESOLVE, END }

    private Team team;
    private List<Character> monsters;
    private ICombatView menu;
    
    private BattleState state = BattleState.INIT;
    private Runnable onVictory;
    private Runnable onDefeat;
    private Runnable onFlee;
    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.combat.ICombatEngine combatEngine;
    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.combat.BattleEngine engine;
    private int currentTurnIndex = 0;
    private Character currentCombatant;

    public BattleController(Team team, List<Character> monsters, ICombatView menu) {
        this.team = team;
        this.monsters = monsters;
        this.menu = menu;
        this.combatEngine = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.combat.StandardCombatEngine();
        this.engine = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.combat.BattleEngine(this.combatEngine);
    }
    
    public void setCallbacks(Runnable onVictory, Runnable onDefeat, Runnable onFlee) {
        this.onVictory = onVictory;
        this.onDefeat = onDefeat;
        this.onFlee = onFlee;
    }

    public boolean start() {
        return true;
    }

    @Override
    public void enter() {
        menu.displayBattleStatus(monsters, team);
        this.state = BattleState.ROUND_START;
    }
    
    @Override
    public void update(float deltaTime) {
        if (state == BattleState.ROUND_START) {
            engine.initRound(team, monsters);
            
            currentTurnIndex = 0;
            state = BattleState.NEXT_COMBATANT;
        } else if (state == BattleState.NEXT_COMBATANT) {
            if (checkEndCondition()) return;

            if (currentTurnIndex >= engine.getTurnOrderSize()) {
                state = BattleState.ROUND_START;
                return;
            }

            currentCombatant = engine.getTurnOrderCache()[currentTurnIndex];
            currentTurnIndex++;

            if (currentCombatant.getHealthPoint() <= 0) {
                return;
            }

            if (team.getMembers().contains(currentCombatant)) {
                state = BattleState.PLAYER_ACTION_CHOICE;
                promptPlayerAction();
            } else {
                state = BattleState.ENEMY_TURN;
                executeEnemyTurn();
            }
        }
    }
    
    private boolean checkEndCondition() {
        boolean allMonstersDead = true;
        for (Character m : monsters) {
            if (m.getHealthPoint() > 0) allMonstersDead = false;
        }
        if (allMonstersDead) {
            state = BattleState.END;
            menu.displayVictory();
            if (onVictory != null) onVictory.run();
            return true;
        }

        boolean allPlayersDead = true;
        for (Character p : team.getMembers()) {
            if (p.getHealthPoint() > 0) allPlayersDead = false;
        }
        if (allPlayersDead) {
            state = BattleState.END;
            if (onDefeat != null) onDefeat.run();
            return true;
        }
        return false;
    }

    private void promptPlayerAction() {
        menu.displayMessage("Tour de " + currentCombatant.getName() + " !");
        menu.showActionMenu(currentCombatant, Arrays.asList("Attaque", "Compétence", "Objet", "Fuite"));
    }

    private void executeEnemyTurn() {
        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.combat.CombatResult result = engine.executeAIAttack(currentCombatant, team);
        menu.displayMessage(result.getMessage());
        if (result.getDamageMessage() != null) {
            menu.displayMessage(result.getDamageMessage());
        }
        
        state = BattleState.NEXT_COMBATANT;
    }

    @Override
    public void onInput(String action) {
        if ("ENTER".equals(action)) {
            int selection = menu.getMenuSelection();
            menu.setMenuRequest(null, null);
            onActionSelected(selection);
        }
    }
    
    public void onActionSelected(int index) {
        state = BattleState.EXECUTING_ACTION;
        if (index == 0) {
            // Attaque
            Character target = null;
            for (Character m : monsters) {
                if (m.getHealthPoint() > 0) {
                    target = m;
                    break;
                }
            }
            if (target != null) {
                menu.displayMessage(currentCombatant.getName() + " attaque " + target.getName() + " !");
                fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.combat.CombatResult result = this.combatEngine.executeAttack(currentCombatant, target, false);
            }
            state = BattleState.NEXT_COMBATANT;
        } else if (index == 1) {
            menu.displayMessage("Compétence non implémentée.");
            state = BattleState.NEXT_COMBATANT;
        } else if (index == 2) {
            menu.displayMessage("Objet (Mock partiel).");
            state = BattleState.NEXT_COMBATANT;
        } else if (index == 3) {
            menu.displayMessage("Vous fuyez lâchement !");
            state = BattleState.END;
            if (onFlee != null) onFlee.run(); // Fleeing
        } else {
            state = BattleState.NEXT_COMBATANT;
        }
    }

    @Override
    public void exit() {}
}


