package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.Item;

/**
 * Sous-vue responsable du rendu visuel de l'exploration du Donjon (Carte ASCII, Déplacements, Coffres).
 */
public class DungeonView {

    public String askPlayerMovement(Menu menu) {
        menu.displayMessage("\nDéplacement (Z: Nord, S: Sud, Q: Ouest, D: Est | C: Fiche de la compagnie, I: Inventaire | X: Quitter) :");
        return menu.askPlayerString().trim().toUpperCase();
    }

    public boolean askPickupItem(Item item, Menu menu) {
        menu.displayMessage("\n LA COMPAGNIE FOUILLE L'ENDROIT ET TROUVE UN COFFRE !");
        menu.displayMessage("\n Il contient un(e) " + item.getName() + " - " + item.getDescription());
        menu.displayMessage("Ca peut nous être utile ! On le récupère ?");

        int choice = 0;
        while (choice != 1 && choice != 2) {
            menu.displayMessage("1. Oui, on ramasse !");
            menu.displayMessage("2. Non, on laisse,c'est surement un piège..");
            choice = menu.askPlayerInt();
            if (choice != 1 && choice != 2) {
                menu.displayMessage("❌ Choix invalide.");
            }
        }
        return choice == 1;
    }

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
                if (x == team.getX() && y == team.getY()) {
                    sb.append(" @ ");
                } else if (cell.hasMonster()) {
                    sb.append(" M ");
                } else if (cell.hasItem()) {
                    sb.append(" C ");
                } else if (cell.hasStairs()) {
                    sb.append(" ≡ ");
                } else {
                    sb.append("   ");
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
