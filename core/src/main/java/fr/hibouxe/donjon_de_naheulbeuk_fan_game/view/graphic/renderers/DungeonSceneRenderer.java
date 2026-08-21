package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.renderers;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.AssetProvider;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;

/**
 * Composant de rendu 3D spécialisé pour la scène d'exploration du Donjon (SRP).
 * Gère la construction 3D des dalles, des murs massifs, des coffres et des escaliers.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class DungeonSceneRenderer implements Disposable {
    private Model floorModel;
    private Model wallStraightModel;
    private Model wallCornerModel;
    private Model wallInnerCornerModel;
    private Array<ModelInstance> instances = new Array<>();
    private Array<Decal> entityBillboards = new Array<>();
    private Decal heroSprite;

    private Texture heroTexture;
    private TextureRegion[][] heroFrames;
    private int currentDirection = 0; // 0=Bas, 1=Haut, 2=Gauche, 3=Droite
    private float stateTime = 0f;
    private int lastPlayerX = -1;
    private int lastPlayerY = -1;
    private float targetSpriteX = 0f;
    private float targetSpriteZ = 0f;
    private boolean isAnimating = false;
    public boolean isAnimating() { return isAnimating; }

    public float getHeroSpriteX(float fallback) {
        return heroSprite != null ? heroSprite.getX() : fallback;
    }

    public float getHeroSpriteZ(float fallback) {
        return heroSprite != null ? heroSprite.getZ() : fallback;
    }

    public boolean isAlmostFinished() {
        if (!isAnimating) return true;
        if (heroSprite == null) return true;
        float moveSpeed = 4.0f * com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        float diffX = targetSpriteX - heroSprite.getX();
        float diffZ = targetSpriteZ - heroSprite.getZ();
        // Autoriser la saisie 2 frames avant la fin de l'animation
        return (Math.abs(diffX) <= moveSpeed * 2.5f && Math.abs(diffZ) <= moveSpeed * 2.5f);
    }
    private int lastFloor = -1;

    private Texture monsterTexture;
    private Texture chestTexture;
    private Texture stairsTexture;
    private Texture elfTexture;

    private Texture dungeonTex;

    private com.badlogic.gdx.utils.Pool<ModelInstance> floorPool;
    private com.badlogic.gdx.utils.Pool<ModelInstance> wallStraightPool;
    private com.badlogic.gdx.utils.Pool<ModelInstance> wallCornerPool;
    private com.badlogic.gdx.utils.Pool<ModelInstance> wallInnerCornerPool;
    
    private com.badlogic.gdx.utils.Pool<Decal> decalPool;

    private TextureRegion monsterRegion;
    private TextureRegion chestRegion;
    private TextureRegion stairsRegion;
    private TextureRegion[][] elfFrames;

    private float tileSize = 1.0f;
    private boolean usingObjModels = false;

    private AssetProvider assetProvider;

    public DungeonSceneRenderer(AssetProvider assetProvider) {
        this.assetProvider = assetProvider;
        heroTexture = assetProvider.getHeroSprite("Ranger");
        // Découpage automatique si l'image fait 256x256 (64x64 par frame)
        if (heroTexture.getWidth() >= 256) {
            heroFrames = TextureRegion.split(heroTexture, 64, 64);
        } else {
            heroFrames = new TextureRegion[1][1];
            heroFrames[0][0] = new TextureRegion(heroTexture);
        }
        
        monsterTexture = assetProvider.getMonsterSprite("Orc");
        chestTexture = assetProvider.getChestSprite();
        stairsTexture = assetProvider.getStairsSprite();
        elfTexture = assetProvider.getHeroSprite("Elf");

        monsterRegion = new TextureRegion(monsterTexture);
        chestRegion = new TextureRegion(chestTexture);
        stairsRegion = new TextureRegion(stairsTexture);
        if (elfTexture != null) {
            elfFrames = TextureRegion.split(elfTexture, 64, 64);
        }

        com.badlogic.gdx.graphics.g3d.loader.ObjLoader objLoader = new com.badlogic.gdx.graphics.g3d.loader.ObjLoader();
        
        floorModel = objLoader.loadModel(com.badlogic.gdx.Gdx.files.internal("models/dungeon/cavern/floor.obj"));
        wallStraightModel = objLoader.loadModel(com.badlogic.gdx.Gdx.files.internal("models/dungeon/cavern/wall_straight.obj"));
        wallCornerModel = objLoader.loadModel(com.badlogic.gdx.Gdx.files.internal("models/dungeon/cavern/wall_corner.obj"));
        wallInnerCornerModel = objLoader.loadModel(com.badlogic.gdx.Gdx.files.internal("models/dungeon/cavern/wall_inner_corner.obj"));
        
        dungeonTex = new Texture(com.badlogic.gdx.Gdx.files.internal("models/dungeon/cavern/texture.png"), true);
        dungeonTex.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute texAttr = com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute.createDiffuse(dungeonTex);

        for (Material m : floorModel.materials) m.set(texAttr);
        for (Material m : wallStraightModel.materials) m.set(texAttr);
        for (Material m : wallCornerModel.materials) m.set(texAttr);
        for (Material m : wallInnerCornerModel.materials) m.set(texAttr);

        this.floorPool = new com.badlogic.gdx.utils.Pool<ModelInstance>() {
            @Override protected ModelInstance newObject() { return new ModelInstance(floorModel); }
        };
        this.wallStraightPool = new com.badlogic.gdx.utils.Pool<ModelInstance>() {
            @Override protected ModelInstance newObject() { return new ModelInstance(wallStraightModel); }
        };
        this.wallCornerPool = new com.badlogic.gdx.utils.Pool<ModelInstance>() {
            @Override protected ModelInstance newObject() { return new ModelInstance(wallCornerModel); }
        };
        this.wallInnerCornerPool = new com.badlogic.gdx.utils.Pool<ModelInstance>() {
            @Override protected ModelInstance newObject() { return new ModelInstance(wallInnerCornerModel); }
        };
        this.decalPool = new com.badlogic.gdx.utils.Pool<Decal>() {
            @Override protected Decal newObject() { return Decal.newDecal(1f, 1f, new TextureRegion(), true); }
        };

        usingObjModels = true;
    }

    public void setLeaderClass(String className) {
        if (heroTexture != null) {
            heroTexture = null; // L'AssetProvider s'occupe de la gestion mémoire
        }
        heroTexture = assetProvider.getHeroSprite(className);
        if (heroTexture.getWidth() >= 256) {
            heroFrames = TextureRegion.split(heroTexture, 64, 64);
        } else {
            heroFrames = new TextureRegion[1][1];
            heroFrames[0][0] = new TextureRegion(heroTexture);
        }
    }

    public void buildScene(Dungeon dungeon, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team, float playerX, float playerZ, int currentFloor) {
        
        float oldHeroX = (heroSprite != null && lastFloor == currentFloor) ? heroSprite.getX() : (playerX * tileSize + 0.5f);
        float oldHeroZ = (heroSprite != null && lastFloor == currentFloor) ? heroSprite.getZ() : (playerZ * tileSize + 0.5f);

        for (ModelInstance instance : instances) {
            if (instance.model == floorModel) floorPool.free(instance);
            else if (instance.model == wallStraightModel) wallStraightPool.free(instance);
            else if (instance.model == wallCornerModel) wallCornerPool.free(instance);
            else if (instance.model == wallInnerCornerModel) wallInnerCornerPool.free(instance);
        }
        for (Decal d : entityBillboards) decalPool.free(d);
        instances.clear();
        entityBillboards.clear();
        
        // (Ligne de téléportation supprimée ici pour permettre la fluidité)

        Cell[][] grid = dungeon.getGrid();

        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                Cell cell = grid[x][y];
                float posX = x * tileSize;
                float posZ = y * tileSize;

                if (cell.isWalkable()) {
                    ModelInstance floor = floorPool.obtain();
                    floor.transform.setToTranslation(posX + 0.5f, 0f, posZ + 0.5f);
                    floor.transform.rotate(com.badlogic.gdx.math.Vector3.Y, 0f);
                    instances.add(floor);

                    if (cell.hasMonster()) {
                        Decal monsterSprite = decalPool.obtain();
                        monsterSprite.setTextureRegion(monsterRegion);
                        monsterSprite.setDimensions(1.2f, 1.8f);
                        monsterSprite.setPosition(posX + 0.5f, 1.0f, posZ + 0.5f);
                        entityBillboards.add(monsterSprite);
                    }

                    if (cell.hasItem()) {
                        Decal chestSprite = decalPool.obtain();
                        chestSprite.setTextureRegion(chestRegion);
                        chestSprite.setDimensions(1.2f, 1.2f);
                        chestSprite.setPosition(posX + 0.5f, 0.7f, posZ + 0.5f);
                        entityBillboards.add(chestSprite);
                    }

                    if (cell.hasStairs()) {
                        Decal stairsSprite = decalPool.obtain();
                        stairsSprite.setTextureRegion(stairsRegion);
                        stairsSprite.setDimensions(1.6f, 1.6f);
                        stairsSprite.setPosition(posX + 0.5f, 1.0f, posZ + 0.5f);
                        entityBillboards.add(stairsSprite);
                    }
                    
                                // SCRIPT ELFE (Tutoriel - étage 2)
                    if (currentFloor == 2 && x == 1 && y == 2) {
                        boolean elfSaved = false;
                        for (fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character m : team.getMembers()) {
                            if (m.getClass().getSimpleName().equals("Elf")) elfSaved = true;
                        }
                        if (!elfSaved) {
                            if (elfFrames != null) {
                                Decal elfSprite = decalPool.obtain();
                                elfSprite.setTextureRegion(elfFrames[0][0]);
                                elfSprite.setDimensions(1.8f, 1.8f);
                                elfSprite.setPosition(posX + 0.5f, 1.0f, posZ + 0.5f);
                                entityBillboards.add(elfSprite);
                            }
                        }
                    }
                } else {
                    boolean n = (y > 0) ? !grid[x][y-1].isWalkable() : true;
                    boolean s = (y < dungeon.getHeight() - 1) ? !grid[x][y+1].isWalkable() : true;
                    boolean w = (x > 0) ? !grid[x-1][y].isWalkable() : true;
                    boolean e = (x < dungeon.getWidth() - 1) ? !grid[x+1][y].isWalkable() : true;

                    if (n && s && e && w) {
                        continue;
                    }

                    boolean floorN = !n;
                    boolean floorS = !s;
                    boolean floorE = !e;
                    boolean floorW = !w;

                    int openCount = (floorN ? 1 : 0) + (floorS ? 1 : 0) + (floorE ? 1 : 0) + (floorW ? 1 : 0);

                    ModelInstance wall = null;
                    float angle = 0f;

                    if (openCount == 1) {
                        wall = wallStraightPool.obtain();
                        if (floorN) angle = 180f;
                        else if (floorE) angle = 90f;
                        else if (floorS) angle = 0f;
                        else if (floorW) angle = 270f;
                    } else if (openCount == 2) {
                        if ((floorN && floorS) || (floorE && floorW)) {
                            wall = wallStraightPool.obtain();
                            if (floorN && floorS) angle = 90f;
                            else angle = 0f;
                        } else {
                            wall = wallCornerPool.obtain();
                            if (floorN && floorE) angle = 180f;
                            else if (floorE && floorS) angle = 90f;
                            else if (floorS && floorW) angle = 0f;
                            else if (floorW && floorN) angle = 270f;
                        }
                    } else if (openCount >= 3) {
                        wall = wallInnerCornerPool.obtain();
                        if (!floorN) angle = 0f;
                        else if (!floorE) angle = 90f;
                        else if (!floorS) angle = 180f;
                        else if (!floorW) angle = 270f;
                    }

                    if (wall != null) {
                        wall.transform.setToTranslation(posX + 0.5f, 0.5f, posZ + 0.5f);
                        wall.transform.rotate(com.badlogic.gdx.math.Vector3.Y, angle);
                        instances.add(wall);
                    }
                }
            }
        }
        // --- Positionnement du Joueur (Héros) ---
        float currentX = oldHeroX;
        float currentZ = oldHeroZ;

        lastPlayerX = (int) playerX;
        lastPlayerY = (int) playerZ;
        lastFloor = currentFloor;

        TextureRegion initialFrame = heroFrames.length > 0 && heroFrames[0].length > 0 ? heroFrames[0][0] : new TextureRegion(heroTexture);
        heroSprite = decalPool.obtain();
        heroSprite.setTextureRegion(initialFrame);
        heroSprite.setDimensions(1.4f, 2.0f);
        heroSprite.setPosition(currentX, 1.0f, currentZ);
        entityBillboards.add(heroSprite);
    }

    public void render(ModelBatch modelBatch, DecalBatch decalBatch, Environment environment, PerspectiveCamera camera, float playerX, float playerZ, int playerDirection) {
        this.currentDirection = playerDirection;
        this.targetSpriteX = playerX * tileSize + 0.5f;
        this.targetSpriteZ = playerZ * tileSize + 0.5f;

        float currentSpriteX = heroSprite.getX();
        float currentSpriteZ = heroSprite.getZ();

        float diffX = targetSpriteX - currentSpriteX;
        float diffZ = targetSpriteZ - currentSpriteZ;

        if (Math.abs(diffX) > 0.01f || Math.abs(diffZ) > 0.01f) {
            this.isAnimating = true;
            currentSpriteX = targetSpriteX;
            currentSpriteZ = targetSpriteZ;
        } else {
            this.isAnimating = false;
        }

        if (this.isAnimating) {
            stateTime += com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        } else {
            stateTime = 0f;
        }

        int maxFrames = 1;
        if (heroFrames.length > currentDirection) {
            maxFrames = heroFrames[currentDirection].length;
        }
        
        // Vitesse d'animation ajustée (8) proportionnellement au mouvement
        int frameIndex = (int)(stateTime * 8) % maxFrames;

        if (heroFrames.length > currentDirection && heroFrames[currentDirection].length > frameIndex) {
            heroSprite.setTextureRegion(heroFrames[currentDirection][frameIndex]);
        }

        heroSprite.setPosition(currentSpriteX, 1.0f, currentSpriteZ);

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
        if (wallStraightModel != null) wallStraightModel.dispose();
        if (wallCornerModel != null) wallCornerModel.dispose();
        if (wallInnerCornerModel != null) wallInnerCornerModel.dispose();
        if (dungeonTex != null) dungeonTex.dispose();
        // Les textures heroTexture, monsterTexture, chestTexture, stairsTexture sont gérées par l'AssetProvider
    }
}
