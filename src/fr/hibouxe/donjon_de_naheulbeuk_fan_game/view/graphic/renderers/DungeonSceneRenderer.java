package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.renderers;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.SpriteFactory;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp;

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
    private Model wallBlockModel;
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

    private float tileSize = 2.0f;

    public DungeonSceneRenderer() {
        heroTexture = SpriteFactory.createHeroSprite("Ranger");
        // Découpage automatique si l'image fait 256x256 (64x64 par frame)
        if (heroTexture.getWidth() >= 256) {
            heroFrames = TextureRegion.split(heroTexture, 64, 64);
        } else {
            heroFrames = new TextureRegion[1][1];
            heroFrames[0][0] = new TextureRegion(heroTexture);
        }
        
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

    public void setLeaderClass(String className) {
        if (heroTexture != null) {
            heroTexture.dispose();
        }
        heroTexture = SpriteFactory.createHeroSprite(className);
        if (heroTexture.getWidth() >= 256) {
            heroFrames = TextureRegion.split(heroTexture, 64, 64);
        } else {
            heroFrames = new TextureRegion[1][1];
            heroFrames[0][0] = new TextureRegion(heroTexture);
        }
    }

    public void buildScene(Dungeon dungeon, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team, int playerX, int playerY, int currentFloor) {
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
                        stairsSprite.setPosition(posX, 1.0f, posZ);
                        entityBillboards.add(stairsSprite);
                    }
                    
                    // SCRIPT ELFE (Tutoriel - étage 2)
                    if (currentFloor == 2 && x == 1 && y == 2) {
                        boolean elfSaved = false;
                        for (fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character c : team.getMembers()) {
                            if (c.getClass().getSimpleName().equals("Elf")) {
                                elfSaved = true;
                                break;
                            }
                        }
                        if (!elfSaved) {
                            Texture elfTex = SpriteFactory.createHeroSprite("Elf");
                            if (elfTex != null) {
                                TextureRegion[][] elfFrames = TextureRegion.split(elfTex, 64, 64);
                                Decal elfSprite = Decal.newDecal(1.8f, 1.8f, elfFrames[0][0], true);
                                elfSprite.setPosition(posX, 1.0f, posZ);
                                entityBillboards.add(elfSprite);
                            }
                        }
                    }
                } else {
                    ModelInstance wallBlock = new ModelInstance(wallBlockModel);
                    wallBlock.transform.setToTranslation(posX, 1.25f, posZ);
                    instances.add(wallBlock);
                }
            }
        }
    // lastFloor tracker is moved outside
        float currentX = playerX * tileSize;
        float currentZ = playerY * tileSize;
        if (heroSprite != null && lastFloor == currentFloor) {
            currentX = heroSprite.getX();
            currentZ = heroSprite.getZ();
        }
        lastFloor = currentFloor;

        TextureRegion initialFrame = heroFrames.length > 0 && heroFrames[0].length > 0 ? heroFrames[0][0] : new TextureRegion(heroTexture);
        heroSprite = Decal.newDecal(1.4f, 2.0f, initialFrame, true);
        heroSprite.setPosition(currentX, 1.0f, currentZ);
        entityBillboards.add(heroSprite);
    }

    public void render(ModelBatch modelBatch, DecalBatch decalBatch, Environment environment, PerspectiveCamera camera, int playerX, int playerY, int playerDirection) {
        this.currentDirection = playerDirection;
        // Animation et déplacement fluide du sprite
        this.targetSpriteX = playerX * tileSize;
        this.targetSpriteZ = playerY * tileSize;

        float currentSpriteX = heroSprite.getX();
        float currentSpriteZ = heroSprite.getZ();

        float diffX = targetSpriteX - currentSpriteX;
        float diffZ = targetSpriteZ - currentSpriteZ;

        // Vitesse de déplacement très ralentie (4.0f) pour le style Donjon Mystère
        float moveSpeed = 4.0f * com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        
        if (Math.abs(diffX) > moveSpeed || Math.abs(diffZ) > moveSpeed) {
            if (Math.abs(diffX) > moveSpeed) {
                currentSpriteX += Math.signum(diffX) * moveSpeed;
            } else {
                currentSpriteX = targetSpriteX; // On snap pour éviter le tremblement (overshoot)
            }
            
            if (Math.abs(diffZ) > moveSpeed) {
                currentSpriteZ += Math.signum(diffZ) * moveSpeed;
            } else {
                currentSpriteZ = targetSpriteZ; // On snap pour éviter le tremblement (overshoot)
            }
            
            this.isAnimating = true;
        } else {
            currentSpriteX = targetSpriteX;
            currentSpriteZ = targetSpriteZ;
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
        if (wallBlockModel != null) wallBlockModel.dispose();
        if (heroTexture != null) heroTexture.dispose();
        if (monsterTexture != null) monsterTexture.dispose();
        if (chestTexture != null) chestTexture.dispose();
        if (stairsTexture != null) stairsTexture.dispose();
    }
}
