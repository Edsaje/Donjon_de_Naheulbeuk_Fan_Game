package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.Equipment;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.usable.potion.Potion;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IMenuView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.state.GameState;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;

public class InventoryController implements GameState {
    public enum State { BROWSING, ITEM_ACTION, SELECT_TARGET }
    public enum Tab { TOUT, EQUIPEMENT, CONSOMMABLE, MATERIAU }
    public enum SortMode { AUCUN, NOM, TYPE }

    private State currentState = State.BROWSING;
    private Tab currentTab = Tab.TOUT;
    private SortMode currentSort = SortMode.AUCUN;
    
    private Team team;
    private IMenuView menu;
    private GameContext gameContext;
    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon maze;

    private int selectedItemIndex = 0;
    private int selectedActionIndex = 0; // 0=Utiliser/Equiper, 1=Jeter, 2=Annuler
    private int selectedTargetIndex = 0;

    private List<Item> filteredItems;

    public InventoryController(Team team, IMenuView menu, GameContext gameContext, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon maze) {
        this.maze = maze;
        this.team = team;
        this.menu = menu;
        this.gameContext = gameContext;
        refreshFilteredItems();
    }

    private void refreshFilteredItems() {
        filteredItems = team.getInventory().stream().filter(item -> {
            if (currentTab == Tab.EQUIPEMENT) return item instanceof Equipment;
            if (currentTab == Tab.CONSOMMABLE) return item instanceof Potion;
            if (currentTab == Tab.MATERIAU) return !(item instanceof Equipment) && !(item instanceof Potion);
            return true;
        }).collect(Collectors.toList());

        if (currentSort == SortMode.NOM) {
            filteredItems.sort(Comparator.comparing(Item::getName));
        } else if (currentSort == SortMode.TYPE) {
            filteredItems.sort(Comparator.comparing(i -> i.getClass().getSimpleName()));
        }

        if (selectedItemIndex >= filteredItems.size()) {
            selectedItemIndex = Math.max(0, filteredItems.size() - 1);
        }
    }

    @Override
    public void enter() {
        currentState = State.BROWSING;
        refreshFilteredItems();
        menu.setMenuRequest("INVENTORY_UI", null); // Secret code to tell HD2DGameApp to render custom UI
    }

    @Override
    public void update(float deltaTime) {}

    @Override
    public void onInput(String action) {
        if ("ESCAPE".equals(action) || "X".equals(action)) {
            if (currentState == State.BROWSING) {
                gameContext.popState();
            } else if (currentState == State.ITEM_ACTION) {
                currentState = State.BROWSING;
            } else if (currentState == State.SELECT_TARGET) {
                currentState = State.ITEM_ACTION;
            }
            return;
        }

        if (currentState == State.BROWSING) {
            if ("RIGHT".equals(action)) {
                currentTab = Tab.values()[(currentTab.ordinal() + 1) % Tab.values().length];
                refreshFilteredItems();
            } else if ("LEFT".equals(action)) {
                currentTab = Tab.values()[(currentTab.ordinal() - 1 + Tab.values().length) % Tab.values().length];
                refreshFilteredItems();
            } else if ("DOWN".equals(action)) {
                if (selectedItemIndex < filteredItems.size() - 1) selectedItemIndex++;
            } else if ("UP".equals(action)) {
                if (selectedItemIndex > 0) selectedItemIndex--;
            } else if ("Y".equals(action)) { // Toggle Sort
                currentSort = SortMode.values()[(currentSort.ordinal() + 1) % SortMode.values().length];
                refreshFilteredItems();
            } else if ("ENTER".equals(action)) {
                if (!filteredItems.isEmpty()) {
                    currentState = State.ITEM_ACTION;
                    selectedActionIndex = 0;
                }
            }
        } else if (currentState == State.ITEM_ACTION) {
            if ("DOWN".equals(action) || "RIGHT".equals(action)) {
                selectedActionIndex = (selectedActionIndex + 1) % 3;
            } else if ("UP".equals(action) || "LEFT".equals(action)) {
                selectedActionIndex = (selectedActionIndex - 1 + 3) % 3;
            } else if ("ENTER".equals(action)) {
                if (selectedActionIndex == 0) { // Utiliser/Equiper
                    Item item = filteredItems.get(selectedItemIndex);
                    if (!(item instanceof Equipment) && !(item instanceof Potion)) {
                        menu.displayDialogue("\nImpossible d'utiliser cet objet ici.");
                        currentState = State.BROWSING;
                    } else {
                        currentState = State.SELECT_TARGET;
                        selectedTargetIndex = 0;
                    }
                } else if (selectedActionIndex == 1) { // Jeter
                    team.removeItem(filteredItems.get(selectedItemIndex));
                    refreshFilteredItems();
                    currentState = State.BROWSING;
                } else { // Annuler
                    currentState = State.BROWSING;
                }
            }
        } else if (currentState == State.SELECT_TARGET) {
            if ("DOWN".equals(action) || "RIGHT".equals(action)) {
                selectedTargetIndex = (selectedTargetIndex + 1) % team.getMembers().size();
            } else if ("UP".equals(action) || "LEFT".equals(action)) {
                selectedTargetIndex = (selectedTargetIndex - 1 + team.getMembers().size()) % team.getMembers().size();
            } else if ("ENTER".equals(action)) {
                Item item = filteredItems.get(selectedItemIndex);
                Character target = team.getMembers().get(selectedTargetIndex);
                if (item.use(target)) {
                    team.removeItem(item);
                    menu.displayMessage("\n" + target.getName() + " utilise " + item.getName() + " !");
                    refreshFilteredItems();
                    currentState = State.BROWSING;
                } else {
                    menu.displayDialogue("\n" + target.getName() + " ne peut pas utiliser ca !");
                    currentState = State.BROWSING;
                }
            }
        }
    }

    @Override
    public void exit() {
        menu.setMenuRequest(null, null);
    }
    
    // Getters for UI
    public State getCurrentState() { return currentState; }
    public Tab getCurrentTab() { return currentTab; }
    public SortMode getCurrentSort() { return currentSort; }
    public List<Item> getFilteredItems() { return filteredItems; }
    public int getSelectedItemIndex() { return selectedItemIndex; }
    public int getSelectedActionIndex() { return selectedActionIndex; }
    public int getSelectedTargetIndex() { return selectedTargetIndex; }
    public Team getTeam() { return team; }
}