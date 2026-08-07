package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console.ConsoleMenu;

/**
 * Sous-vue responsable de l'écran-titre initial, du ConsoleMenu principal et de la gestion des Emplacements Multi-Slots (Slots 1, 2, 3).
 * Respecte à 100% le principe MVC et la séparation des responsabilités.
 *
 * @author Hibouxe
 * @version 2.0
 */
public class ConsoleMainMenuView {

    /**
     * Affiche l'écran-titre initial et attend que le joueur appuie sur Entrée.
     *
     * @param ConsoleMenu La vue principale (Injectée)
     */
    public void displayTitleScreen(ConsoleMenu menu) {
        menu.displayMessage("\n==================================================================");
        menu.displayMessage("          DONJON DE NAHEULBEUK - FAN GAME                         ");
        menu.displayMessage("==================================================================");
        menu.displayMessage("   Un Rogue-Lite textuel d'aventure au tour par tour              ");
        menu.displayMessage("   Inspiré de la saga audio mythique de Pen of Chaos              ");
        menu.displayMessage("==================================================================");
        menu.displayMessage("\n---> Appuyez sur ENTRÉE pour accéder au ConsoleMenu principal <---");
        menu.askPlayerString();
    }

    /**
     * Affiche le ConsoleMenu principal et retourne le choix du joueur.
     *
     * @param ConsoleMenu La vue principale (Injectée)
     * @return 1 pour Nouvelle Partie, 2 pour Charger Partie, 3 pour Gestion des Profils/Slots, 4 pour Quitter.
     */
    public int askMainMenuChoice(ConsoleMenu menu) {
        menu.displayMessage("\n=== ConsoleMenu PRINCIPAL ===");
        menu.displayMessage("1. Nouvelle Partie");
        menu.displayMessage("2. Charger Partie");
        menu.displayMessage("3. Gérer les Emplacements de Sauvegarde (Copier / Supprimer)");
        menu.displayMessage("4. Quitter");

        while (true) {
            int choice = menu.askPlayerInt();
            if (choice >= 1 && choice <= 4) {
                return choice;
            }
            menu.displayMessage("[Erreur] Choix invalide. Veuillez entrer 1, 2, 3 ou 4.");
        }
    }

    /**
     * Propose la sélection d'un slot (1, 2 ou 3) avec leurs résumés respectifs.
     *
     * @param ConsoleMenu          La vue principale (Injectée)
     * @param actionTitle   Titre du ConsoleMenu de sélection (ex: "SÉLECTION DE L'EMPLACEMENT DE SAUVEGARDE")
     * @param slotSummaries Résumés des 3 slots (index 0=Slot 1, 1=Slot 2, 2=Slot 3)
     * @return Le numéro du slot choisi (1, 2 ou 3) ou 0 pour annuler.
     */
    public int askSlotChoice(ConsoleMenu menu, String actionTitle, String[] slotSummaries) {
        menu.displayMessage("\n=== " + actionTitle + " ===");
        for (int i = 0; i < slotSummaries.length; i++) {
            menu.displayMessage((i + 1) + ". " + slotSummaries[i]);
        }
        menu.displayMessage("0. Retour");

        while (true) {
            int choice = menu.askPlayerInt();
            if (choice >= 0 && choice <= 3) {
                return choice;
            }
            menu.displayMessage("[Erreur] Choix invalide. Veuillez entrer 0, 1, 2 ou 3.");
        }
    }

    /**
     * ConsoleMenu d'actions de gestion des emplacements (Copier / Supprimer).
     *
     * @param ConsoleMenu La vue principale (Injectée)
     * @return Choix du joueur (1: Copier, 2: Supprimer, 0: Retour).
     */
    public int askSlotManagementAction(ConsoleMenu menu) {
        menu.displayMessage("\n=== GESTION DES EMPLACEMENTS DE SAUVEGARDE ===");
        menu.displayMessage("1. Copier un Emplacement de Sauvegarde");
        menu.displayMessage("2. Supprimer un Emplacement de Sauvegarde");
        menu.displayMessage("0. Retour au ConsoleMenu Principal");

        while (true) {
            int choice = menu.askPlayerInt();
            if (choice >= 0 && choice <= 2) {
                return choice;
            }
            menu.displayMessage("[Erreur] Choix invalide. Veuillez entrer 0, 1 ou 2.");
        }
    }

    /**
     * Choix du slot de destination pour la copie d'un profil.
     *
     * @param ConsoleMenu          La vue principale (Injectée)
     * @param sourceSlot    Le slot d'origine
     * @param slotSummaries Les résumés des slots
     * @return Le numéro du slot de destination (1, 2 ou 3) ou 0 pour annuler.
     */
    public int askTargetCopySlot(ConsoleMenu menu, int sourceSlot, String[] slotSummaries) {
        menu.displayMessage("\n=== COPIER LE SLOT " + sourceSlot + " VERS... ===");
        for (int i = 0; i < slotSummaries.length; i++) {
            int slotNum = i + 1;
            if (slotNum != sourceSlot) {
                menu.displayMessage(slotNum + ". " + slotSummaries[i]);
            }
        }
        menu.displayMessage("0. Annuler");

        while (true) {
            int choice = menu.askPlayerInt();
            if (choice == 0 || (choice >= 1 && choice <= 3 && choice != sourceSlot)) {
                return choice;
            }
            menu.displayMessage("[Erreur] Choix de destination invalide.");
        }
    }

    /**
     * Demande au joueur s'il souhaite charger la Sauvegarde Rapide trouvée après l'écran titre.
     *
     * @param ConsoleMenu        La vue principale (Injectée)
     * @param slot        Le numéro du slot détecté
     * @param slotSummary Le résumé de la quicksave
     * @return true si le joueur veut charger la quicksave, false pour aller au ConsoleMenu principal.
     */
    public boolean askLoadQuickSavePrompt(ConsoleMenu menu, int slot, String slotSummary) {
        menu.displayMessage("\n=== SAUVEGARDE RAPIDE DÉTECTÉE (SLOT " + slot + ") ===");
        menu.displayMessage(slotSummary);
        menu.displayMessage("1. Reprendre l'exploration là où vous vous étiez arrêté");
        menu.displayMessage("2. Accéder au ConsoleMenu Principal");

        while (true) {
            int choice = menu.askPlayerInt();
            if (choice == 1) return true;
            if (choice == 2) return false;
            menu.displayMessage("[Erreur] Choix invalide. Veuillez entrer 1 ou 2.");
        }
    }

    /**
     * Affiche un message d'avertissement si le joueur refuse de reprendre sa sauvegarde rapide.
     * Confirme l'abandon et la suppression définitive de la quicksave.
     *
     * @param ConsoleMenu La vue principale (Injectée)
     * @return true si le joueur confirme l'abandon (suppression), false s'il annule et reprend sa partie.
     */
    public boolean askConfirmAbandonQuickSave(ConsoleMenu menu) {
        menu.displayMessage("\n==================================================================");
        menu.displayMessage("[ATTENTION / WARNING]");
        menu.displayMessage("Si vous accédez au ConsoleMenu Principal sans reprendre votre Sauvegarde Rapide,");
        menu.displayMessage("votre progression temporaire dans le Donjon sera DÉFINITIVEMENT PERDUE !");
        menu.displayMessage("==================================================================");
        menu.displayMessage("1. Oui, abandonner et supprimer la Sauvegarde Rapide");
        menu.displayMessage("2. Non, reprendre la Sauvegarde Rapide");

        while (true) {
            int choice = menu.askPlayerInt();
            if (choice == 1) return true;
            if (choice == 2) return false;
            menu.displayMessage("[Erreur] Choix invalide. Veuillez entrer 1 ou 2.");
        }
    }
}
