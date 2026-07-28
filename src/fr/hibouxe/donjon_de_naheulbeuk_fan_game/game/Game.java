package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Maze;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.Item;

/**
 * Contrôleur principal du jeu.
 * Gère la boucle globale de jeu, l'initialisation de la carte et de l'équipe,
 * les déplacements du joueur et les collisions avec les murs et les monstres.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Game {
    // Attributs
    private Maze maze;
    private Team team;
    private boolean running;
    private Menu menu;

    /**
     * Constructeur par défaut (instancie un Menu si aucun n'est fourni).
     */
    public Game() {
        this.menu = new Menu();
    }

    /**
     * Constructeur avec injection de dépendance de la Vue (Menu).
     *
     * @param menu L'instance unique de la vue Menu
     */
    public Game(Menu menu) {
        this.menu = menu;
    }

    /**
     * Démarre la boucle de jeu principale.
     * Génère le labyrinthe, les salles, les monstres et instancie la compagnie de Naheulbeuk.
     */
    public void startGame() {
        // Génération du Maze
        this.maze = new Maze(10, 10);
        this.maze.generateMaze();
        this.maze.generateRandomRooms(6, 2, 4);

        this.maze.generateMonsters(5);
        this.maze.generateItems(3);

        this.running = true;
        // Création de la Team
        this.team = new Team();

        while (running) {
            menu.display(maze, team);
            playerMovement();
        }
    }

    /**
     * Gère la saisie utilisateur du déplacement et exécute la tentative de mouvement.
     * Déclenche un combat en cas de rencontre avec un monstre et ramasse les coffres d'objets.
     */
    public void playerMovement() {
        String choice = menu.askPlayerMovement();
        boolean moved = false;

        switch (choice) {
            case "Z":
                moved = tryMoveNorth();
                break;

            case "S":
                moved = tryMoveSouth();
                break;

            case "Q":
                moved = tryMoveWest();
                break;

            case "D":
                moved = tryMoveEast();
                break;

            case "X":
                running = false; // Stop le jeu
                System.out.println("Tchoss Nulloss");
                break;

            case "C":
                menu.displayTeamStats(team);
                break;

            case "I":
                menu.displayInventory(team);
                break;

            default:
                System.out.println("Commande inconnue");
        }

        if (!moved && !choice.equals("X") && "ZSQD".contains(choice)) {
            menu.displayWallCollision();
        }

        if (moved) {
            Cell currentCell = maze.getGrid()[team.getX()][team.getY()];

            // 1. Rencontre avec un monstre
            if (currentCell.hasMonster()) {
                Character monster = currentCell.getMonster();
                System.out.println("\nUN " + monster.getName().toUpperCase() + " ! BASTOOON ! ");

                Battle battle = new Battle(team, monster, menu);
                boolean victory = battle.start();

                if (victory) {
                    currentCell.setMonster(null); // On retire le monstre vaincu
                } else {
                    running = false; // Fin de partie si défaite
                }
            }

            // 2. Découverte d'un coffre d'objet
            if (currentCell.hasItem()) {
                Item item = currentCell.getItem();
                boolean take = menu.askPickupItem(item);

                if(take) {
                    boolean added = team.addItem(item);
                    if (added) {
                        menu.displayItemPickedUp(item);
                        currentCell.setItem(null); // On retire le coffre une fois ramassé
                    } else {
                        menu.displayInventoryFull();
                    }
                }
                else {
                    menu.displayChestLeftBehind();
                }
            }
        }
    }

    /**
     * Tente de déplacer l'équipe vers le Nord si aucun mur ne bloque le passage.
     *
     * @return true si le mouvement a réussi, false s'il y a un mur.
     */
    public boolean tryMoveNorth() {
        Cell currentCell = maze.getGrid()[team.getX()][team.getY()];
        if (!currentCell.isWallNorth()) {
            team.moveNorth();
            return true;
        }
        return false;
    }

    /**
     * Tente de déplacer l'équipe vers le Sud si aucun mur ne bloque le passage.
     *
     * @return true si le mouvement a réussi, false s'il y a un mur.
     */
    public boolean tryMoveSouth() {
        Cell currentCell = maze.getGrid()[team.getX()][team.getY()];
        if (!currentCell.isWallSouth()) {
            team.moveSouth();
            return true;
        }
        return false;
    }

    /**
     * Tente de déplacer l'équipe vers l'Ouest si aucun mur ne bloque le passage.
     *
     * @return true si le mouvement a réussi, false s'il y a un mur.
     */
    public boolean tryMoveWest() {
        Cell currentCell = maze.getGrid()[team.getX()][team.getY()];
        if (!currentCell.isWallWest()) {
            team.moveWest();
            return true;
        }
        return false;
    }

    /**
     * Tente de déplacer l'équipe vers l'Est si aucun mur ne bloque le passage.
     *
     * @return true si le mouvement a réussi, false s'il y a un mur.
     */
    public boolean tryMoveEast() {
        Cell currentCell = maze.getGrid()[team.getX()][team.getY()];
        if (!currentCell.isWallEast()) {
            team.moveEast();
            return true;
        }
        return false;
    }
}
