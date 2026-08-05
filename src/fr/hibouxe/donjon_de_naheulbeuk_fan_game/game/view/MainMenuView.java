package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

/**
 * Sous-vue responsable de l'écran-titre initial et du menu principal du jeu.
 * Respecte à 100% le principe MVC et la séparation des responsabilités.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class MainMenuView {

    /**
     * Affiche l'écran-titre initial et attend que le joueur appuie sur Entrée.
     *
     * @param menu La vue principale (Injectée)
     */
    public void displayTitleScreen(Menu menu) {
        menu.displayMessage("\n==================================================================");
        menu.displayMessage("          DONJON DE NAHEULBEUK - FAN GAME                         ");
        menu.displayMessage("==================================================================");
        menu.displayMessage("   Un Rogue-Lite textuel d'aventure au tour par tour              ");
        menu.displayMessage("   Inspiré de la saga audio mythique de Pen of Chaos              ");
        menu.displayMessage("==================================================================");
        menu.displayMessage("\n---> Appuyez sur ENTRÉE pour accéder au menu principal <---");
        menu.askPlayerString();
    }

    /**
     * Affiche le menu principal et retourne le choix du joueur.
     *
     * @param menu La vue principale (Injectée)
     * @return 1 pour Nouvelle Partie, 2 pour Charger Partie, 3 pour Quitter.
     */
    public int askMainMenuChoice(Menu menu) {
        menu.displayMessage("\n=== MENU PRINCIPAL ===");
        menu.displayMessage("1. Nouvelle Partie");
        menu.displayMessage("2. Charger Partie");
        menu.displayMessage("3. Quitter");

        while (true) {
            int choice = menu.askPlayerInt();
            if (choice >= 1 && choice <= 3) {
                return choice;
            }
            menu.displayMessage("[Erreur] Choix invalide. Veuillez entrer 1, 2 ou 3.");
        }
    }

    /**
     * Demande au joueur s'il souhaite charger la Sauvegarde Rapide trouvée après l'écran titre.
     *
     * @param menu La vue principale (Injectée)
     * @return true si le joueur veut charger la quicksave, false pour aller au menu principal.
     */
    public boolean askLoadQuickSavePrompt(Menu menu) {
        menu.displayMessage("\n=== SAUVEGARDE RAPIDE DÉTECTÉE ===");
        menu.displayMessage("Une exploration en cours dans le Donjon a été trouvée !");
        menu.displayMessage("1. Reprendre l'exploration là où vous vous étiez arrêté");
        menu.displayMessage("2. Accéder au Menu Principal");

        while (true) {
            int choice = menu.askPlayerInt();
            if (choice == 1) return true;
            if (choice == 2) return false;
            menu.displayMessage("[Erreur] Choix invalide. Veuillez entrer 1 ou 2.");
        }
    }
}
