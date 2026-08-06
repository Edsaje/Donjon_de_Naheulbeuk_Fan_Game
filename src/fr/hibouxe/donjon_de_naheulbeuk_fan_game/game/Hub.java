package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

/**
 * Contrôleur gérant la phase de repos au campement.
 * Respecte le SRP : ne fait rien d'autre que gérer le menu entre les donjons.
 */
public class Hub {
    private Team team;
    private Menu menu;
    private int activeSlot = 1;

    public Hub(Team team, Menu menu) {
        this(team, menu, 1);
    }

    public Hub(Team team, Menu menu, int activeSlot) {
        this.team = team;
        this.menu = menu;
        this.activeSlot = activeSlot;
    }

    /**
     * Lance la boucle du Hub.
     * @return true si le joueur veut partir à l'aventure, false s'il quitte le jeu.
     */
    public boolean enter() {
        menu.displayMessage("\n=== LE CAMPEMENT (HUB) ===");
        menu.displayMessage("La compagnie se repose autour d'un feu de camp mal allumé par l'Elfe...");

        boolean inHub = true;
        while (inHub) {
            menu.displayMessage("\n--- QUE VOULEZ-VOUS FAIRE ? ---");
            menu.displayMessage("1. Descendre dans le Donjon (RogueLite Mode)");
            menu.displayMessage("2. Voir la fiche de la compagnie");
            menu.displayMessage("3. Regarder dans le sac à dos");
            menu.displayMessage("4. Sauvegarder la partie (Feu de camp)");
            menu.displayMessage("5. Quitter le jeu");

            int choice = menu.askPlayerInt();
            switch (choice) {
                case 1:
                    menu.displayMessage("Vous rangez vos affaires et vous dirigez vers l'entrée du gouffre...");
                    return true; // On retourne true pour signaler à Game.java de lancer le donjon
                case 2:
                    menu.displayTeamStats(team);
                    break;
                case 3:
                    menu.displayInventory(team);
                    int invChoice = menu.askInventoryMenuChoice();
                    if (invChoice == 1 && !team.getInventory().isEmpty()) {
                        int itemIndex = menu.askItemIndex();
                        if (itemIndex >= 0 && itemIndex < team.getInventory().size()) {
                            fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.Item selectedItem = team.getInventory().get(itemIndex);
                            fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character target = menu.askItemTarget(team);
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
                        fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character unequipTarget = menu.askItemTarget(team);
                        if (unequipTarget != null) {
                            fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentSlot slot = menu.askSlotToUnequip();
                            if (slot != null) {
                                unequipTarget.unequip(slot, team, menu);
                            }
                        }
                    }
                    break;
                case 4:
                    boolean saved = fr.hibouxe.donjon_de_naheulbeuk_fan_game.save.SaveManager.saveHubSave(activeSlot, team, 1);
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
