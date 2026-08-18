package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;

public interface IMenuView {
    void displayTitleScreen();
    int askMainMenuChoice();
    int askHubChoice();
    boolean askLoadQuickSavePrompt();
    boolean askLoadQuickSavePrompt(int slot, String summary);
    boolean askConfirmAbandonQuickSave();
    int askSlotChoice(String actionTitle, String[] slotSummaries);
    int askSlotManagementAction();
    int askTargetCopySlot(int sourceSlot, String[] slotSummaries);
    void displayInventory(Team team);
    int askInventoryMenuChoice();
    void displayStatusScreen(Team team);
    EquipmentSlot askSlotToUnequip();
    boolean askUseItem();
    int askItemIndex();
    Character askItemTarget(Team team);
    void displayMessage(String message);
    void clearMessages();
    void displayDialogue(String message);
    int askPlayerInt();
    String askPlayerString();
    int getMenuSelection();
    void resetMenuSelection();
    void setMenuRequest(String title, String[] options);
    void displaySaveSuccess(int slot);
    void displaySaveError();
}
