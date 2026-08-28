package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.input.InputListener;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.state.GameState;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.NaheulbeukDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.TutorialDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.SaveData;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.ISaveManager;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IGameView;

public class Game implements InputListener, GameContext {
    private GameState currentState;
    private IGameView app;
    private Team team;
    private int currentSlot = 1;
    private ISaveManager saveManager;
    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository monsterRepository;
    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.input.IInputProvider inputProvider;

    public Game(IGameView app, ISaveManager saveManager, fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.input.IInputProvider inputProvider, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository monsterRepository) {
        this.app = app;
        this.saveManager = saveManager;
        this.inputProvider = inputProvider;
        this.monsterRepository = monsterRepository;
        initMainMenu();
    }
    
    @Override
    public fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.input.IInputProvider getInputProvider() {
        return inputProvider;
    }
    private java.util.Stack<GameState> stateStack = new java.util.Stack<>();

    @Override
    public void pushState(GameState newState) {
        if (currentState != null) {
            stateStack.push(currentState);
        }
        currentState = newState;
        if (currentState != null) {
            currentState.enter();
        }
    }

    @Override
    public void popState() {
        if (currentState != null) {
            currentState.exit();
        }
        if (!stateStack.isEmpty()) {
            currentState = stateStack.pop();
        } else {
            currentState = null;
        }
    }

    public void changeState(GameState newState) {
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        if (currentState != null) {
            currentState.enter();
        }
    }

    public GameState getCurrentState() {
        return currentState;
    }

    private void initMainMenu() {
        if (saveManager.hasQuickSave(currentSlot)) {
            changeState(new QuickSavePromptState());
        } else {
            changeState(new MainMenuState());
        }
    }

    public void update(float deltaTime) {
        if (currentState != null) {
            currentState.update(deltaTime);
        }
    }

    @Override
    public void onInput(String action) {
        if (app.onInput(action)) return;
        if (currentState != null) {
            currentState.onInput(action);
        }
    }

    private void loadGame(int slot) {
        this.currentSlot = slot;
        if (saveManager.hasQuickSave(slot)) {
            SaveData data = saveManager.loadQuickSave(slot);
            if (data != null) {
                this.team = data.getTeam();
                ExplorationController ec = new ExplorationController(data.getDungeon(), team, app.getExplorationView(), app.getMenuView(), false, currentSlot, this, saveManager, new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.lang.LocalizationManager());
                app.switchToExplorationView();
                app.getMenuView().setMenuRequest(null, null);
                changeState(ec);
            } else {
                changeState(new MainMenuState());
            }
        } else if (saveManager.hasHubSave(slot)) {
            SaveData data = saveManager.loadHubSave(slot);
            if (data != null) {
                this.team = data.getTeam();
                goToVillage();
            } else {
                changeState(new MainMenuState());
            }
        } else {
            changeState(new MainMenuState());
        }
    }

    public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository getMonsterRepository() {
        return monsterRepository;
    }
    @Override public boolean isInputBlocked() { return app != null && app.isAnyMenuOpen(); }

    private void runTutorial() {
        this.team = new Team();
        this.team.getMembers().clear();
        this.team.getMembers().add(new Ranger());

        TutorialDungeon tutorialMaze = new TutorialDungeon();
        tutorialMaze.prepareFloor(1, team, monsterRepository);

        ExplorationController ec = new ExplorationController(tutorialMaze, team, app.getExplorationView(), app.getMenuView(), true, currentSlot, this, saveManager, new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.lang.LocalizationManager());
        app.displayTransitionScreen(1);
        app.getMenuView().setMenuRequest(null, null);
        changeState(ec);
    }

    private GameState suspendedExplorationState;

    @Override
    public void resumeExploration() {
        if (suspendedExplorationState != null) {
            changeState(suspendedExplorationState);
            app.switchToExplorationView();
            app.getMenuView().setMenuRequest(null, null);
            suspendedExplorationState = null;
        } else {
            changeState(new MainMenuState());
        }
    }

    @Override
    public void goToMainMenu() {
        changeState(new MainMenuState());
    }

    @Override
    public void goToVillage() {
        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.HubDungeon hub = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.HubDungeon();
        hub.prepareFloor(0, team, monsterRepository);
        ExplorationController ec = new ExplorationController(hub, team, app.getExplorationView(), app.getMenuView(), false, currentSlot, this, saveManager, new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.lang.LocalizationManager());
        app.displayTransitionScreen(0);
        app.getMenuView().setMenuRequest(null, null);
        changeState(ec);
    }

    @Override
    public void exitGame() {
        com.badlogic.gdx.Gdx.app.exit();
    }

    @Override
    public void triggerBattle(java.util.List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character> monsters, Runnable onVictory, Runnable onDefeat, Runnable onFlee) {
        if (currentState instanceof ExplorationController) {
            suspendedExplorationState = currentState;
        }
        app.displayTransitionScreen(-1); // Combat transition without floor text
        BattleController bc = new BattleController(team, monsters, app.getCombatView());
        bc.setCallbacks(
            () -> { if (onVictory != null) onVictory.run(); resumeExploration(); },
            () -> { if (onDefeat != null) onDefeat.run(); resumeExploration(); },
            () -> { if (onFlee != null) onFlee.run(); resumeExploration(); }
        );
        changeState(bc);
    }// --- Inner States ---
    private class MainMenuState implements GameState {
        @Override public void enter() {
            app.switchToHubView();
            app.getMenuView().setMenuRequest("Menu Principal", new String[]{"Nouvelle Partie", "Charger Partie", "Gérer Sauvegardes", "[TEST] Campement", "Quitter"});
        }
        @Override public void update(float deltaTime) {}
        @Override public void onInput(String action) {
            if ("ENTER".equals(action)) {
                int choice = app.getMenuView().getMenuSelection();
                app.getMenuView().resetMenuSelection();
                if (choice == 0) changeState(new NewGameMenuState());
                else if (choice == 1) changeState(new LoadMenuState());
                else if (choice == 2) changeState(new ManageSavesMenuState());
                else if (choice == 3) {
                    try {
                        team = new Team();
                        team.getMembers().add(new Ranger());
                        goToVillage();
                    } catch (Throwable e) {
                        try {
                            java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter("crash.log"));
                            e.printStackTrace(pw);
                            pw.close();
                        } catch (Exception ex) {}
                        throw e;
                    }
                }
                else if (choice == 4) com.badlogic.gdx.Gdx.app.exit();
            }
        }
        @Override public void exit() {}
    }

    private class NewGameMenuState implements GameState {
        @Override public void enter() {
            String[] options = new String[4];
            for (int i = 1; i <= 3; i++) {
                options[i-1] = saveManager.getSlotSummary(i);
            }
            options[3] = "Retour";
            app.getMenuView().setMenuRequest("Choisir un Slot", options);
        }
        @Override public void update(float deltaTime) {}
        @Override public void onInput(String action) {
            if ("ENTER".equals(action)) {
                int choice = app.getMenuView().getMenuSelection();
                app.getMenuView().resetMenuSelection();
                if (choice >= 0 && choice < 3) {
                    currentSlot = choice + 1;
                    saveManager.deleteSlot(currentSlot);
                    runTutorial();
                } else if (choice == 3) {
                    changeState(new MainMenuState());
                }
            }
        }
        @Override public void exit() {}
    }

    private class LoadMenuState implements GameState {
        @Override public void enter() {
            String[] options = new String[4];
            for (int i = 1; i <= 3; i++) {
                options[i-1] = saveManager.getSlotSummary(i);
            }
            options[3] = "Retour";
            app.getMenuView().setMenuRequest("Charger Partie", options);
        }
        @Override public void update(float deltaTime) {}
        @Override public void onInput(String action) {
            if ("ENTER".equals(action)) {
                int choice = app.getMenuView().getMenuSelection();
                app.getMenuView().resetMenuSelection();
                if (choice >= 0 && choice < 3) {
                    int slot = choice + 1;
                    loadGame(slot);
                } else if (choice == 3) {
                    changeState(new MainMenuState());
                }
            }
        }
        @Override public void exit() {}
    }

    private class ManageSavesMenuState implements GameState {
        @Override public void enter() {
            String[] options = new String[4];
            for (int i = 1; i <= 3; i++) {
                options[i-1] = saveManager.getSlotSummary(i);
            }
            options[3] = "Retour";
            app.getMenuView().setMenuRequest("Gérer Sauvegardes", options);
        }
        @Override public void update(float deltaTime) {}
        @Override public void onInput(String action) {
            if ("ENTER".equals(action)) {
                int choice = app.getMenuView().getMenuSelection();
                app.getMenuView().resetMenuSelection();
                if (choice >= 0 && choice < 3) {
                    int slot = choice + 1;
                    saveManager.deleteSlot(slot);
                    changeState(new ManageSavesMenuState());
                } else if (choice == 3) {
                    changeState(new MainMenuState());
                }
            }
        }
        @Override public void exit() {}
    }

    private class QuickSavePromptState implements GameState {
        @Override public void enter() {
            app.switchToHubView();
            app.getMenuView().setMenuRequest("Reprendre la partie rapide ?", new String[]{"Oui", "Non"});
        }
        @Override public void update(float deltaTime) {}
        @Override public void onInput(String action) {
            if ("ENTER".equals(action)) {
                int choice = app.getMenuView().getMenuSelection();
                app.getMenuView().resetMenuSelection();
                if (choice == 0) {
                    loadGame(currentSlot);
                } else {
                    saveManager.deleteQuickSave(currentSlot);
                    changeState(new MainMenuState());
                }
            }
        }
        @Override public void exit() {}
    }
    @Override
    public void startDungeon(String dungeonId) {
        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon maze;
        if ("TUTORIAL".equals(dungeonId)) {
            maze = new TutorialDungeon();
        } else {
            maze = new NaheulbeukDungeon();
        }
        maze.prepareFloor(1, team, monsterRepository);
        boolean isTuto = "TUTORIAL".equals(dungeonId);
        ExplorationController ec = new ExplorationController(maze, team, app.getExplorationView(), app.getMenuView(), isTuto, currentSlot, this, saveManager, new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.lang.LocalizationManager());
        app.displayTransitionScreen(1);
        app.getMenuView().setMenuRequest(null, null);
        changeState(ec);
    }
}



