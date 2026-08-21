package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.NaheulbeukDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.TutorialDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.ISaveManager;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IMenuView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IExplorationView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.ICombatView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;
import java.util.List;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.state.GameState;

public class ExplorationController implements GameState {
    private Dungeon maze;
    private Team team;
    private boolean running;
    private int currentFloor = 1;
    private IExplorationView view;
    private IMenuView menu;
    private ICombatView combatView;
    private boolean isTutorial;
    private int activeSlot = 1;
    private boolean elfJoined = false;
    private enum SubState { EXPLORING, PAUSE_MENU, STATUS_MENU }
    private SubState subState = SubState.EXPLORING;

    private float playerX = 0f;
    private float playerZ = 0f;
    private float moveSpeed = 4.0f;

    private ISaveManager saveManager;
    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.lang.LocalizationManager locManager;
    private float moveTimer = 0.5f;
    private final float moveCooldown = 0.5f;

    private GameContext gameContext;

    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.engine.ExplorationEngine engine;

    public ExplorationController(Dungeon maze, Team team, IExplorationView view, IMenuView menu, ICombatView combatView, boolean isTutorial, GameContext gameContext, ISaveManager saveManager, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.lang.LocalizationManager locManager) {
        this(maze, team, view, menu, combatView, isTutorial, 1, gameContext, saveManager, locManager);
    }

    public ExplorationController(Dungeon maze, Team team, IExplorationView view, IMenuView menu, ICombatView combatView, boolean isTutorial, int activeSlot, GameContext gameContext, ISaveManager saveManager, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.lang.LocalizationManager locManager) {
        this.maze = maze;
        this.team = team;
        this.view = view;
        this.menu = menu;
        this.combatView = combatView;
        this.isTutorial = isTutorial;
        this.activeSlot = activeSlot;
        this.gameContext = gameContext;
        this.saveManager = saveManager;
        this.locManager = locManager;
        this.engine = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.engine.ExplorationEngine(maze, team);
    }

    public void setCurrentFloor(int floor) {
        this.currentFloor = floor;
    }

    private boolean floorIntroPlayed = false;

    @Override
    public void enter() {
        this.running = true;
        this.playerX = team.getX() + 0.5f; // start in the center of the tile
        this.playerZ = team.getY() + 0.5f;
        
        maze.updateFogOfWar((int)playerX, (int)playerZ, 3);
        view.display(maze, team, currentFloor);
        
        if (!floorIntroPlayed) {
            playFloorIntro(currentFloor);
            floorIntroPlayed = true;
        }
    }
    private void playFloorIntro(int floorNumber) {
        java.util.List<String> dialogues = maze.getFloorIntroDialogues(floorNumber);
        if (dialogues != null) {
            for (String d : dialogues) {
                menu.displayDialogue(locManager.getString(d));
            }
        }
    }

    @Override
    public void onInput(String action) {
        if (!running) return;

        if (subState == SubState.PAUSE_MENU) {
            if ("ENTER".equals(action)) {
                int selection = menu.getMenuSelection();
                if (selection == 0) { // Sac
                    gameContext.pushState(new InventoryController(team, menu, gameContext, maze));
                } else if (selection == 1) { // Magie
                    menu.displayDialogue("Pas de magie disponible.");
                } else if (selection == 2) { // CompÃƒÆ’Ã‚Â©tences
                    menu.displayDialogue("GÃƒÆ’Ã‚Â©rÃƒÆ’Ã‚Â© par l'interface dÃƒÆ’Ã‚Â©diÃƒÆ’Ã‚Â©e en combat.");
                } else if (selection == 3) { // Status
                    subState = SubState.STATUS_MENU;
                    menu.displayStatusScreen(team);
                } else if (selection == 4) { // Options
                    menu.displayDialogue("Les options sont accessibles via la touche M.");
                } else if (selection == 5) { // Sauvegarder
                    saveManager.saveQuickSave(1, team, maze, currentFloor);
                    menu.displaySaveSuccess(1);
                } else if (selection == 6) { // Quitter
                    gameContext.exitGame();
                }
            } else if ("X".equals(action) || "ECHAP".equals(action)) {
                subState = SubState.EXPLORING;
                menu.setMenuRequest(null, null);
            }
            return;
        }

        if (checkTutorialScripts(action)) {
            return;
        }

        handleMovementAction(action);
    }

    private boolean handleMovementAction(String choice) {
        switch (choice) {
            case "ENTER":
                handleInteraction();
                return false;
            case "1": team.setActiveLeaderIndex(0); return false;
            case "2": team.setActiveLeaderIndex(1); return false;
            case "3": team.setActiveLeaderIndex(2); return false;
            case "4": team.setActiveLeaderIndex(3); return false;
            case "5": team.setActiveLeaderIndex(4); return false;
            case "6": team.setActiveLeaderIndex(5); return false;
            case "7": team.setActiveLeaderIndex(6); return false;
            case "MENU_STATUS":
                menu.displayStatusScreen(team);
                return false;
            case "MENU_INVENTORY":
                gameContext.pushState(new InventoryController(team, menu, gameContext, maze));
                return false;
            case "MENU_EQUIPMENT":
                menu.displayMessage("Menu d'Equipement non implemente.");
                return false;
            case "MENU_MAGIC":
                menu.displayMessage("Menu de Magie/Competences non implemente.");
                return false;
            case "MENU_SAVE":
                handleSaveAction();
                return false;
            default:
                return false;
        }
    }

    private void handleSaveAction() {
        if (isTutorial) {
            menu.displayDialogue("\nSauvegarde Rapide impossible pendant le tutoriel ! Zangdar vous surveille...");
            return;
        }
        boolean quickSaved = saveManager.saveQuickSave(activeSlot, team, maze, currentFloor);
        if (quickSaved) {
            view.displaySaveSuccess(activeSlot);
        } else {
            view.displaySaveError();
        }
    }

    private boolean checkTutorialScripts(String choice) {
        return false;
    }

    private void handlePostMovement() {
        Cell currentCell = maze.getGrid()[team.getX()][team.getY()];
        handleCellEvents(currentCell);
    }

    private boolean handleCellEvents(Cell currentCell) {
        if (currentCell.getEvent() != null) {
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.EventResult result = currentCell.getEvent().trigger(team);
            if (result != null && result.getDialogsToDisplay() != null) {
                for (String d : result.getDialogsToDisplay()) {
                    menu.displayDialogue(locManager.getString(d));
                }
            }
        }
        
        if (currentCell.hasMonster()) {
            java.util.List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character> monsters = currentCell.getMonsters();

            gameContext.triggerBattle(monsters, () -> {
                view.displayDungeon(maze, team, currentFloor);
                currentCell.getMonsters().clear(); 
                
                if (isTutorial && currentFloor == 5) {
                    menu.displayDialogue(locManager.getString("TUTO_FLOOR_5_POST_NAIN_1"));
                    menu.displayDialogue(locManager.getString("TUTO_FLOOR_5_POST_RANGER_1"));
                }

                if (maze.isExpeditionComplete(currentFloor)) {
                    running = false; 
                }
            }, () -> {
                view.displayDungeon(maze, team, currentFloor);
                running = false; 
            }, () -> {
                currentCell.getMonsters().clear();
                view.displayDungeon(maze, team, currentFloor);
            });
        }

        if (currentCell.hasItem()) {
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item item = currentCell.getItem();
            boolean take = view.askPickupItem(item);

            if (take) {
                boolean added = team.addItem(item);
                if (added) {
                    menu.displayMessage(locManager.getString("ITEM_PICKUP", item.getName()));
                    currentCell.setItem(null); 
                } else {
                    menu.displayMessage(locManager.getString("INVENTORY_FULL"));
                }
            } else {
                menu.displayMessage(locManager.getString("ITEM_LEAVE"));
            }
        }

        if (currentCell.hasStairs()) {
            if (isTutorial) {
                if (currentFloor < 5) {
                    menu.displayMessage("\nVous prenez l'escalier pour fuir plus loin dans le cellier...");
                    currentFloor++;
                    view.displayTransitionScreen(currentFloor);
                    this.maze = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.TutorialDungeon();
                    this.maze.prepareFloor(currentFloor, team);
                    this.engine.setDungeon(this.maze);
                    this.floorIntroPlayed = true;
                    playFloorIntro(currentFloor);
                } else {
                    menu.displayMessage("\nL'air frais ! Vous avez survécu et établi un petit campement !");
                    gameContext.goToVillage();
                }
            } else {
                menu.displayMessage("\nVous trouvez un escalier lugubre qui descend dans les profondeurs...");
                currentFloor++;
                view.displayTransitionScreen(currentFloor);
                this.maze = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.NaheulbeukDungeon();
                this.maze.prepareFloor(currentFloor, team);
                this.engine.setDungeon(this.maze);
                this.floorIntroPlayed = true;
                playFloorIntro(currentFloor);
            }
            return true;
        }
        return false;
    }

    private void handleInteraction() {
        int targetX = (int) playerX;
        int targetY = (int) playerZ;
        
        if (team.getFacingDirection() == 1) targetY -= 1; // Nord
        else if (team.getFacingDirection() == 0) targetY += 1; // Sud
        else if (team.getFacingDirection() == 2) targetX -= 1; // Ouest
        else if (team.getFacingDirection() == 3) targetX += 1; // Est
        
        if (targetX >= 0 && targetX < maze.getWidth() && targetY >= 0 && targetY < maze.getHeight()) {
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell targetCell = maze.getGrid()[targetX][targetY];
            handleCellEvents(targetCell);
        }
    }

    @Override
    public void update(float deltaTime) {
        if (!running || subState != SubState.EXPLORING) return;
        
        fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.input.IInputProvider input = gameContext.getInputProvider();
        if (input != null) {
            float nextX = playerX;
            float nextZ = playerZ;
            
            if (input.isUpPressed()) nextZ -= moveSpeed * deltaTime;
            if (input.isDownPressed()) nextZ += moveSpeed * deltaTime;
            if (input.isLeftPressed()) nextX -= moveSpeed * deltaTime;
            if (input.isRightPressed()) nextX += moveSpeed * deltaTime;
            
            // Allow moving in X and Z independently if the respective target cell is walkable
            int currentGridX = (int) playerX;
            int currentGridZ = (int) playerZ;
            
            // Fix bounds and collisions for X
            float boundedNextX = Math.max(0, Math.min(nextX, maze.getWidth() - 1 + 0.99f));
            int nextGridX = (int) boundedNextX;
            if (maze.getGrid()[nextGridX][currentGridZ].isWalkable()) {
                playerX = boundedNextX;
            }
            
            // Fix bounds and collisions for Z
            float boundedNextZ = Math.max(0, Math.min(nextZ, maze.getHeight() - 1 + 0.99f));
            int nextGridZ = (int) boundedNextZ;
            if (maze.getGrid()[(int)playerX][nextGridZ].isWalkable()) {
                playerZ = boundedNextZ;
            }

            if (input.isUpPressed()) team.setFacingDirection(1);
            else if (input.isDownPressed()) team.setFacingDirection(0);
            else if (input.isLeftPressed()) team.setFacingDirection(2);
            else if (input.isRightPressed()) team.setFacingDirection(3);

            int newGridX = (int) playerX;
            int newGridZ = (int) playerZ;
            
            if (newGridX != currentGridX || newGridZ != currentGridZ) {
                team.setX(newGridX);
                team.setY(newGridZ);
                handlePostMovement();
                maze.updateFogOfWar(newGridX, newGridZ, 3);
                view.display(maze, team, currentFloor);
            }
        }
    }

    @Override
    public void exit() {
        this.running = false;
    }
    
    public float getPlayerX() { return playerX; }
    public float getPlayerZ() { return playerZ; }
}