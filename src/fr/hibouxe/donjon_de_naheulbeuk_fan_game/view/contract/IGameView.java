package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot;

import java.util.List;

/**
 * Contrat d'interface abstrait pour la Vue (Plug-and-Play Architecture).
 * Permet de commuter en transparence entre la vue Console ASCII actuelle
 * et un moteur graphique 2.5D (JavaFX / LibGDX / Swing) sans modifier une seule ligne du Modèle ou des Contrôleurs.
 *
 * @author Hibouxe
 * @version 1.0
 */
public interface IGameView {
    void displayMessage(String message);
    int askPlayerInt();
    String askPlayerString();

    // Écran Titre et ConsoleMenu Principal
    void displayTitleScreen();
    int askMainMenuChoice();
    int askHubChoice();
    boolean askLoadQuickSavePrompt();
    boolean askLoadQuickSavePrompt(int slot, String summary);
    boolean askConfirmAbandonQuickSave();

    // Gestion des Emplacements / Slots
    int askSlotChoice(String actionTitle, String[] slotSummaries);
    int askSlotManagementAction();
    int askTargetCopySlot(int sourceSlot, String[] slotSummaries);

    // Vue Donjon et Déplacement
    void display(Dungeon maze, Team team);
    void displayDungeon(Dungeon maze, Team team);
    String askPlayerMovement();
    boolean askPickupItem(Item item);

    // Vue Inventaire
    void displayInventory(Team team);
    int askInventoryMenuChoice();
    EquipmentSlot askSlotToUnequip();
    boolean askUseItem();
    int askItemIndex();
    Character askItemTarget(Team team);

    // Vue Combat
    void displayBattleStatus(List<Character> monsters, Team team);
    int askBattleAction(Character attacker);
    Skill askSkill(Character attacker, List<Character> monsters);
    Character askMonsterTarget(List<Character> monsters);
    Character askAllyToHeal(Team team);

    // Fiche de la Compagnie
    void displayTeamStats(Team team);
}
