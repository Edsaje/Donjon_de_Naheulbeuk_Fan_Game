package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Maze;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
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
     * Affiche un message personnalisé dans la console.
     * Centralise l'ensemble des sorties textuelles du jeu.
     *
     * @param message Le texte à afficher
     */
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Affiche le message d'introduction du jeu.
     */
    public void introduction() {

    }

    /**
     * Saisit un entier auprès du joueur avec gestion des erreurs de type.
     *
     * @return L'entier saisi par l'utilisateur (ou -1 en cas d'erreur).
     */
    public int askPlayerInt() {
        System.out.print("> ");
        try {
            return Integer.parseInt(keyboard.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Saisit une chaîne de caractères auprès du joueur.
     *
     * @return La ligne saisie.
     */
    public String askPlayerString() {
        System.out.print("> ");
        return keyboard.nextLine();
    }

    /**
     * Affiche les commandes de déplacement et lit la touche saisie.
     *
     * @return La commande nettoyée en majuscules (ex: "Z", "Q", "S", "D").
     */
    public String askPlayerMovement() {
        displayMessage("\nDéplacement (Z: Nord, S: Sud, Q: Ouest, D: Est | C: Fiche de la compagnie, I: Inventaire | X: Quitter) :");
        return askPlayerString().trim().toUpperCase();
    }

    /**
     * Demande au joueur s'il souhaite ramasser un objet découvert dans un coffre.
     *
     * @param item L'objet trouvé dans le coffre
     * @return true si le joueur choisit de ramasser l'objet (1), false sinon.
     */
    public boolean askPickupItem(Item item) {
        displayMessage("\n LA COMPAGNIE FOUILLE L'ENDROIT ET TROUVE UN COFFRE !");
        displayMessage("\n Il contient un(e) " + item.getName() + " - " + item.getDescription());
        displayMessage("Ca peut nous être utile ! On le récupère ?");
        displayMessage("1. Oui, on ramasse !");
        displayMessage("2. Non, on laisse,c'est surement un piège..");

        int choice = askPlayerInt();
        return choice == 1;
    }

    /**
     * Affiche la fiche récapitulative des statistiques de tous les aventuriers de l'équipe.
     *
     * @param team L'équipe de héros à afficher
     */
    public void displayTeamStats(Team team) {
        displayMessage("\n=================== Fiche de la compagnie de Naheulbeuk ===================");
        for (Character c : team.getMembers()) {
            displayMessage(String.format("🔹 %-12s | Niv %d | PV: %2d | %s : %2d | Attaque: %2d | Attaque Magique: %2d | Défense: %2d | Défense Magique : %2d",
                    c.getName(), c.getLevel(), c.getHealthPoint(), c.getResourceName(), c.getResourcePoint(), c.getAttack(), c.getMagicAttack(), c.getDefense(), c.getMagicDefense()));
            displayMessage("   └ Équipement : " + c.getEquippedSummary());
        }
        displayMessage("===========================================================================\n");
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

        StringBuilder sb = new StringBuilder();

        // 1. Dessiner le bord tout en haut du labyrinthe
        for (int x = 0; x < width; x++) {
            sb.append("+---");
        }
        sb.append("+\n");

        // 2. Parcourir ligne par ligne (y de 0 à height-1)
        for (int y = 0; y < height; y++) {

            // Ligne A : Le contenu des cases et les murs Est/Ouest
            sb.append("|"); // Bordure gauche
            for (int x = 0; x < width; x++) {
                Cell cell = grid[x][y];
                if (x == team.getX() && y == team.getY()) {
                    sb.append(" @ "); // Le symbole de la compagnie de Naheulbeuk !
                } else if (cell.hasMonster()) {
                    sb.append(" M "); // Le symbole des monstres
                } else if (cell.hasItem()) {
                    sb.append(" C "); // Le symbole des coffres
                } else if (cell.hasStairs()) {
                    sb.append(" ≡ "); // Le symbole de l'escalier
                } else {
                    sb.append("   "); // Case vide
                }
                if (cell.isWallEast()) {
                    sb.append("|");
                } else {
                    sb.append(" "); // Passage ouvert vers l'Est
                }
            }
            sb.append("\n"); // Fin de la ligne des cases

            // Ligne B : Les murs du bas (Sud) et les coins '+'
            sb.append("+");
            for (int x = 0; x < width; x++) {
                Cell cell = grid[x][y];
                if (cell.isWallSouth()) {
                    sb.append("---");
                } else {
                    sb.append("   "); // Passage ouvert vers le Sud
                }
                sb.append("+");
            }
            sb.append("\n"); // Fin de la ligne des murs Sud
        }

        displayMessage(sb.toString());
    }

    /**
     * Affiche le contenu du sac à dos (inventaire) de la Compagnie de Naheulbeuk.
     *
     * @param team L'équipe du joueur contenant le sac à dos
     */
    public void displayInventory(Team team) {
        displayMessage("\n=================== Sac à dos de la Compagnie ===================");
        List<Item> items = team.getInventory();

        if (items.isEmpty()) {
            displayMessage("  Le sac est vide... Damned !");
        } else {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                displayMessage("  " + (i + 1) + ". " + item.getName() + " : " + item.getDescription());
            }
        }
        displayMessage("=================================================================\n");
    }

    /**
     * Affiche l'état de l'ennemi et les statistiques des membres de la compagnie au début de chaque tour de combat.
     *
     * @param monster Le monstre affronté
     * @param team    La compagnie de Naheulbeuk
     */
    public void displayBattleStatus(Character monster, Team team) {
        displayMessage("\n" + monster.getName().toUpperCase() + " (PV: " + Math.max(0, monster.getHealthPoint())
                + " | Attaque: " + monster.getAttack()
                + " | Attaque Magique: " + monster.getMagicAttack()
                + " | Defense: " + monster.getDefense()
                + " | Defense Magique: " + monster.getMagicDefense() + ")\n");

        for (int i = 0; i < team.getMembers().size(); i++) {
            Character c = team.getMembers().get(i);
            if (c.getHealthPoint() > 0) {
                displayMessage(i + ". " + c.getName() + " (PV: " + c.getHealthPoint()
                        + " | Attaque: " + c.getAttack()
                        + " | " + c.getResourceName() + ": " + c.getResourcePoint()
                        + " | Defense: " + c.getDefense()
                        + " | Defense Magique: " + c.getMagicDefense() + ")");
            }
        }
    }

    /**
     * Demande au joueur de choisir quel membre de la compagnie passe à l'action.
     *
     * @return L'index du personnage choisi
     */
    public int askAttacker() {
        displayMessage("\nQui passe à l'action ?");
        int choice = askPlayerInt();
        return (choice >= 0) ? choice : 0;
    }

    /**
     * Demande au joueur de choisir le type d'action (Attaque physique ou Compétence).
     *
     * @param attacker Le personnage qui agit
     * @return L'option choisie (1 pour Physique, 2 pour Compétence)
     */
    public int askBattleAction(Character attacker) {
        displayMessage("\n" + attacker.getName() + " réfléchit à sa prochaine action...");
        displayMessage("1. Attaque Physique");
        displayMessage("2. Compétence Spéciale / Magie");
        int choice = askPlayerInt();
        return (choice > 0) ? choice : 1;
    }

    /**
     * Demande au joueur s'il souhaite utiliser un objet du sac à dos.
     *
     * @return true si le joueur choisit d'utiliser un objet, false sinon.
     */
    public boolean askUseItem() {
        displayMessage("\nVoulez-vous utiliser un objet du sac ?");
        displayMessage("1. Oui, utiliser un objet");
        displayMessage("2. Non, fermer le sac");
        int choice = askPlayerInt();
        return choice == 1;
    }

    /**
     * Demande au joueur de choisir le numéro de l'objet à utiliser.
     *
     * @return L'index choisi (0-indexed) ou -1 si invalide.
     */
    public int askItemIndex() {
        displayMessage("Quel objet voulez-vous utiliser ? (Entrez le numéro)");
        int choice = askPlayerInt();
        return (choice > 0) ? choice - 1 : -1;
    }

    /**
     * Demande au joueur de choisir sur quel aventurier utiliser un objet.
     *
     * @param team La compagnie de Naheulbeuk
     * @return Le membre choisi ou null si invalide.
     */
    public Character askItemTarget(Team team) {
        displayMessage("\nSur quel aventurier voulez-vous l'utiliser ?");
        for (int i = 0; i < team.getMembers().size(); i++) {
            Character c = team.getMembers().get(i);
            displayMessage(i + ". " + c.getName() + " (PV: " + c.getHealthPoint() + ")");
        }
        int choice = askPlayerInt();
        if (choice >= 0 && choice < team.getMembers().size()) {
            return team.getMembers().get(choice);
        }
        return null;
    }

    /**
     * Demande au joueur de choisir quel coéquipier l'Élfette doit soigner.
     *
     * @param team La compagnie de Naheulbeuk
     * @return Le membre choisi à soigner ou null si invalide.
     */
    public Character askAllyToHeal(Team team) {
        displayMessage("\nChoisissez le coéquipier à soigner :");
        for (int i = 0; i < team.getMembers().size(); i++) {
            Character c = team.getMembers().get(i);
            displayMessage(i + ". " + c.getName() + " | PV: " + Math.max(0, c.getHealthPoint()));
        }
        int choice = askPlayerInt();
        if (choice >= 0 && choice < team.getMembers().size()) {
            return team.getMembers().get(choice);
        }
        return null;
    }
}
