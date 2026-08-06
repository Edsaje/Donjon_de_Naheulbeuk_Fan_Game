package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Dungeon;

/**
 * Composant de rendu 3D spécialisé pour la scène d'exploration du Donjon (SRP).
 * Gère la construction 3D des dalles, des murs massifs, des coffres et des escaliers.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class DungeonSceneRenderer implements Disposable {
    private Model floorModel;
    private Model wallBlockModel;
    private Array<ModelInstance> instances = new Array<>();
    private Array<Decal> entityBillboards = new Array<>();
    private Decal heroSprite;

    private Texture heroTexture;
    private Texture monsterTexture;
    private Texture chestTexture;
    private Texture stairsTexture;

    private float tileSize = 2.0f;

    public DungeonSceneRenderer() {
        heroTexture = SpriteFactory.createHeroSprite("Ranger");
        monsterTexture = SpriteFactory.createMonsterSprite("Orc");
        chestTexture = SpriteFactory.createChestSprite();
        stairsTexture = SpriteFactory.createStairsSprite();

        ModelBuilder modelBuilder = new ModelBuilder();
        floorModel = modelBuilder.createBox(2.0f, 0.1f, 2.0f,
                new Material(ColorAttribute.createDiffuse(new Color(0.35f, 0.35f, 0.4f, 1f))),
                Usage.Position | Usage.Normal);

        wallBlockModel = modelBuilder.createBox(2.0f, 2.5f, 2.0f,
                new Material(ColorAttribute.createDiffuse(new Color(0.5f, 0.35f, 0.25f, 1f))),
                Usage.Position | Usage.Normal);
    }

    public void buildScene(Dungeon dungeon, int playerX, int playerY) {
        instances.clear();
        entityBillboards.clear();

        Cell[][] grid = dungeon.getGrid();

        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                Cell cell = grid[x][y];
                float posX = x * tileSize;
                float posZ = y * tileSize;

                if (cell.isWalkable()) {
                    ModelInstance floor = new ModelInstance(floorModel);
                    floor.transform.setToTranslation(posX, 0f, posZ);
                    instances.add(floor);

                    if (cell.hasMonster()) {
                        Decal monsterSprite = Decal.newDecal(1.2f, 1.8f, new TextureRegion(monsterTexture), true);
                        monsterSprite.setPosition(posX, 1.0f, posZ);
                        entityBillboards.add(monsterSprite);
                    }

                    if (cell.hasItem()) {
                        Decal chestSprite = Decal.newDecal(1.2f, 1.2f, new TextureRegion(chestTexture), true);
                        chestSprite.setPosition(posX, 0.7f, posZ);
                        entityBillboards.add(chestSprite);
                    }

                    if (cell.hasStairs()) {
                        Decal stairsSprite = Decal.newDecal(1.6f, 1.6f, new TextureRegion(stairsTexture), true);
                        stairsSprite.setPosition(posX, 0.8f, posZ);
                        entityBillboards.add(stairsSprite);
                    }
                } else {
                    ModelInstance wallBlock = new ModelInstance(wallBlockModel);
                    wallBlock.transform.setToTranslation(posX, 1.25f, posZ);
                    instances.add(wallBlock);
                }
            }
        }

        heroSprite = Decal.newDecal(1.4f, 2.0f, new TextureRegion(heroTexture), true);
        heroSprite.setPosition(playerX * tileSize, 1.0f, playerY * tileSize);
        entityBillboards.add(heroSprite);
    }

    public void render(ModelBatch modelBatch, DecalBatch decalBatch, Environment environment, PerspectiveCamera camera, int playerX, int playerY) {
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
        if (floorModel != null) floorModel.dispose();
        if (wallBlockModel != null) wallBlockModel.dispose();
        if (heroTexture != null) heroTexture.dispose();
        if (monsterTexture != null) monsterTexture.dispose();
        if (chestTexture != null) chestTexture.dispose();
        if (stairsTexture != null) stairsTexture.dispose();
    }
}
