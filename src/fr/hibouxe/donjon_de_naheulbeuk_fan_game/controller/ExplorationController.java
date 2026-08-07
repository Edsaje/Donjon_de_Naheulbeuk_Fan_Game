package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.NaheulbeukDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IGameView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;

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
    private IGameView menu;
    private boolean isTutorial;
    private int activeSlot = 1;

    /**
     * Constructeur injectant toutes les dépendances.
     */
    public ExplorationController(Dungeon maze, Team team, IGameView menu, boolean isTutorial) {
        this(maze, team, menu, isTutorial, 1);
    }

    /**
     * Constructeur injectant le slot actif pour la sauvegarde rapide liée.
     */
    public ExplorationController(Dungeon maze, Team team, IGameView menu, boolean isTutorial, int activeSlot) {
        this.maze = maze;
        this.team = team;
        this.menu = menu;
        this.isTutorial = isTutorial;
        this.activeSlot = activeSlot;
    }

    public void setCurrentFloor(int floor) {
        this.currentFloor = floor;
    }

    /**
     * Démarre la boucle d'exploration.
     */
    public void start() {
        this.running = true;
        while (running) {
            menu.display(maze, team, currentFloor);
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
                team.setFacingDirection(1); // Nord
                moved = tryMoveNorth();
                break;
            case "S":
                team.setFacingDirection(0); // Sud
                moved = tryMoveSouth();
                break;
            case "Q":
                team.setFacingDirection(2); // Ouest
                moved = tryMoveWest();
                break;
            case "D":
                team.setFacingDirection(3); // Est
                moved = tryMoveEast();
                break;
            case "1": team.setActiveLeaderIndex(0); break;
            case "2": team.setActiveLeaderIndex(1); break;
            case "3": team.setActiveLeaderIndex(2); break;
            case "4": team.setActiveLeaderIndex(3); break;
            case "5": team.setActiveLeaderIndex(4); break;
            case "6": team.setActiveLeaderIndex(5); break;
            case "7": team.setActiveLeaderIndex(6); break;
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
            case "K":
                boolean quickSaved = fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.SaveManager.saveQuickSave(activeSlot, team, maze, currentFloor);
                if (quickSaved) {
                    menu.displayMessage("\n[Sauvegarde Rapide] Donjon et position enregistrés dans le Slot " + activeSlot + ". Retour à l'écran initial...");
                    running = false;
                } else {
                    menu.displayMessage("\n[Erreur] Échec de la Sauvegarde Rapide.");
                }
                break;
            default:
                menu.displayMessage("Commande inconnue");
        }

        if (!moved && !choice.equals("X") && "ZSQD".contains(choice)) {
            menu.displayMessage("\nTu vas dans un mur");
        }

        if (moved) {
            Cell currentCell = maze.getGrid()[team.getX()][team.getY()];
            // 1. Vérification immédiate : Si le joueur a marché sur un monstre, combat immédiat !
            boolean tookStairs = handleCellEvents(currentCell);

            // 2. Si le joueur n'a pas déclenché de combat et explore toujours, les monstres se déplacent
            if (running && !currentCell.hasMonster() && !tookStairs) {
                maze.moveMonsters(team, menu);
                // 3. Vérification Embuscade : Un monstre est-il arrivé sur la case du joueur ?
                Cell newCell = maze.getGrid()[team.getX()][team.getY()];
                handleCellEvents(newCell);
            }
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
                    fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot slot = menu.askSlotToUnequip();
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
     * @return true si l'équipe a pris un escalier, false sinon
     */
    private boolean handleCellEvents(Cell currentCell) {
        // 1. Rencontre avec un monstre
        if (currentCell.hasMonster()) {
            List<Character> monsters = currentCell.getMonsters();
            if (monsters.size() == 1) {
                menu.displayMessage("\nUN " + monsters.get(0).getName().toUpperCase() + " APPARAÎT ! BASTOOON !");
            } else {
                menu.displayMessage("\nUNE HORDE DE " + monsters.size() + " MONSTRES APPARAÎT ! BASTOOON !");
            }

            BattleController battleController = new BattleController(team, monsters, menu);
            boolean victory = battleController.start();

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
                menu.displayMessage("\nVous trouvez un escalier lugubre qui descend dans les profondeurs...");
                currentFloor++;
                menu.displayMessage("=== DESCENTE À L'ÉTAGE " + currentFloor + " ===");

                this.maze = new NaheulbeukDungeon();
                this.maze.generate();
                int[] startPos = this.maze.getFirstWalkablePosition();
                team.setX(startPos[0]);
                team.setY(startPos[1]);

                if (currentFloor == 4) {
                    menu.displayMessage("\nRanger : On est presque devant le bureau de Zangdar ! Préparez vos armes !");
                    this.maze.generateMonsters(8, team.getX(), team.getY());
                    this.maze.generateItems(3);
                    this.maze.generateStairs(1);
                } else if (currentFloor == 5) {
                    menu.displayMessage("\n=== ÉTAGE 5 : L'ANTICHAMBRE DU BUREAU DE ZANGDAR ===");
                    menu.displayMessage("Magicienne : Attention ! C'est un Golem de Fer ! C'est une machine à baffes insensible aux armes simples !");
                    menu.displayMessage("Nain : YAAAAAAAAAH ! (Il charge la hache en avant, frappe l'acier et se tord les poignets !)");
                    menu.displayMessage("Zangdar (depuis son balcon) : Insolents ! Misérables cloportes ! Vous n'emporterez jamais la statuette de Gladeulfeurh ! Golem de fer, réduis-les en bouillie !");

                    // Spawner le Boss Golem de Fer sur la case actuelle !
                    List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character> bossList = new java.util.ArrayList<>();
                    bossList.add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.boss.Golem());
                    currentCell.setMonsters(bossList);

                    BattleController bossBattle = new BattleController(team, bossList, menu);
                    boolean victory = bossBattle.start();

                    if (victory) {
                        menu.displayMessage("\nZangdar claque la porte de son bureau et hurle en s'enfuyant : 'Maudits aventuriers d'opérette ! Vous ne payez rien pour attendre, je reviendrai vous anéantir !'");
                        currentCell.getMonsters().clear();
                        running = false; // Fin de l'expédition donjon !
                    } else {
                        running = false;
                    }
                } else {
                    this.maze.generateMonsters(5 + currentFloor, team.getX(), team.getY());
                    this.maze.generateItems(3);
                    this.maze.generateStairs(1);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Tente de déplacer l'équipe vers le Nord si aucun mur ne bloque le passage.
     *
     * @return true si le mouvement a réussi, false s'il y a un mur.
     */
    public boolean tryMoveNorth() {
        int targetY = team.getY() - 1;
        if (targetY >= 0 && maze.getGrid()[team.getX()][targetY].isWalkable()) {
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
        int targetY = team.getY() + 1;
        if (targetY < maze.getHeight() && maze.getGrid()[team.getX()][targetY].isWalkable()) {
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
        int targetX = team.getX() - 1;
        if (targetX >= 0 && maze.getGrid()[targetX][team.getY()].isWalkable()) {
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
        int targetX = team.getX() + 1;
        if (targetX < maze.getWidth() && maze.getGrid()[targetX][team.getY()].isWalkable()) {
            team.moveEast();
            return true;
        }
        return false;
    }
}
