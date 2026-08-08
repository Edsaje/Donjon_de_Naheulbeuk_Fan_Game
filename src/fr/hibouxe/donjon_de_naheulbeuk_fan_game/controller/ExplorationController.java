package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.NaheulbeukDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.TutorialDungeon;
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
    private boolean elfJoined = false;
    private boolean elfHealed = false;

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

    private boolean floorIntroPlayed = false;

    /**
     * Démarre la boucle d'exploration.
     */
    public void start() {
        this.running = true;
        this.floorIntroPlayed = false; // Reset pour le début du donjon
        while (running) {
            maze.updateFogOfWar(team.getX(), team.getY(), 3); // Vision radius: 3 cases
            menu.display(maze, team, currentFloor);
            
            if (!floorIntroPlayed) {
                java.util.List<String> dialogues = maze.getIntroDialogues(currentFloor);
                if (dialogues != null) {
                    for (String d : dialogues) {
                        menu.displayDialogue(d);
                    }
                    menu.clearMessages();
                }
                floorIntroPlayed = true;
            }
            
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

        // --- SCRIPT ELFE ---
        if (isTutorial && currentFloor == 2 && elfJoined && !elfHealed) {
            if ("ZSQD".contains(choice) || choice.equals("ENTER")) {
                if (choice.equals("ENTER")) {
                    handleInteraction();
                } else {
                    menu.displayMessage("\nL'Elfe est trop blessée pour avancer. Appuyez sur ECHAP pour ouvrir le menu, allez dans SAC, et utilisez la Potion de Soin sur l'Elfe.");
                }
                return;
            }
        }

        switch (choice) {
            case "ENTER":
                handleInteraction();
                break;
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
                                menu.displayMessage("\n" + target.getName() + " utilise ou s'équipe de " + selectedItem.getName() + " !"); if (isTutorial && currentFloor == 2 && elfJoined && !elfHealed && target.getClass().getSimpleName().equals("Elf")) { elfHealed = true; menu.displayDialogue("Elfe : *tousse* Berk ! Ça a un goût de jus de chaussette ! Mais je me sens mieux."); menu.displayMessage("\n[L'Elfe est soignée, le passage est libre !]"); }
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
                if (maze.isExpeditionComplete(currentFloor)) {
                    menu.displayMessage("\nZangdar claque la porte de son bureau et hurle en s'enfuyant : 'Maudits aventuriers d'opérette ! Vous ne payez rien pour attendre, je reviendrai vous anéantir !'");
                    running = false; // Fin de l'expédition donjon !
                }
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
                if (currentFloor < 5) {
                    menu.displayMessage("\nVous prenez l'escalier pour fuir plus loin dans le cellier...");
                    currentFloor++;
                    menu.displayTransitionScreen(currentFloor);
                    this.maze = new TutorialDungeon();
                    this.maze.prepareFloor(currentFloor, team);
                    this.floorIntroPlayed = false;
                } else {
                    running = false; // Fin des 5 étages du tutoriel
                }
            } else {
                menu.displayMessage("\nVous trouvez un escalier lugubre qui descend dans les profondeurs...");
                currentFloor++;
                menu.displayTransitionScreen(currentFloor);
                this.maze = new NaheulbeukDungeon();
                this.maze.prepareFloor(currentFloor, team);
                this.floorIntroPlayed = false;
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
        
        // --- SCRIPT ELFE (Tutoriel - Étage 2) ---
        if (isTutorial && currentFloor == 2 && team.getX() == 0 && targetY == 2 && !elfJoined) {
            menu.displayMessage("\nL'Elfe inconsciente bloque le passage. Appuyez sur ESPACE pour interagir avec elle.");
            return false;
        }

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

    private void handleInteraction() {
        if (isTutorial && currentFloor == 2 && !elfHealed) {
            int targetX = team.getX();
            int targetY = team.getY();
            
            if (team.getFacingDirection() == 1) targetY -= 1; // Nord
            else if (team.getFacingDirection() == 0) targetY += 1; // Sud
            else if (team.getFacingDirection() == 2) targetX -= 1; // Ouest
            else if (team.getFacingDirection() == 3) targetX += 1; // Est
            
            if (targetX == 0 && targetY == 2) {
                interactWithElf();
            } else if (elfJoined && !elfHealed) {
                menu.displayMessage("\nL'Elfe est trop blessée pour avancer. Appuyez sur ECHAP pour ouvrir le menu, allez dans SAC, et utilisez la Potion de Soin sur l'Elfe.");
            }
        }
    }
    
    private void interactWithElf() {
        if (!elfJoined) {
            menu.displayDialogue("Ranger : Hé l'Elfe, lève-toi, on doit sortir d'ici.");
            menu.displayDialogue("Elfe : *gémissement* J'ai trop mal à la tête... je peux à peine marcher.");
            menu.displayDialogue("Ranger : Bon, rejoins le groupe, mais il va falloir te rafistoler avant qu'on bouge d'ici.");
            
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Elf elfe = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Elf();
            elfe.setHealthPoint(1);
            team.getMembers().add(elfe);
            elfJoined = true;
            menu.displayMessage("\n[L'Elfe a rejoint le groupe, mais elle est gravement blessée !]");
        } else if (!elfHealed) {
            menu.displayDialogue("Ranger : Tu vas pas avancer dans cet état. Je dois te donner une Potion de Soin.");
            menu.displayMessage("\nAppuyez sur ECHAP pour ouvrir le menu, allez dans SAC, et utilisez la Potion de Soin sur l'Elfe.");
        }
    }
}
