package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.NaheulbeukDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import java.util.ArrayList;
import java.util.List;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.input.InputManager;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.Game;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.FileSaveManager;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IGameView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.renderers.*;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.settings.GameSettingsManager;
import com.badlogic.gdx.Graphics.DisplayMode;

/**
 * Moteur principal LibGDX (ApplicationAdapter).
 * S'occupe du rendu 3D, de la boucle de jeu et de la gestion de la fenetre.
 *
 * @author Hibouxe
 * @version 2.0
 */
public class HD2DGameApp extends com.badlogic.gdx.Game implements GameSettingsManager.SettingsListener {

    public enum GameState {
        EXPLORATION,
        BATTLE,
        HUB,
        VILLAGE,
        TRANSITION
    }

    private boolean showDebugStats = false;
    private GameState currentState = GameState.EXPLORATION;
    private GameState pendingState = null;
    private long transitionStartTime = 0;
    private int transitionFloor = 1;

    public long getTransitionStartTime() { return transitionStartTime; }

    public int getCurrentFloor() { return currentFloor; }

    public void setTransitionFloor(int floor) {
        this.transitionFloor = floor;
    }

    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private DecalBatch decalBatch;
    private Environment environment;
    private PointLight heroLight;

    private AssetProvider assetProvider;

    private DungeonSceneRenderer dungeonRenderer;
    private BattleArenaRenderer battleRenderer;
    private HUDRenderer hudRenderer;
    

    private InputManager inputManager;
    private Game game;
    private Dungeon maze;
    private Team team;
    private float tileSize = 5.0f; // TODO: fetch from theme
    private KeyboardLayout activeKeyboardLayout = KeyboardLayout.detectSystemLayout();

    private GameSettingsManager settingsManager;
    private ViewProvider viewProvider;

    public ViewProvider getViewProvider() {
        if (viewProvider == null) {
            viewProvider = new ViewProvider(this, hudRenderer, battleRenderer);
        }
        return viewProvider;
    }

    public HD2DGameApp(GameSettingsManager settingsManager) {
        this.settingsManager = settingsManager;
        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.audio.AudioManager.getInstance().setSettingsManager(settingsManager);
    }

    public boolean isAnyMenuOpen() {
        if (currentMenuTitle != null) return true;
        if (currentMessages != null && !currentMessages.isEmpty()) return true;
        if (hudRenderer != null) {
            return hudRenderer.isMenuOpen() || hudRenderer.isSettingsMenuOpen();
        }
        return false;
    }
    public void pushInput(String input) {
        if (game != null) game.onInput(input);
    }
    
    public boolean onInput(String action) {
        if (currentMessages != null && !currentMessages.isEmpty()) {
            if ("ENTER".equals(action) || "SPACE".equals(action) || "X".equals(action)) {
                currentMessages.remove(0);
                return true;
            }
            return true;
        }

        if (currentMenuTitle != null && ("PAUSE".equals(currentMenuTitle) || "STATISTIQUES".equals(currentMenuTitle) 
|| "INVENTAIRE".equals(currentMenuTitle) || "CIBLE_OBJET".equals(currentMenuTitle) || 
"CATEGORIES".equals(currentMenuTitle) || "OBJETS".equals(currentMenuTitle))) {
            if (hudRenderer != null) {
                boolean consumed = hudRenderer.onInput(action, this);
                if (!consumed) {
                    if ("ZSQD".contains(action) || "UP".equals(action) || "DOWN".equals(action)) {
                        return true; // Bloquer les mouvements d'exploration
                    }
                    return false; // Laisse le Controller gerer
                }
                return true;
            }
        }

        if (hudRenderer != null) {
            return hudRenderer.onInput(action, this);
        }
        return false;
    }

    public int getMenuSelection() { return hudRenderer != null ? hudRenderer.getContextMenuSelection() : 0; }
    public void resetMenuSelection() { if (hudRenderer != null) hudRenderer.resetContextMenuSelection(); }

    
    private float transitionPlayerX;
    private float transitionPlayerZ;

    public void setState(GameState state) {
        if (state == GameState.TRANSITION) {
            this.currentState = state;
            this.transitionStartTime = System.currentTimeMillis();
            // transitionPlayerX and transitionPlayerZ are already kept continuously updated by the render loop when exploring
        } else if (this.currentState == GameState.TRANSITION) {
            this.pendingState = state;
        } else {
            if (this.currentState == GameState.BATTLE && state == GameState.EXPLORATION) {
                cameraNeedsSnap = true;
            }
            this.currentState = state;
        }
    }

    public void setDependencies(InputManager inputManager, Game game) {
        this.inputManager = inputManager;
        this.game = game;
    }

    @Override
    public void create() {
        this.settingsManager.addListener(this);
        applyDisplaySettings();
        
        Gdx.input.setCatchKey(com.badlogic.gdx.Input.Keys.ESCAPE, true);
        
        camera = new PerspectiveCamera(45, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 0.1f;
        camera.far = 500f;

        environment = new Environment();
        // Ambient light is updated per-state in render()
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.5f, 1f));
        
        // A global directional light (moonlight/sunlight)
        environment.add(new DirectionalLight().set(0.15f, 0.15f, 0.2f, -1f, -0.8f, -0.5f));

        heroLight = new PointLight().set(1.0f, 0.7f, 0.4f, 0f, 2f, 0f, 30f);
        environment.add(heroLight);

        modelBatch = new ModelBatch();
        decalBatch = new DecalBatch(new CameraGroupStrategy(camera));

        assetProvider = new AssetProvider();
        dungeonRenderer = new DungeonSceneRenderer(assetProvider, "data/themes/theme_naheulbeuk.json");
        battleRenderer = new BattleArenaRenderer(this.assetProvider);
        hudRenderer = new HUDRenderer(this.settingsManager);
        
    }

    private boolean sceneNeedsBuild = false;
    private boolean cameraNeedsSnap = true;
    private int currentLeaderIndex = -1;
    private java.util.List<String> currentMessages = new java.util.ArrayList<>();
    public String currentMenuTitle = null;
    public String[] currentMenuOptions = null;
    
    private long lastMoveTime = 0;

    public void setupBattle(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team, java.util.List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character> monsters) {
        if (battleRenderer != null) {
            battleRenderer.setupTeamBattleArena(team);
            battleRenderer.setDungeonTheme("naheulbeuk");
            if (maze != null) battleRenderer.setDungeonTheme("naheulbeuk");
        }
    }

    public void setMenuRequest(String title, String[] options) {
        this.currentMenuTitle = title;
        this.currentMenuOptions = options;
    }



    public void setMessages(java.util.List<String> messages) {
        this.currentMessages = messages;
    }

    private int currentFloor = 1;

    private Dungeon pendingMaze;
    private Team pendingTeam;
    private int pendingFloor = -1;
    private boolean hasRenderedBlackScreen = false;

    public void setContext(Dungeon maze, Team team, int currentFloor) {
        if (this.currentState == GameState.TRANSITION && System.currentTimeMillis() - transitionStartTime < 1000) {
            this.pendingMaze = maze;
            this.pendingTeam = team;
            this.pendingFloor = currentFloor;
        } else {
            applyContext(maze, team, currentFloor);
        }
    }

    private void applyContext(Dungeon maze, Team team, int currentFloor) {
        if (this.maze != maze) {
            this.cameraNeedsSnap = true;
            if (team != null) {
                transitionPlayerX = team.getX() + 0.5f;
                transitionPlayerZ = team.getY() + 0.5f;
            }
            
            // Changer le theme dynamiquement en fonction du donjon
            String themePath = "data/themes/theme_naheulbeuk.json";
            if (maze instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.HubDungeon) {
                themePath = "data/themes/theme_hub.json";
            } else if (maze instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.TutorialDungeon) {
                themePath = "data/themes/theme_naheulbeuk.json"; // Le tuto utilise Naheulbeuk pour l'instant
            }
            
            if (dungeonRenderer != null) {
                dungeonRenderer.dispose();
                dungeonRenderer = new DungeonSceneRenderer(assetProvider, themePath);
            }
        }
        this.sceneNeedsBuild = true; // Toujours reconstruire la scene
        this.maze = maze;
        this.team = team;
        this.currentFloor = currentFloor;
    }

    @Override
    public void resize(int width, int height) {
        if (camera != null) {
            camera.viewportWidth = width;
            camera.viewportHeight = height;
            camera.update();
        }
        if (hudRenderer != null) {
            hudRenderer.resize(width, height);
        }
    }

    @Override
    public void render() {
        try {
            doRender();
        } catch (Throwable e) {
            try {
                java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter("crash_render.log"));
                e.printStackTrace(pw);
                pw.close();
            } catch (Exception ex) {}
            throw e; // still throw so the game exits, but log is saved
        }
    }
    
    private void doRender() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3) || Gdx.input.isKeyJustPressed(Input.Keys.GRAVE)) {
            showDebugStats = !showDebugStats;
        }

        if (inputManager != null) {
            inputManager.update();
        }
        if (game != null) {
            game.update(Gdx.graphics.getDeltaTime());
        }

        if (team != null && dungeonRenderer != null) {
            if (team.getActiveLeaderIndex() != currentLeaderIndex) {
                currentLeaderIndex = team.getActiveLeaderIndex();
                fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character leader = team.getActiveLeader();
                if (leader != null) {
                    dungeonRenderer.setLeaderClass(leader.getClass().getSimpleName());
                    sceneNeedsBuild = true; // Reconstruire la scene
                }
            }
        }



        // handleInput(); // We removed the local handleInput since InputManager handles it now

        if (currentState == GameState.TRANSITION) {
            if (pendingMaze != null && System.currentTimeMillis() - transitionStartTime >= 1000) {
                if (!hasRenderedBlackScreen) {
                    hasRenderedBlackScreen = true;
                } else {
                    applyContext(pendingMaze, pendingTeam, pendingFloor);
                    pendingMaze = null;
                    pendingTeam = null;
                    hasRenderedBlackScreen = false;
                }
            }

            if (System.currentTimeMillis() - transitionStartTime > 1500) {
                currentState = pendingState != null ? pendingState : GameState.EXPLORATION;
                pendingState = null;
                cameraNeedsSnap = true;
            }
        }

        if (sceneNeedsBuild && dungeonRenderer != null && maze != null && team != null) {
            dungeonRenderer.buildScene(maze, team, team.getX(), team.getY(), currentFloor);
            sceneNeedsBuild = false;
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if ((currentState == GameState.EXPLORATION || currentState == GameState.TRANSITION) && team != null) {
            float playerX = team.getX();
            float playerZ = team.getY();
            if (currentState == GameState.TRANSITION) {
                playerX = transitionPlayerX;
                playerZ = transitionPlayerZ;
            } else if (game != null && game.getCurrentState() instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.ExplorationController) {
                fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.ExplorationController ec = (fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.ExplorationController) game.getCurrentState();
                playerX = ec.getPlayerX();
                playerZ = ec.getPlayerZ();
                transitionPlayerX = playerX; // Cache float coordinates smoothly every frame
                transitionPlayerZ = playerZ;
            }

            
            
            if (cameraNeedsSnap && dungeonRenderer != null && team != null) {
                float startWorldX = team.getX() * tileSize + (tileSize / 2f);
                float startWorldZ = team.getY() * tileSize + (tileSize / 2f);
                camera.position.set(startWorldX, 7f, startWorldZ + 6.0f);
                camera.lookAt(startWorldX, 0.5f, startWorldZ);
                camera.update();
                cameraNeedsSnap = false;
            }

            float targetWorldX = playerX * tileSize;
            float targetWorldZ = playerZ * tileSize;

            // Restauration fluide de la position et hauteur de camera
            float camSpeed = 12.0f * Gdx.graphics.getDeltaTime();
            camera.position.x += (targetWorldX - camera.position.x) * 0.1f;
            camera.position.y += (10f - camera.position.y) * 0.1f;
            camera.position.z += ((targetWorldZ + 9.0f) - camera.position.z) * 0.1f;
            camera.lookAt(camera.position.x, 0.0f, camera.position.z - 9.0f);
            camera.update();

            // Update lighting for Dungeon
            environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.05f, 0.05f, 0.08f, 1f));
            heroLight.intensity = 25f + (float)(Math.random() * 2f); // Flicker effect
            heroLight.setPosition(camera.position.x, camera.position.y, camera.position.z);

            dungeonRenderer.render(modelBatch, decalBatch, environment, camera, playerX, playerZ, team.getFacingDirection(), maze != null ? maze.getRoamingMonsters() : new java.util.ArrayList<>());
        } else if (currentState == GameState.BATTLE) {
            // Update lighting for Battle (brighter)
            environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.5f, 1f));
            heroLight.intensity = 0f;

            battleRenderer.render(modelBatch, decalBatch, environment, camera);
        }

        // Rendu 2D de l'Interface HUD & Minimap en superposition
        float hudPX = team != null ? team.getX() : 0;
        float hudPZ = team != null ? team.getY() : 0;
        if (currentState == GameState.TRANSITION) {
            hudPX = transitionPlayerX;
            hudPZ = transitionPlayerZ;
        } else if (game != null && game.getCurrentState() instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.ExplorationController) {
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.ExplorationController ec = (fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.ExplorationController) game.getCurrentState();
            hudPX = ec.getPlayerX();
            hudPZ = ec.getPlayerZ();
        }
        hudRenderer.renderHUD(maze, hudPX, hudPZ, currentFloor, currentState, this.team, currentMessages, currentMenuTitle, currentMenuOptions, this);
        if (currentState == GameState.TRANSITION && hudRenderer != null) {
            hudRenderer.renderTransitionScreen(transitionFloor, transitionStartTime);
        }
        if (showDebugStats && hudRenderer.uiBatch != null && hudRenderer.font != null) {
            hudRenderer.uiBatch.begin();
            hudRenderer.font.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
            hudRenderer.font.draw(hudRenderer.uiBatch, "DEBUG MODE (F3) - FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 710);
            hudRenderer.font.draw(hudRenderer.uiBatch, "DEBUG CAM: " + camera.position.toString(), 20, 680);
            hudRenderer.font.draw(hudRenderer.uiBatch, "DEBUG INSTANCES: " + (dungeonRenderer != null ? dungeonRenderer.getInstancesCount() : 0), 20, 650);
            hudRenderer.font.draw(hudRenderer.uiBatch, "DEBUG PLAYER: X=" + (team != null ? team.getX() : 0) + " Z=" + (team != null ? team.getY() : 0), 20, 620);
            hudRenderer.uiBatch.end();
        }
    }

    // handleInput removed as InputManager handles it

    @Override
    public void dispose() {
        if (modelBatch != null) modelBatch.dispose();
        if (decalBatch != null) decalBatch.dispose();
        if (dungeonRenderer != null) dungeonRenderer.dispose();
        if (battleRenderer != null) battleRenderer.dispose();
        if (hudRenderer != null) hudRenderer.dispose();
        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.audio.AudioManager.getInstance().dispose();
        if (assetProvider != null) assetProvider.dispose();
        
        Gdx.app.exit(); // Fermeture propre de LibGDX (au lieu de System.exit(0))
    }

    // --- SettingsListener Implementation ---

    @Override
    public void onDisplaySettingsChanged() {
        applyDisplaySettings();
    }

    private void applyDisplaySettings() {
        GameSettingsManager config = this.settingsManager;
        boolean isNowFullscreen = Gdx.graphics.isFullscreen();
        
        if (config.isFullscreen() && !isNowFullscreen) {
            DisplayMode mode = Gdx.graphics.getDisplayMode();
            Gdx.graphics.setFullscreenMode(mode);
        } else if (!config.isFullscreen() && isNowFullscreen) {
            Gdx.graphics.setWindowedMode(1280, 720);
        }
        
        Gdx.graphics.setVSync(config.isVsync());
    }

    @Override
    public void onAudioSettingsChanged() {
        // Sera implemente
    }

    @Override
    public void onGameplaySettingsChanged() {
        // Le Gameplay est lu directement par le controleur
    }

    // --- IGameView Implementation ---
    public void clearMessages() { currentMessages.clear(); }
    public void displayMessage(String message) { 
        if (message != null && !message.trim().isEmpty()) {
            for (String line : message.split("\n")) {
                if (!line.trim().isEmpty()) currentMessages.add(line);
            }
        }
    }
    public void displayDialogue(String message) { displayMessage(message); }
    public int askPlayerInt() { return 1; }
    public String askPlayerString() { return ""; }
    public void displayTitleScreen() {}
    public int askMainMenuChoice() { return 1; }
    public int askHubChoice() { return 1; }
    public boolean askLoadQuickSavePrompt() { return false; }
    public boolean askLoadQuickSavePrompt(int slot, String summary) { return false; }
    public boolean askConfirmAbandonQuickSave() { return false; }
    public int askSlotChoice(String actionTitle, String[] slotSummaries) { return 1; }
    public int askSlotManagementAction() { return 0; }
    public int askTargetCopySlot(int sourceSlot, String[] slotSummaries) { return 0; }
    public void displayTransitionScreen(int floorNumber) { 
        setState(GameState.TRANSITION);
        setTransitionFloor(floorNumber);
    }
    public void display(Dungeon maze, Team team, int currentFloor) { setContext(maze, team, currentFloor); }
    public void displayDungeon(Dungeon maze, Team team, int currentFloor) { setContext(maze, team, currentFloor); }
    public String askPlayerMovement() { return ""; }
    public boolean askPickupItem(Item item) { return true; }
    public void displayInventory(Team team) {
        String[] options = new String[team.getInventory().size() + 1];
        for(int i=0; i<team.getInventory().size(); i++) {
            options[i] = team.getInventory().get(i).getName();
        }
        options[options.length-1] = "Retour";
        setMenuRequest("INVENTAIRE", options);
    }
    public int askInventoryMenuChoice() { return 3; }
    public void displayStatusScreen(Team team) {
        String[] options = new String[team.getMembers().size() + 1];
        for (int i = 0; i < team.getMembers().size(); i++) {
            options[i] = team.getMembers().get(i).getName();
        }
        options[options.length - 1] = "Retour";
        setMenuRequest("STATISTIQUES", options);
    }
    public EquipmentSlot askSlotToUnequip() { return null; }
    public boolean askUseItem() { return false; }
    public int askItemIndex() { return -1; }
    public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character askItemTarget(Team team) { return null; }
    public void displayBattleStatus(java.util.List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character> monsters, Team team) { setupBattle(team, monsters); setState(GameState.BATTLE); }
    public int askBattleAction(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character attacker) { return 1; }
    public void showActionMenu(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character combatant, java.util.List<String> actions) {
        setMenuRequest("Action: " + combatant.getName(), actions.toArray(new String[0]));
    }
    public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill askSkill(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character attacker, java.util.List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character> monsters) { return null; }
    public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character askMonsterTarget(java.util.List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character> monsters) { return monsters.isEmpty() ? null : monsters.get(0); }
    public void displayTurn(String characterName) {
        if (hudRenderer != null) {
            hudRenderer.showFloatingMessage("Tour de " + characterName + " !", 1.5f);
        }
    }
    public void displayVictory() { displayMessage("Victoire !"); }
    public void displayDefeat() { displayMessage("Defaite !"); }
    public void displaySaveSuccess(int slot) { displayMessage("Sauvegarde effectuee sur le slot " + slot); }
    public void displaySaveError() { displayMessage("Erreur save"); }
}