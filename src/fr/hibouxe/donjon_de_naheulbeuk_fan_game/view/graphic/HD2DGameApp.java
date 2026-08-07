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

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.NaheulbeukDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import java.util.ArrayList;
import java.util.List;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.renderers.*;

/**
 * Moteur principal LibGDX (ApplicationAdapter).
 * S'occupe du rendu 3D, de la boucle de jeu et de la gestion de la fenǦtre.
 *
 * @author Hibouxe
 * @version 2.0
 */
public class HD2DGameApp extends ApplicationAdapter {

    public enum GameState {
        EXPLORATION,
        BATTLE,
        HUB
    }

    private GameState currentState = GameState.EXPLORATION;

    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private DecalBatch decalBatch;
    private Environment environment;

    private DungeonSceneRenderer dungeonRenderer;
    private BattleArenaRenderer battleRenderer;
    private HUDRenderer hudRenderer;

    public GraphicHD2DView parentView;
    private Dungeon maze;
    private Team team;
    private float tileSize = 2.0f;
    private KeyboardLayout activeKeyboardLayout = KeyboardLayout.detectSystemLayout();

    public HD2DGameApp(GraphicHD2DView parentView) {
        this.parentView = parentView;
    }
    
    public void setState(GameState state) {
        this.currentState = state;
    }

    @Override
    public void create() {
        camera = new PerspectiveCamera(45, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 0.1f;
        camera.far = 500f;

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.5f, 1f));
        environment.add(new DirectionalLight().set(0.85f, 0.75f, 0.6f, -1f, -0.8f, -0.5f));

        modelBatch = new ModelBatch();
        decalBatch = new DecalBatch(new CameraGroupStrategy(camera));

        dungeonRenderer = new DungeonSceneRenderer();
        battleRenderer = new BattleArenaRenderer();
        hudRenderer = new HUDRenderer();
    }

    private boolean sceneNeedsBuild = false;
    private java.util.List<String> currentMessages = new java.util.ArrayList<>();
    public String currentMenuTitle = null;
    public String[] currentMenuOptions = null;

    public void setupBattle(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team, java.util.List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character> monsters) {
        if (battleRenderer != null) {
            battleRenderer.setupTeamBattleArena(team);
        }
    }

    public void setMenuRequest(String title, String[] options) {
        this.currentMenuTitle = title;
        this.currentMenuOptions = options;
    }

    public void setMessages(java.util.List<String> messages) {
        this.currentMessages = messages;
    }

    public void setContext(Dungeon maze, Team team) {
        this.sceneNeedsBuild = true; // Toujours reconstruire la scène pour mettre à jour les entités (morts, coffres)
        this.maze = maze;
        this.team = team;
    }

    @Override
    public void render() {
        if (sceneNeedsBuild && dungeonRenderer != null && maze != null && team != null) {
            float startWorldX = team.getX() * tileSize;
            float startWorldZ = team.getY() * tileSize;
            camera.position.set(startWorldX, 14f, startWorldZ + 12f);
            camera.lookAt(startWorldX, 0f, startWorldZ);
            camera.update();
            dungeonRenderer.buildScene(maze, team.getX(), team.getY());
            sceneNeedsBuild = false;
        }

        handleInput();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (currentState == GameState.EXPLORATION && team != null) {
            float targetWorldX = team.getX() * tileSize;
            float targetWorldZ = team.getY() * tileSize;

            // Restauration fluide de la position et hauteur de caméra d'exploration (Y = 14.0m)
            camera.position.x += (targetWorldX - camera.position.x) * 0.1f;
            camera.position.y += (14.0f - camera.position.y) * 0.1f;
            camera.position.z += ((targetWorldZ + 12f) - camera.position.z) * 0.1f;
            camera.lookAt(camera.position.x, 0.0f, camera.position.z - 12f);
            camera.update();

            dungeonRenderer.render(modelBatch, decalBatch, environment, camera, team.getX(), team.getY());
        } else {
            battleRenderer.render(modelBatch, decalBatch, environment, camera);
        }

        // Rendu 2D de l'Interface HUD & Minimap en superposition
        hudRenderer.renderHUD(maze, (team != null ? team.getX() : 0), (team != null ? team.getY() : 0), 1, currentState, this.team, currentMessages, currentMenuTitle, currentMenuOptions, this);
    }

    private void handleInput() {
        if (hudRenderer != null && hudRenderer.isMenuOpen()) {
            return; // En pause tant que le ConsoleMenu Dragon Quest est ouvert
        }
        if (currentMenuTitle != null) {
            return; // En attente du choix dans le ConsoleMenu contextuel (géré par HUDRenderer)
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            currentState = (currentState == GameState.EXPLORATION) ? GameState.BATTLE : GameState.EXPLORATION;
            System.out.println("=== BASCULE EN MODE : " + currentState + " ===");
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.Z) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            parentView.pushInput("Z");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            parentView.pushInput("S");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.Q) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            parentView.pushInput("Q");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            parentView.pushInput("D");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1)) {
            parentView.pushInput("1");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)) {
            parentView.pushInput("2");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3)) {
            parentView.pushInput("3");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_4)) {
            parentView.pushInput("4");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_5)) {
            parentView.pushInput("5");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_6)) {
            parentView.pushInput("6");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_7)) {
            parentView.pushInput("7");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8)) {
            parentView.pushInput("8");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_9)) {
            parentView.pushInput("9");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            parentView.pushInput("K");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            parentView.pushInput("C");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            parentView.pushInput("I");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            parentView.pushInput("X");
        }
    }

    @Override
    public void dispose() {
        if (modelBatch != null) modelBatch.dispose();
        if (decalBatch != null) decalBatch.dispose();
        if (dungeonRenderer != null) dungeonRenderer.dispose();
        if (battleRenderer != null) battleRenderer.dispose();
        if (hudRenderer != null) hudRenderer.dispose();
        
        System.exit(0); // Ferme complètement le programme Java
    }
}
