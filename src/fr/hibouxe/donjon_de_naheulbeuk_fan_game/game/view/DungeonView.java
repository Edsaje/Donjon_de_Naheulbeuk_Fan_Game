package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.Item;

/**
 * Sous-vue responsable du rendu visuel de l'exploration du Donjon (Carte ASCII, Déplacements, Coffres).
 *
 * @author Hibouxe
 * @version 1.0
 */
public class DungeonView {

    /**
     * Affiche l'invite de commande de déplacement et retourne la touche saisie.
     *
     * @param menu La vue principale (Injectée)
     * @return La commande nettoyée en majuscules (ex: "Z", "Q", "S", "D").
     */
    public String askPlayerMovement(Menu menu) {
        menu.displayMessage("\nDéplacement (Z: Nord, S: Sud, Q: Ouest, D: Est | C: Compagnie, I: Sac, K: QuickSave | X: Quitter) :");
        return menu.askPlayerString().trim().toUpperCase();
    }

    /**
     * Demande au joueur s'il souhaite ramasser un objet trouvé dans un coffre.
     *
     * @param item L'objet trouvé
     * @param menu La vue principale (Injectée)
     * @return true si le joueur choisit de ramasser l'objet, false sinon.
     */
    public boolean askPickupItem(Item item, Menu menu) {
        menu.displayMessage("\n LA COMPAGNIE FOUILLE L'ENDROIT ET TROUVE UN COFFRE !");
        menu.displayMessage("\n Il contient un(e) " + item.getName() + " - " + item.getDescription());
        menu.displayMessage("Ca peut nous être utile ! On le récupère ?");

        int choice = 0;
        while (choice != 1 && choice != 2) {
            menu.displayMessage("1. Oui, on ramasse !");
            menu.displayMessage("2. Non, on laisse, c'est surement un piège..");
            choice = menu.askPlayerInt();
            if (choice != 1 && choice != 2) {
                menu.displayMessage("[Erreur] Choix invalide.");
            }
        }
        return choice == 1;
    }

    /**
     * Effectue le rendu ASCII complet de la carte du labyrinthe dans la console.
     *
     * @param maze Le labyrinthe à afficher
     * @param team L'équipe de héros pour repérer ses coordonnées
     * @param menu La vue principale (Injectée)
     */
    public void display(Dungeon maze, Team team, Menu menu) {
        int width = maze.getWidth();
        int height = maze.getHeight();
        Cell[][] grid = maze.getGrid();

        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < width; x++) {
            sb.append("+---");
        }
        sb.append("+\n");

        for (int y = 0; y < height; y++) {
            sb.append("|");
            for (int x = 0; x < width; x++) {
                Cell cell = grid[x][y];
                if (cell.isWall()) {
                    sb.append("###");
                } else if (x == team.getX() && y == team.getY()) {
                    sb.append(" @ ");
                } else if (cell.hasMonster()) {
                    sb.append(" M ");
                } else if (cell.hasItem()) {
                    sb.append(" C ");
                } else if (cell.hasStairs()) {
                    sb.append(" ≡ ");
                } else {
                    sb.append(" . ");
                }
                if (cell.isWallEast()) {
                    sb.append("|");
                } else {
                    sb.append(" ");
                }
            }
            sb.append("\n");

            sb.append("+");
            for (int x = 0; x < width; x++) {
                Cell cell = grid[x][y];
                if (cell.isWallSouth()) {
                    sb.append("---");
                } else {
                    sb.append("   ");
                }
                sb.append("+");
            }
            sb.append("\n");
        }

        menu.displayMessage(sb.toString());
    }
}
