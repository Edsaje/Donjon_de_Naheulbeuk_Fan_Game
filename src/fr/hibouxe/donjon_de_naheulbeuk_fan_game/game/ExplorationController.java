package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.NaheulbeukDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.Item;

import java.util.List;

/**
 * Contrôleur principal du jeu.
 * Gère la boucle globale de jeu, l'initialisation de la carte et de l'équipe,
 * les déplacements du joueur et les collisions avec les murs et les monstres.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class ExplorationController {
    // Attributs
    private Dungeon maze;
    private Team team;
    private boolean running;
    private int currentFloor = 1;
    private Menu menu;
    private boolean isTutorial;

    /**
     * Constructeur injectant toutes les dépendances.
     */
    public ExplorationController(Dungeon maze, Team team, Menu menu, boolean isTutorial) {
        this.maze = maze;
        this.team = team;
        this.menu = menu;
        this.isTutorial = isTutorial;
    }

    /**
     * Démarre la boucle d'exploration.
     */
    public void start() {
        this.running = true;
        while (running) {
            menu.display(maze, team);
            playerMovement();
        }
    }

    /**
     * Gère les saisies de déplacement et les commandes du joueur dans le donjon.
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
                running = false;
                menu.displayMessage("Tchoss Nulloss");
                break;
            case "C":
                menu.displayTeamStats(team);
                break;
            case "I":
                handleInventoryAction();
                break;
            default:
                menu.displayMessage("Commande inconnue");
        }

        if (!moved && !choice.equals("X") && "ZSQD".contains(choice)) {
            menu.displayMessage("\nTu vas dans un mur");
        }

        if (moved) {
            Cell currentCell = maze.getGrid()[team.getX()][team.getY()];
            handleCellEvents(currentCell);
        }
    }

    /**
     * Gère la consultation et l'utilisation des objets du sac à dos.
     */
    private void handleInventoryAction() {
        menu.displayInventory(team);
        int choice = menu.askInventoryMenuChoice();

        switch (choice) {
            case 1:
                if (!team.getInventory().isEmpty()) {
                    int itemIndex = menu.askItemIndex();
                    if (itemIndex >= 0 && itemIndex < team.getInventory().size()) {
                        Item selectedItem = team.getInventory().get(itemIndex);
                        Character target = menu.askItemTarget(team);
                        if (target != null) {
                            boolean used = selectedItem.use(target);
                            if (used) {
                                team.removeItem(selectedItem);
                                menu.displayMessage("\n" + target.getName() + " utilise ou s'équipe de " + selectedItem.getName() + " !");
                            } else {
                                menu.displayMessage("\n" + target.getName() + " ne peut pas utiliser ça ! C'est réservé à une autre classe...");
                            }
                        }
                    }
                } else {
                    menu.displayMessage("Le sac à dos est vide ! Impossible d'utiliser un objet.");
                }
                break;

            case 2:
                Character unequipTarget = menu.askItemTarget(team);
                if (unequipTarget != null) {
                    fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentSlot slot = menu.askSlotToUnequip();
                    if (slot != null) {
                        unequipTarget.unequip(slot, team, menu);
                    }
                }
                break;

            case 3:
                menu.displayMessage("Fermeture du sac à dos.");
                break;
        }
    }

    /**
     * Gère les événements d'une case (combat avec monstre, coffre à trésor).
     *
     * @param currentCell La case sur laquelle se trouve la compagnie
     */
    private void handleCellEvents(Cell currentCell) {
        // 1. Rencontre avec un monstre
        if (currentCell.hasMonster()) {
            List<Character> monsters = currentCell.getMonsters();
            if (monsters.size() == 1) {
                menu.displayMessage("\nUN " + monsters.get(0).getName().toUpperCase() + " APPARAÎT ! BASTOOON !");
            } else {
                menu.displayMessage("\nUNE HORDE DE " + monsters.size() + " MONSTRES APPARAÎT ! BASTOOON !");
            }

            Battle battle = new Battle(team, monsters, menu);
            boolean victory = battle.start();

            if (victory) {
                currentCell.getMonsters().clear(); // On retire le monstre vaincu
            } else {
                running = false; // Fin de partie si défaite
            }
        }

        // 2. Découverte d'un coffre d'objet
        if (currentCell.hasItem()) {
            Item item = currentCell.getItem();
            boolean take = menu.askPickupItem(item);

            if (take) {
                boolean added = team.addItem(item);
                if (added) {
                    menu.displayMessage("\n" + item.getName() + " ramassé(e), espérons que le Nain ne vole rien !");
                    currentCell.setItem(null); // On retire le coffre une fois ramassé
                } else {
                    menu.displayMessage("\nJe crois que le Nain essaye encore de porter trop d'objets !");
                }
            } else {
                menu.displayMessage("\nVous laissez le coffre intact.");
            }
        }

        // 3. Découverte de l'escalier
        if (currentCell.hasStairs()) {
            if (isTutorial) {
                running = false; // Fin du tutoriel, on sort de la boucle
            } else {
                menu.displayMessage("\n🚪 Vous trouvez un escalier lugubre qui descend dans les profondeurs...");
                currentFloor++;
                menu.displayMessage("=== DESCENTE À L'ÉTAGE " + currentFloor + " ===");
                
                // 1. On regénère un donjon de taille classique
                this.maze = new NaheulbeukDungeon(); 
                // On génère le labyrinthe en partant de la position ACTUELLE de l'équipe pour ne pas la coincer dans un mur !
                this.maze.generateMaze(team.getX(), team.getY());
                this.maze.generateRandomRooms(6, 2, 4);
                
                // 2. On corse la difficulté !
                this.maze.generateMonsters(5 + currentFloor, team.getX(), team.getY()); 
                this.maze.generateItems(3);
                this.maze.generateStairs(1); // Le nouvel escalier !
                
                // L'équipe reste à ses coordonnées (immersion conservée !)
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
