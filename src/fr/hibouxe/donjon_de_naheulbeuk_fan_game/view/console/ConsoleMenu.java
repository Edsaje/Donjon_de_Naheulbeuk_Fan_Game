package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console.ConsoleBattleView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console.ConsoleDungeonView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IGameView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console.ConsoleInventoryView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console.ConsoleMainMenuView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot;

import java.util.List;
import java.util.Scanner;

/**
 * FaÃ§ade d'Affichage et d'EntrÃ©e/Sortie (Vue Console).
 * ImplÃ©mente l'interface {@link IGameView} (Architecture Plug-and-Play 2.5D).
 * Centralise les appels vers les sous-vues spÃ©cialisÃ©es (ConsoleDungeonView, ConsoleInventoryView, ConsoleBattleView, ConsoleMainMenuView).
 *
 * @author Hibouxe
 * @version 3.0
 */
public class ConsoleMenu implements IGameView {
    private Scanner keyboard = new Scanner(System.in);

    // Sous-vues spÃ©cialisÃ©es (DÃ©lÃ©gation FaÃ§ade)
    private ConsoleDungeonView ConsoleDungeonView = new ConsoleDungeonView();
    private ConsoleInventoryView ConsoleInventoryView = new ConsoleInventoryView();
    private ConsoleBattleView ConsoleBattleView = new ConsoleBattleView();
    private ConsoleMainMenuView ConsoleMainMenuView = new ConsoleMainMenuView();

    @Override
    public void clearMessages() {}

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayDialogue(String message) {
        System.out.println(message);
        System.out.print("[Appuyez sur ENTRÃ‰E pour continuer...]");
        keyboard.nextLine();
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

    // --- DÃ‰LÃ‰GATION MAIN ConsoleMenu VIEW & MULTI-SLOTS ---

    @Override
    public void displayTitleScreen() {
        ConsoleMainMenuView.displayTitleScreen(this);
    }

    @Override
    public int askMainMenuChoice() {
        return ConsoleMainMenuView.askMainMenuChoice(this);
    }

    @Override
    public int askHubChoice() {
        return askPlayerInt();
    }

    @Override
    public boolean askLoadQuickSavePrompt() {
        return ConsoleMainMenuView.askLoadQuickSavePrompt(this, 1, "");
    }

    public boolean askLoadQuickSavePrompt(int slot, String summary) {
        return ConsoleMainMenuView.askLoadQuickSavePrompt(this, slot, summary);
    }

    @Override
    public boolean askConfirmAbandonQuickSave() {
        return ConsoleMainMenuView.askConfirmAbandonQuickSave(this);
    }

    @Override
    public int askSlotChoice(String actionTitle, String[] slotSummaries) {
        return ConsoleMainMenuView.askSlotChoice(this, actionTitle, slotSummaries);
    }

    @Override
    public int askSlotManagementAction() {
        return ConsoleMainMenuView.askSlotManagementAction(this);
    }

    @Override
    public int askTargetCopySlot(int sourceSlot, String[] slotSummaries) {
        return ConsoleMainMenuView.askTargetCopySlot(this, sourceSlot, slotSummaries);
    }

    // --- DÃ‰LÃ‰GATION DUNGEON VIEW ---

    @Override
    public void display(Dungeon maze, Team team, int currentFloor) {
        ConsoleDungeonView.display(maze, team, this);
    }

    @Override
    public void displayTransitionScreen(int floorNumber) {
        System.out.println("\n\n\n=== [ ÉTAGE " + floorNumber + " ] ===\n\n\n");
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {}
    }

    @Override
    public void displayDungeon(Dungeon maze, Team team, int currentFloor) {
        ConsoleDungeonView.display(maze, team, this);
    }

    @Override
    public String askPlayerMovement() {
        return ConsoleDungeonView.askPlayerMovement(this);
    }

    @Override
    public boolean askPickupItem(Item item) {
        return ConsoleDungeonView.askPickupItem(item, this);
    }

    // --- DÃ‰LÃ‰GATION INVENTORY VIEW ---

    @Override
    public void displayInventory(Team team) {
        ConsoleInventoryView.displayInventory(team, this);
    }

    @Override
    public int askInventoryMenuChoice() {
        return ConsoleInventoryView.askInventoryMenuChoice(this);
    }

    @Override
    public EquipmentSlot askSlotToUnequip() {
        return ConsoleInventoryView.askSlotToUnequip(this);
    }

    @Override
    public boolean askUseItem() {
        return ConsoleInventoryView.askUseItem(this);
    }

    @Override
    public int askItemIndex() {
        return ConsoleInventoryView.askItemIndex(this);
    }

    @Override
    public Character askItemTarget(Team team) {
        return ConsoleInventoryView.askItemTarget(team, this);
    }

    // --- DÃ‰LÃ‰GATION BattleController VIEW ---

    @Override
    public void displayBattleStatus(List<Character> monsters, Team team) {
        ConsoleBattleView.displayBattleStatus(monsters, team, this);
    }

    @Override
    public int askBattleAction(Character attacker) {
        return ConsoleBattleView.askBattleAction(attacker, this);
    }

    public Skill askSkill(Character attacker) {
        return ConsoleBattleView.askSkill(attacker, this);
    }

    @Override
    public Skill askSkill(Character attacker, List<Character> monsters) {
        return ConsoleBattleView.askSkill(attacker, monsters, this);
    }
    @Override
    public Character askMonsterTarget(List<Character> monsters) {
        return ConsoleBattleView.askMonsterTarget(monsters, this);
    }
    
    @Override
    public void displayTeamStats(Team team) {
    }

    @Override
    public void displayTurn(String characterName) {
        displayMessage("\n⚡ C'est au tour de " + characterName + " !");
    }

    @Override
    public void displayVictory() {
        displayMessage("\n C'est trop facile..");
    }

    @Override
    public void displayDefeat() {
        displayMessage("\n Plutôt paradis des Nains ou des Aventuriers ?");
    }

    @Override
    public void displaySaveSuccess(int slot) {
        displayMessage("\n[Sauvegarde Rapide] Donjon et position enregistrés dans le Slot " + slot + ". Retour à l'écran initial...");
    }

    @Override
    public void displaySaveError() {
        displayMessage("\n[Erreur] Échec de la Sauvegarde Rapide.");
    }
}