package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentSlot;

import java.util.List;

/**
 * Sous-vue responsable de l'affichage du sac à dos, du déséquipement et des interactions d'objets.
 */
public class InventoryView {

    public void displayInventory(Team team, Menu menu) {
        menu.displayMessage("\n=================== Sac à dos de la Compagnie ===================");
        List<Item> items = team.getInventory();

        if (items.isEmpty()) {
            menu.displayMessage("  Le sac est vide... Damned !");
        } else {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                menu.displayMessage("  " + (i + 1) + ". " + item.getName() + " : " + item.getDescription());
            }
        }
        menu.displayMessage("=================================================================\n");
    }

    public int askInventoryMenuChoice(Menu menu) {
        menu.displayMessage("\n--- GESTION DU SAC À DOS ---");
        menu.displayMessage("1. Utiliser / Équiper un objet du sac");
        menu.displayMessage("2. Retirer un équipement d'un héros");
        menu.displayMessage("3. Fermer le sac");

        int choice = 0;
        while (choice < 1 || choice > 3) {
            choice = menu.askPlayerInt();
            if (choice < 1 || choice > 3) {
                menu.displayMessage("❌ Choix invalide. Entrez 1, 2 ou 3.");
            }
        }
        return choice;
    }

    public EquipmentSlot askSlotToUnequip(Menu menu) {
        menu.displayMessage("\n--- QUEL EMPLACEMENT VOULEZ-VOUS DÉSÉQUIPER ? ---");
        menu.displayMessage("1. 🗡️ Arme (Main Droite)");
        menu.displayMessage("2. 🛡️ Main Gauche (Bouclier / Grimoire)");
        menu.displayMessage("3. 🪖 Tête (Casque)");
        menu.displayMessage("4. 🥋 Torse (Armure / Pagne / Robe)");
        menu.displayMessage("5. 👖 Jambes");
        menu.displayMessage("6. 💍 Bijou");
        menu.displayMessage("0. ↩️ Annuler");

        while (true) {
            int choice = menu.askPlayerInt();
            switch (choice) {
                case 0: return null;
                case 1: return EquipmentSlot.WEAPON;
                case 2: return EquipmentSlot.LEFT_HAND;
                case 3: return EquipmentSlot.HEAD;
                case 4: return EquipmentSlot.CHEST;
                case 5: return EquipmentSlot.LEGS;
                case 6: return EquipmentSlot.JEWELRY;
                default:
                    menu.displayMessage("❌ Choix invalide (ou 0 pour annuler).");
            }
        }
    }

    public boolean askUseItem(Menu menu) {
        menu.displayMessage("\nVoulez-vous utiliser un objet du sac ?");
        int choice = 0;
        while (choice != 1 && choice != 2) {
            menu.displayMessage("1. Oui, utiliser un objet");
            menu.displayMessage("2. Non, fermer le sac");
            choice = menu.askPlayerInt();
            if (choice != 1 && choice != 2) {
                menu.displayMessage("❌ Choix invalide.");
            }
        }
        return choice == 1;
    }

    public int askItemIndex(Menu menu) {
        menu.displayMessage("Quel objet voulez-vous utiliser ? (Entrez le numéro, ou 0 pour Retour)");
        int choice = menu.askPlayerInt();
        return (choice > 0) ? choice - 1 : -1;
    }

    public Character askItemTarget(Team team, Menu menu) {
        menu.displayMessage("\nSur quel aventurier voulez-vous agir ? (0. ↩️ Retour)");
        for (int i = 0; i < team.getMembers().size(); i++) {
            Character c = team.getMembers().get(i);
            menu.displayMessage((i + 1) + ". " + c.getName() + " (PV: " + c.getHealthPoint() + ")");
        }
        int choice = -1;
        while (true) {
            choice = menu.askPlayerInt();
            if (choice == 0) return null;
            if (choice > 0 && choice <= team.getMembers().size()) {
                return team.getMembers().get(choice - 1);
            }
            menu.displayMessage("❌ Cible invalide (ou 0 pour annuler).");
        }
    }
}
