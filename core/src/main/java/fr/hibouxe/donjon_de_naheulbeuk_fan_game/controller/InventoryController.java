package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IMenuView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.lang.LocalizationManager;

public class InventoryController {

    private IMenuView menu;
    private LocalizationManager locManager;

    public InventoryController(IMenuView menu, LocalizationManager locManager) {
        this.menu = menu;
        this.locManager = locManager;
    }

    public boolean handleInventoryAction(Team team, boolean isTutorial, int currentFloor, boolean elfJoined, boolean elfHealed) {
        menu.displayInventory(team);
        int choice = menu.askInventoryMenuChoice();
        boolean healed = false;

        switch (choice) {
            case 1:
                if (!team.getInventory().isEmpty()) {
                    int itemIndex = menu.askItemIndex();
                    if (itemIndex >= 0 && itemIndex < team.getInventory().size()) {
                        Item selectedItem = team.getInventory().get(itemIndex);
                        Character target = menu.askItemTarget(team);
                        if (target != null) {
                            if (selectedItem instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.usable.potion.Potion && target.getHealthPoint() >= target.getMaxHealthPoint()) {
                                menu.displayDialogue("\n" + target.getName() + " a déjà tous ses PV !");

                                break;
                            }
                            boolean used = selectedItem.use(target);
                            if (used) {
                                team.removeItem(selectedItem);
                                menu.displayMessage("\n" + target.getName() + " utilise ou s'équipe de " + selectedItem.getName() + " !"); 
                                if (isTutorial && currentFloor == 2 && elfJoined && !elfHealed && target.getClass().getSimpleName().equals("Elf")) { 
                                    healed = true;
                                    String healLine = locManager.getString("TUTO_FLOOR_2_ELF_HEALED_1");
                                    menu.displayDialogue(healLine); 
                                } else {
                                    menu.displayDialogue("\nAppuyez sur Entrée pour continuer.");
                                }

                            } else {
                                menu.displayDialogue("\n" + target.getName() + " ne peut pas utiliser ça ! C'est réservé à une autre classe...");

                            }
                        }
                    }
                } else {
                    menu.displayDialogue("Le sac à dos est vide ! Impossible d'utiliser un objet.");

                }
                break;

            case 2:
                Character unequipTarget = menu.askItemTarget(team);
                if (unequipTarget != null) {
                    fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot slot = menu.askSlotToUnequip();
                    if (slot != null) {
                        boolean success = unequipTarget.unequip(slot, team);
                        if (success) {
                            menu.displayDialogue("\n" + unequipTarget.getName() + " retire son équipement et le met dans le sac !");
                        } else {
                            menu.displayDialogue("\nAucun équipement, ou le sac est plein !");
                        }

                    }
                }
                break;

            case 3:
                break;
        }
        return healed;
    }
}
