package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view.BattleView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view.DungeonView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view.InventoryView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view.MainMenuView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentSlot;

import java.util.List;
import java.util.Scanner;

/**
 * Façade d'Affichage et d'Entrée/Sortie (Vue) pour l'interface console.
 * Centralise les appels vers les sous-vues spécialisées (DungeonView, InventoryView, BattleView, MainMenuView).
 *
 * @author Hibouxe
 * @version 2.0
 */
public class Menu {
    private Scanner keyboard = new Scanner(System.in);

    // Sous-vues spécialisées (Délégation Façade)
    private DungeonView dungeonView = new DungeonView();
    private InventoryView inventoryView = new InventoryView();
    private BattleView battleView = new BattleView();
    private MainMenuView mainMenuView = new MainMenuView();

    /**
     * Affiche un message personnalisé dans la console.
     */
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Saisit un entier auprès du joueur avec gestion des erreurs de type.
     */
    public int askPlayerInt() {
        System.out.print("> ");
        try {
            return Integer.parseInt(keyboard.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Saisit une chaîne de caractères auprès du joueur.
     */
    public String askPlayerString() {
        System.out.print("> ");
        return keyboard.nextLine();
    }

    // --- DÉLÉGATION DUNGEON VIEW ---

    public String askPlayerMovement() {
        return dungeonView.askPlayerMovement(this);
    }

    public boolean askPickupItem(Item item) {
        return dungeonView.askPickupItem(item, this);
    }

    public void display(Dungeon maze, Team team) {
        dungeonView.display(maze, team, this);
    }

    // --- DÉLÉGATION INVENTORY VIEW ---

    public void displayInventory(Team team) {
        inventoryView.displayInventory(team, this);
    }

    public int askInventoryMenuChoice() {
        return inventoryView.askInventoryMenuChoice(this);
    }

    public EquipmentSlot askSlotToUnequip() {
        return inventoryView.askSlotToUnequip(this);
    }

    public boolean askUseItem() {
        return inventoryView.askUseItem(this);
    }

    public int askItemIndex() {
        return inventoryView.askItemIndex(this);
    }

    public Character askItemTarget(Team team) {
        return inventoryView.askItemTarget(team, this);
    }

    // --- DÉLÉGATION BATTLE VIEW ---

    public void displayBattleStatus(List<Character> monsters, Team team) {
        battleView.displayBattleStatus(monsters, team, this);
    }

    public int askBattleAction(Character attacker) {
        return battleView.askBattleAction(attacker, this);
    }

    public Skill askSkill(Character attacker) {
        return battleView.askSkill(attacker, this);
    }

    public Skill askSkill(Character attacker, List<Character> monsters) {
        return battleView.askSkill(attacker, monsters, this);
    }

    public Character askMonsterTarget(List<Character> monsters) {
        return battleView.askMonsterTarget(monsters, this);
    }

    public Character askAllyToHeal(Team team) {
        return battleView.askAllyToHeal(team, this);
    }

    // --- STATISTIQUES COMPAGNIE ---

    public void displayTeamStats(Team team) {
        displayMessage("\n=================== Fiche de la compagnie de Naheulbeuk ===================");
        for (Character c : team.getMembers()) {
            displayMessage(String.format(" - %-12s | Niv %d | PV: %2d | %s : %2d | Attaque: %2d | Magie: %2d | Défense: %2d | Def.Mag: %2d | Vitesse: %2d",
                    c.getName(), c.getLevel(), c.getHealthPoint(), c.getResourceName(), c.getResourcePoint(), c.getAttack(), c.getMagicAttack(), c.getDefense(), c.getMagicDefense(), c.getSpeed()));
            displayMessage("   └ Équipement : " + c.getEquippedSummary());
        }
        displayMessage("===========================================================================\n");
    }

    // --- DÉLÉGATION MAIN MENU VIEW ---

    public void displayTitleScreen() {
        mainMenuView.displayTitleScreen(this);
    }

    public int askMainMenuChoice() {
        return mainMenuView.askMainMenuChoice(this);
    }

    public boolean askLoadQuickSavePrompt() {
        return mainMenuView.askLoadQuickSavePrompt(this);
    }
}
