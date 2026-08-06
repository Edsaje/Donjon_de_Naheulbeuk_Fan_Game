package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.NaheulbeukDungeon;

/**
 * Moteur de Rendu Graphique HD-2D OpenGL (LibGDX).
 * Génère et affiche le Donjon 3D avec dalles de sol, murs 3D extrudés et éclairage dynamique.
 *
 * @author Hibouxe
 * @version 2.0
 */
public class HD2DGameApp extends ApplicationAdapter {
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Environment environment;

    private Model floorModel;
    private Model wallBlockModel;
    private Array<ModelInstance> instances = new Array<>();

    private NaheulbeukDungeon dungeon;

    @Override
    public void create() {
        // 1. Initialisation de la Caméra 3D HD-2D (Inclinaison plongée -45°)
        camera = new PerspectiveCamera(45, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(21f, 30f, 48f);
        camera.lookAt(21f, 0f, 21f);
        camera.near = 0.1f;
        camera.far = 500f;
        camera.update();

        // 2. Éclairage Dynamique HD-2D (Lumière d'ambiance + Torche directionnelle)
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.35f, 0.35f, 0.45f, 1f));
        environment.add(new DirectionalLight().set(0.85f, 0.75f, 0.6f, -1f, -0.8f, -0.5f));

        modelBatch = new ModelBatch();

        // 3. Génération du Donjon de Naheulbeuk
        dungeon = new NaheulbeukDungeon();
        dungeon.generate();

        // 4. Création des Matériaux 3D (Sol de pierre gris, Blocs massifs Style Pokémon Donjon Mystère)
        ModelBuilder modelBuilder = new ModelBuilder();

        floorModel = modelBuilder.createBox(2.0f, 0.1f, 2.0f,
                new Material(ColorAttribute.createDiffuse(new Color(0.35f, 0.35f, 0.4f, 1f))),
                Usage.Position | Usage.Normal);

        wallBlockModel = modelBuilder.createBox(2.0f, 2.5f, 2.0f,
                new Material(ColorAttribute.createDiffuse(new Color(0.5f, 0.35f, 0.25f, 1f))),
                Usage.Position | Usage.Normal);

        // 5. Traduction de la Grille 2D en Objets 3D OpenGL
        buildDungeonInstances();
    }

    private void buildDungeonInstances() {
        Cell[][] grid = dungeon.getGrid();
        float tileSize = 2.0f;

        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                Cell cell = grid[x][y];
                float posX = x * tileSize;
                float posZ = y * tileSize;

                if (cell.isWalkable()) {
                    // Dalle de Sol 3D Navigable (Y = 0)
                    ModelInstance floor = new ModelInstance(floorModel);
                    floor.transform.setToTranslation(posX, 0f, posZ);
                    instances.add(floor);
                } else {
                    // Bloc de Mur / Roche Massif 3D (Style Pokémon Donjon Mystère)
                    ModelInstance wallBlock = new ModelInstance(wallBlockModel);
                    wallBlock.transform.setToTranslation(posX, 1.25f, posZ);
                    instances.add(wallBlock);
                }
            }
        }
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();

        // Rendu 3D de l'ensemble du Donjon avec éclairage
        modelBatch.begin(camera);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        if (modelBatch != null) modelBatch.dispose();
        if (floorModel != null) floorModel.dispose();
        if (wallBlockModel != null) wallBlockModel.dispose();
    }
}