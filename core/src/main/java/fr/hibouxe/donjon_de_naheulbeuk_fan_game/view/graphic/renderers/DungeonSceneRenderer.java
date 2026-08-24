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
import com.badlogic.gdx.utils.Json;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.theme.DungeonTheme;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;

/**
 * Composant de rendu 3D spÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©cialisÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â© pour la scÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¨ne d'exploration du Donjon (SRP).
 * GÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¨re la construction 3D des dalles, des murs massifs, des coffres et des escaliers.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class DungeonSceneRenderer implements Disposable {
    private Model floorModel;
    private Model wallModel;
    
    private Model chestBaseModel;
    private Model chestLidModel;
    private Model doorFrameModel;
    private Model doorModel;
    
    private com.badlogic.gdx.utils.Pool<ModelInstance> chestBasePool;
    private com.badlogic.gdx.utils.Pool<ModelInstance> chestLidPool;
    private com.badlogic.gdx.utils.Pool<ModelInstance> doorFramePool;
    private com.badlogic.gdx.utils.Pool<ModelInstance> doorPool;

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
    private com.badlogic.gdx.utils.Pool<ModelInstance> wallPool;
    
    private com.badlogic.gdx.utils.Pool<Decal> decalPool;

    private TextureRegion monsterRegion;
    private TextureRegion chestRegion;
    private TextureRegion stairsRegion;
    private TextureRegion[][] elfFrames;

    private float tileSize = 1.0f;
    private boolean usingObjModels = false;

    private AssetProvider assetProvider;

    private DungeonTheme theme;

    public DungeonSceneRenderer(AssetProvider assetProvider, String themePath) {
        this.assetProvider = assetProvider;
        
        Json json = new Json();
        String jsonStr = com.badlogic.gdx.Gdx.files.internal(themePath).readString();
        this.theme = json.fromJson(DungeonTheme.class, jsonStr);

        heroTexture = assetProvider.getHeroSprite("Ranger");
        // DÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©coupage automatique si l'image fait 256x256 (64x64 par frame)
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

        com.badlogic.gdx.graphics.g3d.loader.ObjLoader.ObjLoaderParameters param = new com.badlogic.gdx.graphics.g3d.loader.ObjLoader.ObjLoaderParameters();
        param.flipV = true;
        com.badlogic.gdx.graphics.g3d.loader.ObjLoader objLoader = new com.badlogic.gdx.graphics.g3d.loader.ObjLoader();
        
        floorModel = objLoader.loadModel(com.badlogic.gdx.Gdx.files.internal(theme.getFloorModel()), param);
        wallModel = objLoader.loadModel(com.badlogic.gdx.Gdx.files.internal(theme.getWallModel()), param);
        
        try {
            chestBaseModel = objLoader.loadModel(com.badlogic.gdx.Gdx.files.internal("models/dungeon/common/chest_base.obj"), param);
            chestLidModel = objLoader.loadModel(com.badlogic.gdx.Gdx.files.internal("models/dungeon/common/chest_lid.obj"), param);
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.log("DungeonRenderer", "Missing chest models, fallback to decal");
        }
        
        try {
            doorFrameModel = objLoader.loadModel(com.badlogic.gdx.Gdx.files.internal("models/dungeon/naheulbeuk/door_frame.obj"), param);
            doorModel = objLoader.loadModel(com.badlogic.gdx.Gdx.files.internal("models/dungeon/naheulbeuk/door.obj"), param);
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.log("DungeonRenderer", "Missing door models");
        }
        
        // We let LibGDX handle the materials defined in the .mtl file!
        this.tileSize = theme.getTileSize();

        this.floorPool = new com.badlogic.gdx.utils.Pool<ModelInstance>() {
            @Override protected ModelInstance newObject() { return new ModelInstance(floorModel); }
        };
        this.wallPool = new com.badlogic.gdx.utils.Pool<ModelInstance>() { @Override protected ModelInstance newObject() { return new ModelInstance(wallModel); } };
        
        this.chestBasePool = new com.badlogic.gdx.utils.Pool<ModelInstance>() { @Override protected ModelInstance newObject() { return new ModelInstance(chestBaseModel); } };
        this.chestLidPool = new com.badlogic.gdx.utils.Pool<ModelInstance>() { @Override protected ModelInstance newObject() { return new ModelInstance(chestLidModel); } };
        this.doorFramePool = new com.badlogic.gdx.utils.Pool<ModelInstance>() { @Override protected ModelInstance newObject() { return new ModelInstance(doorFrameModel); } };
        this.doorPool = new com.badlogic.gdx.utils.Pool<ModelInstance>() { @Override protected ModelInstance newObject() { return new ModelInstance(doorModel); } };
        this.decalPool = new com.badlogic.gdx.utils.Pool<Decal>() {
            @Override protected Decal newObject() { return Decal.newDecal(1f, 1f, new TextureRegion(), true); }
        };

        usingObjModels = true;
        
    }

    public int getInstancesCount() { return instances.size; }
    public void setLeaderClass(String className) {
        if (heroTexture != null) {
            heroTexture = null; // L'AssetProvider s'occupe de la gestion mÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©moire
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
        
        float oldHeroX = (heroSprite != null && lastFloor == currentFloor) ? heroSprite.getX() : (playerX * tileSize);
        float oldHeroZ = (heroSprite != null && lastFloor == currentFloor) ? heroSprite.getZ() : (playerZ * tileSize);

        for (ModelInstance instance : instances) {
            if (instance.model == floorModel) floorPool.free(instance);
            else if (instance.model == wallModel) wallPool.free(instance);
        }
        for (Decal d : entityBillboards) decalPool.free(d);
        instances.clear();
        
        entityBillboards.clear();
        
        // (Ligne de tÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©lÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©portation supprimÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©e ici pour permettre la fluiditÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©)

        Cell[][] grid = dungeon.getGrid();

        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                Cell cell = grid[x][y];
                float posX = x * tileSize;
                float posZ = y * tileSize;

                float half = tileSize / 2f;
                if (cell.isWalkable()) {
                    ModelInstance floor = floorPool.obtain();
                    floor.transform.setToTranslation(posX + half, 0f, posZ + half);
                    floor.transform.rotate(com.badlogic.gdx.math.Vector3.Y, 0f);
                    instances.add(floor);



                    if (cell.hasChest()) {
                        if (chestBaseModel != null && chestLidModel != null) {
                            ModelInstance base = chestBasePool.obtain();
                            base.transform.setToTranslation(posX + half, 0f, posZ + half);
                            instances.add(base);
                            
                            ModelInstance lid = chestLidPool.obtain();
                            
                            // ParamÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¨tres de la charniÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¨re du coffre (coordonnÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©es depuis Blender)
                            // Dans Blender: Y est la profondeur, Z est la hauteur.
                            // Dans LibGDX: Z est la profondeur, Y est la hauteur.
                            float hingeZ = 0.14676f; // Inverse du Y de Blender (-0.14676 -> 0.14676)
                            float hingeY = 0.5f;     // A MODIFIER: La hauteur (Z dans Blender) de ta charniÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¨re!
                            
                            // 1. Placement au centre
                            lid.transform.setToTranslation(posX + half, 0f, posZ + half);
                            // 2. DÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©placement sur la charniÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¨re
                            lid.transform.translate(0f, hingeY, hingeZ);
                            // 3. Rotation du couvercle
                            float angle = 90f * cell.getChestOpenProgress();
                            lid.transform.rotate(com.badlogic.gdx.math.Vector3.X, -angle);
                            // 4. DÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©placement inverse
                            lid.transform.translate(0f, -hingeY, -hingeZ);
                            
                            instances.add(lid);
                        } else {
                            Decal chestSprite = decalPool.obtain();
                            chestSprite.setTextureRegion(chestRegion);
                            chestSprite.setDimensions(1.2f, 1.2f);
                            chestSprite.setPosition(posX + half, 0.7f, posZ + half);
                            entityBillboards.add(chestSprite);
                        }
                    } else if (cell.hasItem()) {
                        // Les objets normaux par terre utilisent le sprite 2D
                        Decal itemSprite = decalPool.obtain();
                        itemSprite.setTextureRegion(chestRegion); // TODO: Utiliser un sprite spÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©cifique pour l'item
                        itemSprite.setDimensions(0.5f, 0.5f); // Plus petit pour qu'on sache que c'est un loot au sol
                        itemSprite.setPosition(posX + half, 0.3f, posZ + half);
                        entityBillboards.add(itemSprite);
                    }
                    
                    if (cell.hasDoor() && doorFrameModel != null && doorModel != null) {
                        boolean isDoorNS = false;
                        if (x > 0 && grid[x-1][y].isWall()) isDoorNS = true;
                        else if (x < dungeon.getWidth() - 1 && grid[x+1][y].isWall()) isDoorNS = true;
                        
                        float baseAngle = isDoorNS ? 0f : 90f;
                        
                        ModelInstance frame = doorFramePool.obtain();
                        frame.transform.setToTranslation(posX + half, 0f, posZ + half);
                        frame.transform.rotate(com.badlogic.gdx.math.Vector3.Y, baseAngle);
                        instances.add(frame);
                        
                        ModelInstance door = doorPool.obtain();
                        float hingeX = 0.493241f; // CoordonnÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©e X de la charniÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¨re dans Blender
                        
                        // 1. Placement au centre de la case
                        door.transform.setToTranslation(posX + half, 0f, posZ + half);
                        // 2. Alignement avec le couloir (Est/Ouest ou Nord/Sud)
                        door.transform.rotate(com.badlogic.gdx.math.Vector3.Y, baseAngle);
                        // 3. DÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©placement sur la charniÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¨re locale
                        door.transform.translate(hingeX, 0f, 0f);
                        // 4. Rotation d'ouverture de la porte
                        float doorAngle = 90f * cell.getDoorOpenProgress();
                        door.transform.rotate(com.badlogic.gdx.math.Vector3.Y, doorAngle);
                        // 5. DÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©placement inverse pour ramener la gÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©omÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©trie de la porte
                        door.transform.translate(-hingeX, 0f, 0f);
                        
                        instances.add(door);
                    }

                    if (cell.hasStairs()) {
                        Decal stairsSprite = decalPool.obtain();
                        stairsSprite.setTextureRegion(stairsRegion);
                        stairsSprite.setDimensions(1.6f, 1.6f);
                        stairsSprite.setPosition(posX + half, 1.0f, posZ + half);
                        entityBillboards.add(stairsSprite);
                    }
                    
                                // SCRIPT ELFE (Tutoriel - ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©tage 2)
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
                                elfSprite.setPosition(posX + half, 1.0f, posZ + half);
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
                      if (floorN) {
                          ModelInstance wallN = wallPool.obtain();
                          wallN.transform.setToTranslation(posX + half, theme.getWallOffset()[1], posZ + half);
                          wallN.transform.rotate(com.badlogic.gdx.math.Vector3.Y, 0f);
                          wallN.transform.translate(theme.getWallOffset()[0], 0, theme.getWallOffset()[2] - half);
                          instances.add(wallN);
                      }
                      if (floorS) {
                          ModelInstance wallS = wallPool.obtain();
                          wallS.transform.setToTranslation(posX + half, theme.getWallOffset()[1], posZ + half);
                          wallS.transform.rotate(com.badlogic.gdx.math.Vector3.Y, 180f);
                          wallS.transform.translate(theme.getWallOffset()[0], 0, theme.getWallOffset()[2] - half);
                          instances.add(wallS);
                      }
                      if (floorE) {
                          ModelInstance wallE = wallPool.obtain();
                          wallE.transform.setToTranslation(posX + half, theme.getWallOffset()[1], posZ + half);
                          wallE.transform.rotate(com.badlogic.gdx.math.Vector3.Y, 270f);
                          wallE.transform.translate(theme.getWallOffset()[0], 0, theme.getWallOffset()[2] - half);
                          instances.add(wallE);
                      }
                      if (floorW) {
                          ModelInstance wallW = wallPool.obtain();
                          wallW.transform.setToTranslation(posX + half, theme.getWallOffset()[1], posZ + half);
                          wallW.transform.rotate(com.badlogic.gdx.math.Vector3.Y, 90f);
                          wallW.transform.translate(theme.getWallOffset()[0], 0, theme.getWallOffset()[2] - half);
                          instances.add(wallW);
                      }
                }
            }
        }
        // --- Positionnement du Joueur (HÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©ros) ---
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

    public void render(ModelBatch modelBatch, DecalBatch decalBatch, Environment environment, PerspectiveCamera camera, float playerX, float playerZ, int playerDirection, java.util.List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.RoamingMonsterGroup> roamingMonsters) {
        this.currentDirection = playerDirection;
        this.targetSpriteX = playerX * tileSize + (tileSize / 2f);
        this.targetSpriteZ = playerZ * tileSize + (tileSize / 2f);

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
        
        // Vitesse d'animation ajustÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©e (8) proportionnellement au mouvement
        int frameIndex = (int)(stateTime * 8) % maxFrames;

        if (heroFrames.length > currentDirection && heroFrames[currentDirection].length > frameIndex) {
            heroSprite.setTextureRegion(heroFrames[currentDirection][frameIndex]);
        }

        heroSprite.setPosition(currentSpriteX, 1.0f, currentSpriteZ);

        modelBatch.begin(camera);
        modelBatch.render(instances, environment);
        modelBatch.end();

        for (Decal sprite : entityBillboards) {
            com.badlogic.gdx.math.Vector3 camDir = new com.badlogic.gdx.math.Vector3(-camera.direction.x, 0, -camera.direction.z).nor();
            sprite.setRotation(camDir, com.badlogic.gdx.math.Vector3.Y);
            decalBatch.add(sprite);
        }
        
        java.util.List<Decal> tempDecals = new java.util.ArrayList<>();
        for (fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.RoamingMonsterGroup mg : roamingMonsters) {
            Decal mgSprite = decalPool.obtain();
            mgSprite.setTextureRegion(monsterRegion);
            
            // Set dimensions
            if (mg.isBoss()) {
                mgSprite.setDimensions(1.5f, 2.0f);
            } else {
                mgSprite.setDimensions(1.2f, 1.8f);
            }
            
            // Set colors based on AI State
            if (mg.isBoss()) {
                mgSprite.setColor(Color.RED);
            } else {
                switch (mg.getState()) {
                    case SLEEPING:
                        mgSprite.setColor(Color.ROYAL); // Blueish for sleeping
                        break;
                    case ALERT:
                        mgSprite.setColor(Color.YELLOW);
                        break;
                    case CHASE:
                        mgSprite.setColor(Color.ORANGE);
                        break;
                    case CHARGE:
                        mgSprite.setColor(Color.SCARLET); // Deep red for charging
                        break;
                    case STUNNED:
                        mgSprite.setColor(Color.PURPLE); // Purple/dizzy
                        break;
                    case FLEE:
                        mgSprite.setColor(Color.GREEN);
                        break;
                    case PATROL:
                    case IDLE:
                    default:
                        mgSprite.setColor(Color.WHITE);
                        break;
                }
            }
            
            mgSprite.setPosition(mg.getX() * tileSize + (tileSize / 2f), 1.0f, mg.getZ() * tileSize + (tileSize / 2f));
            com.badlogic.gdx.math.Vector3 mgCamDir = new com.badlogic.gdx.math.Vector3(-camera.direction.x, 0, -camera.direction.z).nor();
            mgSprite.setRotation(mgCamDir, com.badlogic.gdx.math.Vector3.Y);
            decalBatch.add(mgSprite);
            tempDecals.add(mgSprite);
        }
        decalBatch.flush();
        for (Decal d : tempDecals) {
            decalPool.free(d);
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
        if (floorModel != null) floorModel.dispose();
        if (wallModel != null) wallModel.dispose();
        if (dungeonTex != null) dungeonTex.dispose();
        // Les textures heroTexture, monsterTexture, chestTexture, stairsTexture sont gÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©rÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©es par l'AssetProvider
    }
}


