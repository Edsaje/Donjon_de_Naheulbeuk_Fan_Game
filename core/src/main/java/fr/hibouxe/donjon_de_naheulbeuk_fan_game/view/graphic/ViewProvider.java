package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IExplorationView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.ICombatView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IMenuView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.renderers.HUDRenderer;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.renderers.BattleArenaRenderer;
import java.util.List;

public class ViewProvider {
    private HD2DGameApp app;
    public ViewProvider(HD2DGameApp app) {
        this.app = app;
    }

    public IExplorationView getExplorationView() {
        return new IExplorationView() {
            @Override
            public void displayTransitionScreen(int floorNumber) {
                app.setState(HD2DGameApp.GameState.TRANSITION);
                app.setTransitionFloor(floorNumber);
            }
            @Override
            public void display(Dungeon maze, Team team, int currentFloor) {
                app.setContext(maze, team, currentFloor);
            }
            @Override
            public void displayDungeon(Dungeon maze, Team team, int currentFloor) {
                app.setContext(maze, team, currentFloor);
            }
            @Override
            public String askPlayerMovement() { return ""; }
            @Override
            public boolean askPickupItem(Item item) { return true; }
            @Override
            public void displaySaveSuccess(int slot) { app.displaySaveSuccess(slot); }
            @Override
            public void displaySaveError() { app.displaySaveError(); }
        };
    }

    public ICombatView getCombatView() {
        return new ICombatView() {
            @Override
            public void displayBattleStatus(List<Character> monsters, Team team) {
                app.setupBattle(team, monsters);
                app.displayTransitionScreen(0); app.setState(HD2DGameApp.GameState.BATTLE);
            }
            @Override
            public int askBattleAction(Character attacker) { return 1; }
            @Override
            public void showActionMenu(Character combatant, List<String> actions) {
                app.setMenuRequest("Action: " + combatant.getName(), actions.toArray(new String[0]));
            }
            @Override
            public Skill askSkill(Character attacker, List<Character> monsters) { return null; }
            @Override
            public Character askMonsterTarget(List<Character> monsters) { return monsters.isEmpty() ? null : monsters.get(0); }

            public Character askItemTarget(Team team) { return team.getMembers().isEmpty() ? null : team.getMembers().get(0); }
            @Override
            public void displayTurn(String characterName) {}
            @Override
            public void displayVictory(int xp, int gold, java.util.List<String> loots) {
                app.displayVictoryUI(xp, gold, loots);
            }
            @Override
            public void displayDefeat() { app.displayMessage("Défaite !"); }
            @Override
            public void displayMessage(String message) { app.displayMessage(message); }
            @Override
            public void clearMessages() { app.clearMessages(); }
            @Override
            public void displayDialogue(String message) { app.displayMessage(message); }

            @Override
            public void displayInventory(Team team) {
                String[] options = new String[team.getInventory().size() + 1];
                for(int i=0; i<team.getInventory().size(); i++) {
                    options[i] = team.getInventory().get(i).getName();
                }
                options[options.length-1] = "Retour";
                app.setMenuRequest("INVENTAIRE", options);
            }
            @Override
            public int askItemIndex() { return -1; }

            public int askPlayerInt() { return 0; }
            public String askPlayerString() { return ""; }
            
            @Override
            public int getMenuSelection() { return app.getMenuSelection(); }
            
            public void resetMenuSelection() { app.resetMenuSelection(); }
            @Override
            public void setMenuRequest(String title, String[] options) { app.setMenuRequest(title, options); }
            @Override
            public void playHitAnimation(Character target, int damage, String vfxType, String sfxPath) {
                if (app.getBattleRenderer() != null) {
                    app.getBattleRenderer().playHitAnimation(target, damage, vfxType);
                }
                if (app.getAudioManager() != null && sfxPath != null && !sfxPath.isEmpty()) {
                    app.getAudioManager().playSound(sfxPath);
                }
            }
        };
    }

    public IMenuView getMenuView() {
        return new IMenuView() {
            @Override
            public void displayTitleScreen() {}
            @Override
            public int askMainMenuChoice() { return 1; }
            @Override
            public int askHubChoice() { return 1; }
            @Override
            public boolean askLoadQuickSavePrompt() { return false; }
            @Override
            public boolean askLoadQuickSavePrompt(int slot, String summary) { return false; }
            @Override
            public boolean askConfirmAbandonQuickSave() { return false; }
            @Override
            public int askSlotChoice(String actionTitle, String[] slotSummaries) { return 1; }
            @Override
            public int askSlotManagementAction() { return 0; }
            @Override
            public int askTargetCopySlot(int sourceSlot, String[] slotSummaries) { return 0; }
            @Override
            public void displayInventory(Team team) {
                String[] options = new String[team.getInventory().size() + 1];
                for(int i=0; i<team.getInventory().size(); i++) {
                    options[i] = team.getInventory().get(i).getName();
                }
                options[options.length-1] = "Retour";
                app.setMenuRequest("INVENTAIRE", options);
            }
            @Override
            public int askInventoryMenuChoice() { return 3; }
            @Override
            public void displayStatusScreen(Team team) {
                String[] options = new String[team.getMembers().size() + 1];
                for (int i = 0; i < team.getMembers().size(); i++) {
                    options[i] = team.getMembers().get(i).getName();
                }
                options[options.length - 1] = "Retour";
                app.setMenuRequest("STATISTIQUES", options);
            }
            @Override
            public EquipmentSlot askSlotToUnequip() { return null; }
            @Override
            public boolean askUseItem() { return false; }
            @Override
            public int askItemIndex() { return -1; }
            @Override
            public Character askItemTarget(Team team) { return null; }
            @Override
            public void displayMessage(String message) { app.displayMessage(message); }
            @Override
            public void clearMessages() { app.clearMessages(); }
            @Override
            public void displayDialogue(String message) { app.displayMessage(message); }
            @Override
            public int askPlayerInt() { return 0; }
            @Override
            public String askPlayerString() { return ""; }
            @Override
            public int getMenuSelection() { return app.getMenuSelection(); }
            @Override
            public void resetMenuSelection() { app.resetMenuSelection(); }
            @Override
            public void setMenuRequest(String title, String[] options) { app.setMenuRequest(title, options); }
            @Override
            public void displaySaveSuccess(int slot) { app.displayMessage("Sauvegardé !"); }
            @Override
            public void displaySaveError() { app.displayMessage("Erreur save"); }
        };
    }
}
