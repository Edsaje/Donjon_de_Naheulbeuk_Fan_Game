package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view.BattleView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view.DungeonView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view.IGameView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view.InventoryView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view.MainMenuView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentSlot;

import java.util.List;
import java.util.Scanner;

/**
 * Façade d'Affichage et d'Entrée/Sortie (Vue Console).
 * Implémente l'interface {@link IGameView} (Architecture Plug-and-Play 2.5D).
 * Centralise les appels vers les sous-vues spécialisées (DungeonView, InventoryView, BattleView, MainMenuView).
 *
 * @author Hibouxe
 * @version 3.0
 */
public class Menu implements IGameView {
    private Scanner keyboard = new Scanner(System.in);

    // Sous-vues spécialisées (Délégation Façade)
    private DungeonView dungeonView = new DungeonView();
    private InventoryView inventoryView = new InventoryView();
    private BattleView battleView = new BattleView();
    private MainMenuView mainMenuView = new MainMenuView();

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public int askPlayerInt() {
        while (true) {
            try {
                System.out.print("> ");
                String input = keyboard.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[Erreur] Veuillez saisir un nombre entier valide.");
            }
        }
    }

    @Override
    public String askPlayerString() {
        System.out.print("> ");
        return keyboard.nextLine();
    }

    // --- DÉLÉGATION MAIN MENU VIEW & MULTI-SLOTS ---

    @Override
    public void displayTitleScreen() {
        mainMenuView.displayTitleScreen(this);
    }

    @Override
    public int askMainMenuChoice() {
        return mainMenuView.askMainMenuChoice(this);
    }

    @Override
    public boolean askLoadQuickSavePrompt() {
        return mainMenuView.askLoadQuickSavePrompt(this, 1, "");
    }

    public boolean askLoadQuickSavePrompt(int slot, String summary) {
        return mainMenuView.askLoadQuickSavePrompt(this, slot, summary);
    }

    @Override
    public boolean askConfirmAbandonQuickSave() {
        return mainMenuView.askConfirmAbandonQuickSave(this);
    }

    @Override
    public int askSlotChoice(String actionTitle, String[] slotSummaries) {
        return mainMenuView.askSlotChoice(this, actionTitle, slotSummaries);
    }

    @Override
    public int askSlotManagementAction() {
        return mainMenuView.askSlotManagementAction(this);
    }

    @Override
    public int askTargetCopySlot(int sourceSlot, String[] slotSummaries) {
        return mainMenuView.askTargetCopySlot(this, sourceSlot, slotSummaries);
    }

    // --- DÉLÉGATION DUNGEON VIEW ---

    @Override
    public void display(Dungeon maze, Team team) {
        dungeonView.display(maze, team, this);
    }

    @Override
    public void displayDungeon(Dungeon maze, Team team) {
        dungeonView.display(maze, team, this);
    }

    @Override
    public String askPlayerMovement() {
        return dungeonView.askPlayerMovement(this);
    }

    @Override
    public boolean askPickupItem(Item item) {
        return dungeonView.askPickupItem(item, this);
    }

    // --- DÉLÉGATION INVENTORY VIEW ---

    @Override
    public void displayInventory(Team team) {
        inventoryView.displayInventory(team, this);
    }

    @Override
    public int askInventoryMenuChoice() {
        return inventoryView.askInventoryMenuChoice(this);
    }

    @Override
    public EquipmentSlot askSlotToUnequip() {
        return inventoryView.askSlotToUnequip(this);
    }

    @Override
    public boolean askUseItem() {
        return inventoryView.askUseItem(this);
    }

    @Override
    public int askItemIndex() {
        return inventoryView.askItemIndex(this);
    }

    @Override
    public Character askItemTarget(Team team) {
        return inventoryView.askItemTarget(team, this);
    }

    // --- DÉLÉGATION BATTLE VIEW ---

    @Override
    public void displayBattleStatus(List<Character> monsters, Team team) {
        battleView.displayBattleStatus(monsters, team, this);
    }

    @Override
    public int askBattleAction(Character attacker) {
        return battleView.askBattleAction(attacker, this);
    }

    public Skill askSkill(Character attacker) {
        return battleView.askSkill(attacker, this);
    }

    @Override
    public Skill askSkill(Character attacker, List<Character> monsters) {
        return battleView.askSkill(attacker, monsters, this);
    }

    @Override
    public Character askMonsterTarget(List<Character> monsters) {
        return battleView.askMonsterTarget(monsters, this);
    }

    @Override
    public Character askAllyToHeal(Team team) {
        return battleView.askAllyToHeal(team, this);
    }

    // --- STATISTIQUES COMPAGNIE ---

    @Override
    public void displayTeamStats(Team team) {
        displayMessage("\n=================== Fiche de la compagnie de Naheulbeuk ===================");
        for (Character c : team.getMembers()) {
            displayMessage(String.format(" - %-12s | Niv %d | PV: %2d | %s : %2d | Attaque: %2d | Magie: %2d | Défense: %2d | Def.Mag: %2d | Vitesse: %2d",
                    c.getName(), c.getLevel(), c.getHealthPoint(), c.getResourceName(), c.getResourcePoint(), c.getAttack(), c.getMagicAttack(), c.getDefense(), c.getMagicDefense(), c.getSpeed()));
            displayMessage("   └ Équipement : " + c.getEquippedSummary());
        }
        displayMessage("===========================================================================\n");
    }
}
