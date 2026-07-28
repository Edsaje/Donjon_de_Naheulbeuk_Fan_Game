package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.Item;

import java.util.List;
import java.util.Scanner;

/**
 * Composant d'Affichage et d'Entrée/Sortie (Vue) pour l'interface console.
 * S'occupe de l'affichage du labyrinthe en ASCII, des fiches de statistiques
 * et de la lecture des choix de l'utilisateur.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Menu {
    private Scanner keyboard = new Scanner(System.in);

    /**
     * Affiche le message d'introduction du jeu.
     */
    public void introduction() {

    }

    /**
     * Saisit un entier auprès du joueur avec gestion des erreurs de type.
     *
     * @return L'entier saisi par l'utilisateur (ou 0 en cas d'erreur).
     */
    public int askPlayerInt() {
        System.out.print("> ");
        try {
            return keyboard.nextInt();
        } catch (java.util.InputMismatchException e) {
        }
        return 0;
    }

    /**
     * Saisit une chaîne de caractères auprès du joueur.
     *
     * @return La ligne saisie.
     */
    public String askPlayerString() {
        keyboard.nextLine(); // Vider le buffer
        System.out.print("> ");
        return keyboard.nextLine();
    }

    /**
     * Affiche les commandes de déplacement et lit la touche saisie.
     *
     * @return La commande nettoyée en majuscules (ex: "Z", "Q", "S", "D").
     */
    public String askPlayerMovement() {
        System.out.print("\nDéplacement (Z: Nord, S: Sud, Q: Ouest, D: Est | C: Fiche de la compagnie, I: Inventaire | X: Quitter) : ");
        return keyboard.nextLine().trim().toUpperCase(); // Éviter la casse
    }

    /**
     * Affiche un message d'avertissement en cas de collision avec un mur.
     */
    public void displayWallCollision() {
        System.out.println("\nTu vas dans un mur");
    }

    /**
     * Affiche la fiche récapitulative des statistiques de tous les aventuriers de l'équipe.
     *
     * @param team L'équipe de héros à afficher
     */
    public void displayTeamStats(Team team) {
        System.out.println("\n=================== Fiche de la compagnie de Naheulbeuk ===================");
        for (Character c : team.getMembers()) {
            System.out.printf("🔹 %-12s | Niv %d | PV: %2d | " + c.getResourceName() + " : %2d | Attaque: %2d | Attaque Magique: %2d | Défense: %2d | Défense Magique : %2d%n",
                    c.getName(), c.getLevel(), c.getHealthPoint(), c.getResourcePoint(), c.getAttack(), c.getMagicAttack(), c.getDefense(), c.getMagicDefense());
        }
        System.out.println("===========================================================================\n");
    }

    /**
     * Effectue le rendu ASCII du labyrinthe dans la console.
     * Affiche les murs, la position de la Compagnie ('@') et les Monstres ('M').
     *
     * @param maze Le labyrinthe à dessiner
     * @param team L'équipe du joueur pour repérer ses coordonnées
     */
    public void display(Maze maze, Team team) {
        int width = maze.getWidth();
        int height = maze.getHeight();
        Cell[][] grid = maze.getGrid();

        // 1. Dessiner le bord tout en haut du labyrinthe
        for (int x = 0; x < width; x++) {
            System.out.print("+---");
        }
        System.out.println("+");

        // 2. Parcourir ligne par ligne (y de 0 à height-1)
        for (int y = 0; y < height; y++) {

            // Ligne A : Le contenu des cases et les murs Est/Ouest
            System.out.print("|"); // Bordure gauche
            for (int x = 0; x < width; x++) {
                Cell cell = grid[x][y];
                if (x == team.getX() && y == team.getY()) {
                    System.out.print(" @ "); // Le symbole de la compagnie de Naheulbeuk !
                } else if (cell.hasMonster()) {
                    System.out.print(" M "); // Le symbole des monstres
                } else {
                    System.out.print("   "); // Case vide
                }
                if (cell.isWallEast()) {
                    System.out.print("|");
                } else {
                    System.out.print(" "); // Passage ouvert vers l'Est
                }
            }
            System.out.println(); // Fin de la ligne des cases

            // Ligne B : Les murs du bas (Sud) et les coins '+'
            System.out.print("+");
            for (int x = 0; x < width; x++) {
                Cell cell = grid[x][y];
                if (cell.isWallSouth()) {
                    System.out.print("---");
                } else {
                    System.out.print("   "); // Passage ouvert vers le Sud
                }
                System.out.print("+");
            }
            System.out.println(); // Fin de la ligne des murs Sud
        }
    }

    /**
     * Affiche le contenu du sac à dos (inventaire) de la Compagnie de Naheulbeuk.
     *
     * @param team L'équipe du joueur contenant le sac à dos
     */
    public void displayInventory(Team team) {
        System.out.println("\n=================== Sac à dos de la Compagnie ===================");
        List<Item> items = team.getInventory();

        if (items.isEmpty()) {
            System.out.println("  Le sac est vide... Damned !");
        } else {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                System.out.println("  " + (i + 1) + ". " + item.getName() + " : " + item.getDescription());
            }
        }
        System.out.println("=================================================================\n");
    }

    /**
     * Affiche l'état de l'ennemi et les statistiques des membres de la compagnie au début de chaque tour de combat.
     *
     * @param monster Le monstre affronté
     * @param team    La compagnie de Naheulbeuk
     */
    public void displayBattleStatus(Character monster, Team team) {
        System.out.println("\n" + monster.getName().toUpperCase() + " (PV: " + Math.max(0, monster.getHealthPoint())
                + " | Attaque: " + monster.getAttack()
                + " | Attaque Magique: " + monster.getMagicAttack()
                + " | Defense: " + monster.getDefense()
                + " | Defense Magique: " + monster.getMagicDefense() + ")\n");

        for (int i = 0; i < team.getMembers().size(); i++) {
            Character c = team.getMembers().get(i);
            if (c.getHealthPoint() > 0) {
                System.out.println(i + ". " + c.getName() + " (PV: " + c.getHealthPoint()
                        + " | Attaque: " + c.getAttack()
                        + " | " + c.getResourceName() + ": " + c.getResourcePoint()
                        + " | Defense: " + c.getDefense()
                        + " | Defense Magique: " + c.getMagicDefense() + ")");
            }
        }
    }
}
