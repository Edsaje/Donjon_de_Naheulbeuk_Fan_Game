package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IMenuView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.state.GameState;

public class InventoryController implements GameState {
    private enum State { SELECT_ACTION, SELECT_ITEM, SELECT_TARGET, SELECT_SLOT_TO_UNEQUIP }
    private State currentState = State.SELECT_ACTION;
    
    private Team team;
    private IMenuView menu;
    private GameContext gameContext;
    private int actionChoice = -1; // 0=Use, 1=Unequip
    private Item selectedItem = null;
    private Character selectedTarget = null;
    private java.util.List<Item> currentItemList;

    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon maze;

    public InventoryController(Team team, IMenuView menu, GameContext gameContext, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon maze) {
        this.maze = maze;
        this.team = team;
        this.menu = menu;
        this.gameContext = gameContext;
    }

    @Override
    public void enter() {
        currentState = State.SELECT_ACTION;
        promptAction();
    }

    @Override
    public void update(float deltaTime) {}

    @Override
    public void onInput(String action) {
        if ("X".equals(action) || "ESCAPE".equals(action)) {
            gameContext.popState();
            return;
        }

        if ("ENTER".equals(action)) {
            int selection = menu.getMenuSelection();
            if (currentState == State.SELECT_ACTION) {
                if (selection == 0) {
                    actionChoice = 0;
                    currentState = State.SELECT_ITEM;
                    promptItem();
                } else if (selection == 1) {
                    actionChoice = 1;
                    currentState = State.SELECT_TARGET;
                    promptTarget();
                } else {
                    gameContext.popState();
                }
            } else if (currentState == State.SELECT_ITEM) {
                if (selection >= 0 && selection < currentItemList.size()) {
                    selectedItem = currentItemList.get(selection);
                    currentState = State.SELECT_TARGET;
                    promptTarget();
                } else {
                    currentState = State.SELECT_ACTION;
                    promptAction();
                }
            } else if (currentState == State.SELECT_TARGET) {
                if (selection >= 0 && selection < team.getMembers().size()) {
                    selectedTarget = team.getMembers().get(selection);
                    if (actionChoice == 0) {
                        useItem(selectedItem, selectedTarget);
                    } else if (actionChoice == 1) {
                        currentState = State.SELECT_SLOT_TO_UNEQUIP;
                        promptSlot();
                    }
                } else {
                    if (actionChoice == 0) {
                        currentState = State.SELECT_ITEM;
                        promptItem();
                    } else {
                        currentState = State.SELECT_ACTION;
                        promptAction();
                    }
                }
            } else if (currentState == State.SELECT_SLOT_TO_UNEQUIP) {
                fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot slot = null;
                switch (selection) {
                    case 0: slot = fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot.WEAPON; break;
                    case 1: slot = fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot.CHEST; break;
                    case 2: slot = fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot.JEWELRY; break;
                }
                if (slot != null) {
                    boolean success = selectedTarget.unequip(slot, team);
                    if (success) {
                        menu.displayDialogue("\n" + selectedTarget.getName() + " retire son ÃƒÂ©quipement !");
                    } else {
                        menu.displayDialogue("\nRien d'ÃƒÂ©quipÃƒÂ© ou sac plein.");
                    }
                }
                gameContext.popState();
            }
        }
    }

    private void promptAction() {
        menu.setMenuRequest("INVENTAIRE", new String[]{"Utiliser un objet", "DÃƒÂ©sÃƒÂ©quiper", "Retour"});
    }

    private void promptItem() {
        currentItemList = team.getInventory();
        if (currentItemList.isEmpty()) {
            menu.displayDialogue("Le sac est vide !");
            gameContext.popState();
            return;
        }
        String[] options = new String[currentItemList.size() + 1];
        for (int i = 0; i < currentItemList.size(); i++) {
            options[i] = currentItemList.get(i).getName();
        }
        options[currentItemList.size()] = "Retour";
        menu.setMenuRequest("CHOISIR UN OBJET", options);
    }

    private void promptTarget() {
        String[] options = new String[team.getMembers().size() + 1];
        for (int i = 0; i < team.getMembers().size(); i++) {
            options[i] = team.getMembers().get(i).getName();
        }
        options[team.getMembers().size()] = "Retour";
        menu.setMenuRequest("CIBLE", options);
    }
    
    private void promptSlot() {
        menu.setMenuRequest("EMPLACEMENT", new String[]{"Arme", "Armure (Torse)", "Bijoux", "Retour"});
    }

    private void useItem(Item item, Character target) {
        if (item instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.usable.potion.Potion && target.getHealthPoint() >= target.getMaxHealthPoint()) {
            menu.displayDialogue("\n" + target.getName() + " a dÃƒÂ©jÃƒÂ  tous ses PV !");
            gameContext.popState();
            return;
        }
        if (item.use(target)) {
            team.removeItem(item);
            menu.displayMessage("\n" + target.getName() + " utilise " + item.getName() + " !");
            
            // Check for tutorial specific event
            if (target.getClass().getSimpleName().equals("Elf")) {
                for (int x=0; x<maze.getWidth(); x++) {
                    for (int y=0; y<maze.getHeight(); y++) {
                        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.ICellEvent e = maze.getGrid()[x][y].getEvent();
                        if (e != null) e.onItemUsed(item, target, maze);
                    }
                }
            }
        } else {
            menu.displayDialogue("\n" + target.getName() + " ne peut pas utiliser ÃƒÂ§a !");
        }
        gameContext.popState();
    }

    @Override
    public void exit() {
        menu.setMenuRequest(null, null);
    }
}