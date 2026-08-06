package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view;

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

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.NaheulbeukDungeon;

/**
 * Chef d'Orchestre du Moteur HD-2D OpenGL (LibGDX) - Architecture Propre SRP & SOLID.
 * Rôle minimaliste : Orchestre l'état du jeu (EXPLORATION / BATTLE) et les contrôles clavier,
 * puis délègue le rendu 3D aux composants spécialisés {@link DungeonSceneRenderer} et {@link BattleArenaRenderer}.
 *
 * @author Hibouxe
 * @version 7.0
 */
public class HD2DGameApp extends ApplicationAdapter {
    public enum GameState { EXPLORATION, BATTLE }

    private GameState currentState = GameState.EXPLORATION;

    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private DecalBatch decalBatch;
    private Environment environment;

    private DungeonSceneRenderer dungeonRenderer;
    private BattleArenaRenderer battleRenderer;
    private HUDRenderer hudRenderer;

    private NaheulbeukDungeon dungeon;
    private int playerX;
    private int playerY;
    private int currentFloor = 1;
    private float tileSize = 2.0f;
    private KeyboardLayout activeKeyboardLayout = KeyboardLayout.detectSystemLayout();

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

        loadFloor();
    }

    private void loadFloor() {
        dungeon = new NaheulbeukDungeon();
        dungeon.generate();

        int[] startPos = dungeon.getFirstWalkablePosition();
        playerX = startPos[0];
        playerY = startPos[1];

        float startWorldX = playerX * tileSize;
        float startWorldZ = playerY * tileSize;

        camera.position.set(startWorldX, 14f, startWorldZ + 12f);
        camera.lookAt(startWorldX, 0f, startWorldZ);
        camera.update();

        dungeonRenderer.buildScene(dungeon, playerX, playerY);
    }

    @Override
    public void render() {
        handleInput();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (currentState == GameState.EXPLORATION) {
            float targetWorldX = playerX * tileSize;
            float targetWorldZ = playerY * tileSize;

            // Restauration fluide de la position et hauteur de caméra d'exploration (Y = 14.0m)
            camera.position.x += (targetWorldX - camera.position.x) * 0.1f;
            camera.position.y += (14.0f - camera.position.y) * 0.1f;
            camera.position.z += ((targetWorldZ + 12f) - camera.position.z) * 0.1f;
            camera.lookAt(camera.position.x, 0.0f, camera.position.z - 12f);
            camera.update();

            dungeonRenderer.render(modelBatch, decalBatch, environment, camera, playerX, playerY);
        } else {
            battleRenderer.render(modelBatch, decalBatch, environment, camera);
        }

        // Rendu 2D de l'Interface HUD & Minimap en superposition
        hudRenderer.renderHUD(dungeon, playerX, playerY, currentFloor, currentState);
    }

    private void handleInput() {
        if (hudRenderer != null && hudRenderer.isMenuOpen()) {
            return; // En pause tant que le Menu Dragon Quest est ouvert
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            currentState = (currentState == GameState.EXPLORATION) ? GameState.BATTLE : GameState.EXPLORATION;
            System.out.println("=== BASCULE EN MODE : " + currentState + " ===");
            return;
        }

        if (currentState != GameState.EXPLORATION) return;

        Cell[][] grid = dungeon.getGrid();
        int upKey = activeKeyboardLayout.getUpKey();
        int leftKey = activeKeyboardLayout.getLeftKey();
        boolean moved = false;

        if (Gdx.input.isKeyJustPressed(upKey) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (playerY - 1 >= 0 && grid[playerX][playerY - 1].isWalkable()) {
                playerY--;
                moved = true;
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (playerY + 1 < dungeon.getHeight() && grid[playerX][playerY + 1].isWalkable()) {
                playerY++;
                moved = true;
            }
        } else if (Gdx.input.isKeyJustPressed(leftKey) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (playerX - 1 >= 0 && grid[playerX - 1][playerY].isWalkable()) {
                playerX--;
                moved = true;
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (playerX + 1 < dungeon.getWidth() && grid[playerX + 1][playerY].isWalkable()) {
                playerX++;
                moved = true;
            }
        }

        if (moved) {
            Cell currentCell = grid[playerX][playerY];

            if (currentCell.hasMonster()) {
                currentState = GameState.BATTLE;
                System.out.println("COMBAT DÉCLENCHÉ EN 3D STYLE DRAGON QUEST !");
            } else if (currentCell.hasStairs()) {
                currentFloor++;
                System.out.println("=== DESCENTE EN 3D À L'ÉTAGE " + currentFloor + " ===");
                loadFloor();
            }
        }
    }

    @Override
    public void dispose() {
        if (modelBatch != null) modelBatch.dispose();
        if (decalBatch != null) decalBatch.dispose();
        if (dungeonRenderer != null) dungeonRenderer.dispose();
        if (battleRenderer != null) battleRenderer.dispose();
        if (hudRenderer != null) hudRenderer.dispose();
    }
}
