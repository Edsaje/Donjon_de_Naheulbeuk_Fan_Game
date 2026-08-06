package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.utils.Array;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.NaheulbeukDungeon;

/**
 * Moteur de Rendu Graphique HD-2D OpenGL (LibGDX) - Étape 8.
 * Gestion complète des Coffres 3D, Escaliers, Monstres, Héros et Changement d'Étage.
 *
 * @author Hibouxe
 * @version 5.0
 */
public class HD2DGameApp extends ApplicationAdapter {
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private DecalBatch decalBatch;
    private Environment environment;

    private Model floorModel;
    private Model wallBlockModel;
    private Array<ModelInstance> instances = new Array<>();
    private Array<Decal> entityBillboards = new Array<>();
    private Decal heroSprite;

    private Texture heroTexture;
    private Texture monsterTexture;
    private Texture chestTexture;
    private Texture stairsTexture;

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

        // Textures des différentes entités
        heroTexture = createColoredTexture(Color.GOLD);
        monsterTexture = createColoredTexture(Color.FIREBRICK);
        chestTexture = createColoredTexture(Color.CYAN);
        stairsTexture = createColoredTexture(Color.WHITE);

        ModelBuilder modelBuilder = new ModelBuilder();
        floorModel = modelBuilder.createBox(2.0f, 0.1f, 2.0f,
                new Material(ColorAttribute.createDiffuse(new Color(0.35f, 0.35f, 0.4f, 1f))),
                Usage.Position | Usage.Normal);

        wallBlockModel = modelBuilder.createBox(2.0f, 2.5f, 2.0f,
                new Material(ColorAttribute.createDiffuse(new Color(0.5f, 0.35f, 0.25f, 1f))),
                Usage.Position | Usage.Normal);

        // Initialiser l'étage
        loadFloor();
    }

    private void loadFloor() {
        instances.clear();
        entityBillboards.clear();

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

        buildDungeonScene();
    }

    private void buildDungeonScene() {
        Cell[][] grid = dungeon.getGrid();

        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                Cell cell = grid[x][y];
                float posX = x * tileSize;
                float posZ = y * tileSize;

                if (cell.isWalkable()) {
                    // Dalle de sol 3D
                    ModelInstance floor = new ModelInstance(floorModel);
                    floor.transform.setToTranslation(posX, 0f, posZ);
                    instances.add(floor);

                    // Billboard Monstre (Rouge)
                    if (cell.hasMonster()) {
                        Decal monsterSprite = Decal.newDecal(1.2f, 1.8f, new TextureRegion(monsterTexture), true);
                        monsterSprite.setPosition(posX, 1.0f, posZ);
                        entityBillboards.add(monsterSprite);
                    }

                    // Billboard Coffre (Cyan)
                    if (cell.hasItem()) {
                        Decal chestSprite = Decal.newDecal(1.2f, 1.2f, new TextureRegion(chestTexture), true);
                        chestSprite.setPosition(posX, 0.7f, posZ);
                        entityBillboards.add(chestSprite);
                        System.out.println("📦 Coffre 3D positionné en (" + x + ", " + y + ")");
                    }

                    // Billboard Escalier (Blanc)
                    if (cell.hasStairs()) {
                        Decal stairsSprite = Decal.newDecal(1.6f, 1.6f, new TextureRegion(stairsTexture), true);
                        stairsSprite.setPosition(posX, 0.8f, posZ);
                        entityBillboards.add(stairsSprite);
                        System.out.println("🪜 Escalier 3D positionné en (" + x + ", " + y + ")");
                    }
                } else {
                    // Bloc de mur massif 3D
                    ModelInstance wallBlock = new ModelInstance(wallBlockModel);
                    wallBlock.transform.setToTranslation(posX, 1.25f, posZ);
                    instances.add(wallBlock);
                }
            }
        }

        // Billboard du Joueur (Héros Doré)
        heroSprite = Decal.newDecal(1.4f, 2.0f, new TextureRegion(heroTexture), true);
        heroSprite.setPosition(playerX * tileSize, 1.0f, playerY * tileSize);
        entityBillboards.add(heroSprite);
    }

    @Override
    public void render() {
        handleInput();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        float targetWorldX = playerX * tileSize;
        float targetWorldZ = playerY * tileSize;

        camera.position.x += (targetWorldX - camera.position.x) * 0.1f;
        camera.position.z += ((targetWorldZ + 12f) - camera.position.z) * 0.1f;
        camera.lookAt(camera.position.x, 0.0f, camera.position.z - 12f);
        camera.update();

        heroSprite.setPosition(playerX * tileSize, 1.0f, playerY * tileSize);

        modelBatch.begin(camera);
        modelBatch.render(instances, environment);
        modelBatch.end();

        for (Decal sprite : entityBillboards) {
            sprite.lookAt(camera.position, camera.up);
            decalBatch.add(sprite);
        }
        decalBatch.flush();
    }

    private void handleInput() {
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

            // Si le joueur arrive sur l'escalier -> Descendre à l'étage suivant
            if (currentCell.hasStairs()) {
                currentFloor++;
                System.out.println("=== DESCENTE EN 3D À L'ÉTAGE " + currentFloor + " ===");
                loadFloor();
            }
        }
    }

    private Texture createColoredTexture(Color color) {
        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void dispose() {
        if (modelBatch != null) modelBatch.dispose();
        if (decalBatch != null) decalBatch.dispose();
        if (floorModel != null) floorModel.dispose();
        if (wallBlockModel != null) wallBlockModel.dispose();
        if (heroTexture != null) heroTexture.dispose();
        if (monsterTexture != null) monsterTexture.dispose();
        if (chestTexture != null) chestTexture.dispose();
        if (stairsTexture != null) stairsTexture.dispose();
    }
}
