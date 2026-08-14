package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.ISaveManager;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IMenuView;

/**
 * Contrôleur gérant la phase de repos au campement.
 * Respecte le SRP : ne fait rien d'autre que gérer le ConsoleMenu entre les donjons.
 */
public class HubController {
    private Team team;
    private IMenuView menu;
    private int activeSlot = 1;
    private ISaveManager saveManager;

    public HubController(Team team, IMenuView menu, ISaveManager saveManager) {
        this(team, menu, 1, saveManager);
    }

    public HubController(Team team, IMenuView menu, int activeSlot, ISaveManager saveManager) {
        this.team = team;
        this.menu = menu;
        this.activeSlot = activeSlot;
        this.saveManager = saveManager;
    }

    /**
     * Lance la boucle du hubController.
     * @return true si le joueur veut partir à l'aventure, false s'il quitte le jeu.
     */
    public boolean enter() {
        menu.displayMessage("\n=== LE CAMPEMENT (HubController) ===");
        menu.displayMessage("La compagnie se repose autour d'un feu de camp mal allumé par l'Elfe...");

        boolean inHub = true;
        while (inHub) {
            menu.displayMessage("\n--- QUE VOULEZ-VOUS FAIRE ? ---");
            menu.displayMessage("1. Descendre dans le Donjon (RogueLite Mode)");
            menu.displayMessage("2. Voir la fiche de la compagnie");
            menu.displayMessage("3. Regarder dans le sac à dos");
            menu.displayMessage("4. Sauvegarder la partie (Feu de camp)");
            menu.displayMessage("5. Quitter le jeu");

            int choice = menu.askHubChoice();
            switch (choice) {
                case 1:
                    menu.displayMessage("Vous rangez vos affaires et vous dirigez vers l'entrée du gouffre...");
                    return true; // On retourne true pour signaler à Game.java de lancer le donjon
                case 2:
                    // TODO: Implémenter le menu Statistiques complet
                    menu.displayDialogue("\n[Le menu Statistiques détaillé sera disponible dans une prochaine version !]");
                    menu.clearMessages();
                    break;
                case 3:
                    menu.displayInventory(team);
                    int invChoice = menu.askInventoryMenuChoice();
                    if (invChoice == 1 && !team.getInventory().isEmpty()) {
                        int itemIndex = menu.askItemIndex();
                        if (itemIndex >= 0 && itemIndex < team.getInventory().size()) {
                            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item selectedItem = team.getInventory().get(itemIndex);
                            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character target = menu.askItemTarget(team);
                            if (target != null) {
                                boolean used = selectedItem.use(target);
                                if (used) {
                                    team.removeItem(selectedItem);
                                    menu.displayMessage("\n" + target.getName() + " utilise ou s'équipe de " + selectedItem.getName() + " !");
                                } else {
                                    menu.displayMessage("\n" + target.getName() + " ne peut pas utiliser ça ! C me réservé à une autre classe...");
                                }
                            }
                        }
                    } else if (invChoice == 2) {
                        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character unequipTarget = menu.askItemTarget(team);
                        if (unequipTarget != null) {
                            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot slot = menu.askSlotToUnequip();
                            if (slot != null) {
                                boolean success = unequipTarget.unequip(slot, team);
                                if (success) {
                                    menu.displayMessage("\n" + unequipTarget.getName() + " retire son équipement et le met dans le sac !");
                                } else {
                                    menu.displayMessage("\nAucun équipement, ou le sac est plein !");
                                }
                            }
                        }
                    }
                    break;
                case 4:
                    boolean saved = saveManager.saveHubSave(activeSlot, team, 1);
                    if (saved) {
                        menu.displayMessage("\n[Sauvegarde] Progression de la Compagnie enregistrée avec succès dans le Slot " + activeSlot + " !");
                    } else {
                        menu.displayMessage("\n[Erreur] Échec de la sauvegarde.");
                    }
                    break;
                case 5:
                    menu.displayMessage("Fin de l'aventure ! Le Nain pleure car il n'a pas eu son or.");
                    return false; // On quitte
                default:
                    menu.displayMessage("[Erreur] Choix invalide.");
            }
        }
        return false;
    }
}
