package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.ICombatView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.state.GameState;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.combat.BattleEngine.BattleState;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class BattleController implements GameState {
    private Team team;
    private List<Character> monsters;
    private ICombatView menu;
    
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
        engine.setState(BattleState.ROUND_START);
    }
    
    @Override
    public void update(float deltaTime) {
        BattleState state = engine.getState();
        if (state == BattleState.ROUND_START) {
            engine.initRound(team, monsters);
            
            currentTurnIndex = 0;
            engine.setState(BattleState.NEXT_COMBATANT);
        } else if (state == BattleState.NEXT_COMBATANT) {
            if (engine.checkEndCondition()) {
                handleBattleEnd();
                return;
            }

            if (currentTurnIndex >= engine.getTurnOrderSize()) {
                engine.setState(BattleState.ROUND_START);
                return;
            }

            currentCombatant = engine.getCombatant(currentTurnIndex);
            currentTurnIndex++;

            if (currentCombatant.getHealthPoint() <= 0) {
                return;
            }

            if (team.getMembers().contains(currentCombatant)) {
                engine.setState(BattleState.PLAYER_ACTION_CHOICE);
                promptPlayerAction();
            } else {
                engine.setState(BattleState.ENEMY_TURN);
                executeEnemyTurn();
            }
        }
    }
    
    private void handleBattleEnd() {
        if (engine.isVictory()) {
            int totalXp = 0;
            int totalGold = 0;
            java.util.List<String> loots = new java.util.ArrayList<>();
            
            for (Character m : monsters) {
                if (m instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy.Monster) {
                    fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy.Monster monster = (fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy.Monster) m;
                    totalXp += monster.getXp();
                    totalGold += monster.getGoldYield();
                    
                    for (String itemId : monster.rollLoot()) {
                        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item item = fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.ItemFactory.createItem(itemId);
                        if (team.addItem(item)) {
                            loots.add(item.getName());
                        } else {
                            loots.add(item.getName() + " (Jete, inventaire plein)");
                        }
                    }
                }
            }
            
            team.setGold(team.getGold() + totalGold);
            for (Character p : team.getMembers()) {
                p.gainXp(totalXp);
            }
            
            menu.displayVictory(totalXp, totalGold, loots);
        } else {
            menu.displayDefeat();
        }
    }

    private void promptPlayerAction() {
        menu.displayTurn(currentCombatant.getName());
        menu.showActionMenu(currentCombatant, Arrays.asList("Attaque", "Compétence", "Objet", "Fuite"));
    }

    private void executeEnemyTurn() {
        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.combat.CombatResult result = engine.executeAIAttack(currentCombatant, team);
        menu.displayMessage(result.getMessage());
        if (result.getDamageMessage() != null) {
            menu.displayMessage(result.getDamageMessage());
        }
        
        engine.setState(BattleState.NEXT_COMBATANT);
    }

    @Override
    public void onInput(String action) {
        if ("ENTER".equals(action)) {
            if (engine.getState() == BattleState.END) {
                menu.setMenuRequest(null, null);
                if (engine.isVictory()) {
                    if (onVictory != null) onVictory.run();
                } else {
                    if (onDefeat != null) onDefeat.run();
                }
                return;
            }
            int selection = menu.getMenuSelection();
            menu.setMenuRequest(null, null);
            onActionSelected(selection);
        }
    }
    
    public void onActionSelected(int index) {
        engine.setState(BattleState.EXECUTING_ACTION);
        boolean isUnwinnable = false;
        for (Character m : monsters) {
            if ("Patrouille Orque".equals(m.getName())) isUnwinnable = true;
        }
        
        if (isUnwinnable && index != 3) {
            menu.displayMessage("Impossible ! Ils sont trop nombreux, fuyez !");
            engine.setState(BattleState.PLAYER_ACTION_CHOICE);
            promptPlayerAction();
            return;
        }
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
                
                String sfxName = "attack_fist";
                String vfxType = "SCRATCH";
                fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.Equipment weapon = currentCombatant.getEquipments().get(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot.WEAPON);
                if (weapon != null) {
                    vfxType = "SLASH";
                    if (weapon.getCategory() == fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentCategory.HEAVY_WEAPON) sfxName = "attack_heavy";
                    else if (weapon.getCategory() == fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentCategory.LIGHT_WEAPON) sfxName = "attack_light";
                    else if (weapon.getCategory() == fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentCategory.RANGE_WEAPON) sfxName = "attack_range";
                }
                
                menu.playHitAnimation(target, result.getDamage(), vfxType, "audio/sfx/" + sfxName + ".wav");
            }
            engine.setState(BattleState.NEXT_COMBATANT);
        } else if (index == 1) {
            menu.displayMessage("Compétence non implémentée.");
            engine.setState(BattleState.NEXT_COMBATANT);
        } else if (index == 2) {
            menu.displayMessage("Objet (Mock partiel).");
            engine.setState(BattleState.NEXT_COMBATANT);
        } else if (index == 3) {
            menu.displayMessage("Vous fuyez lâchement !");
            engine.setState(BattleState.END);
            if (onFlee != null) onFlee.run(); // Fleeing
        } else {
            engine.setState(BattleState.NEXT_COMBATANT);
        }
    }

    @Override
    public void exit() {}
}


