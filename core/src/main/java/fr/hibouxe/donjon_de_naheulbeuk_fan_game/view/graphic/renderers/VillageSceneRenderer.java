package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.renderers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.utils.Disposable;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.hub.Village;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.AssetProvider;

import java.util.ArrayList;
import java.util.List;

public class VillageSceneRenderer implements Disposable {
    private AssetProvider assetProvider;
    private ObjLoader objLoader;

    private Model grassModel;
    private Model tavernModel;
    private Model treeModel;
    private Texture townTexture;
    private Texture grassTexture;

    private List<ModelInstance> instances = new ArrayList<>();
    private Decal playerDecal;

    public VillageSceneRenderer(AssetProvider assetProvider) {
        this.assetProvider = assetProvider;
        this.objLoader = new ObjLoader();
        loadAssets();
    }

    private void loadAssets() {
        try {
            townTexture = new Texture(com.badlogic.gdx.Gdx.files.internal("models/village/town_texture.png"), true);
            townTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
            
            grassTexture = new Texture(com.badlogic.gdx.Gdx.files.internal("models/village/grass_texture.png"), true);
            grassTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);

            com.badlogic.gdx.graphics.g3d.loader.ObjLoader.ObjLoaderParameters params = new com.badlogic.gdx.graphics.g3d.loader.ObjLoader.ObjLoaderParameters();
            params.flipV = true;

            // Le sol en herbe
            grassModel = objLoader.loadModel(com.badlogic.gdx.Gdx.files.internal("models/village/Grass_Tile_01.obj"), params);
            grassModel.materials.get(0).set(TextureAttribute.createDiffuse(grassTexture));

            // La taverne
            tavernModel = objLoader.loadModel(com.badlogic.gdx.Gdx.files.internal("models/village/Inn_Stone_01.obj"), params);
            tavernModel.materials.get(0).set(TextureAttribute.createDiffuse(townTexture));
            
            // Les limites (rochers)
            try {
                treeModel = objLoader.loadModel(com.badlogic.gdx.Gdx.files.internal("models/village/Mountain_Tile_01.obj"), params);
                treeModel.materials.get(0).set(TextureAttribute.createDiffuse(grassTexture));
            } catch (Exception e) {
                com.badlogic.gdx.Gdx.app.log("VillageSceneRenderer", "Mountain_Tile_01.obj non trouvé.");
            }
            
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("VillageSceneRenderer", "Failed to load village assets", e);
        }
    }

    public void buildScene(Village village) {
        instances.clear();

        int boundX = 10;
        int boundZ = 10;

        if (grassModel != null) {
            // Generer un grand sol en herbe
            for (int x = -boundX - 2; x <= boundX + 2; x++) {
                for (int z = -boundZ - 2; z <= boundZ + 2; z++) {
                    ModelInstance grass = new ModelInstance(grassModel);
                    grass.transform.setToTranslation(x * 1.0f, 0, z * 1.0f);
                    instances.add(grass);
                }
            }
        }

        // Placer les arbres autour (Limites)
        if (treeModel != null) {
            for (int x = -boundX; x <= boundX; x++) {
                for (int z = -boundZ; z <= boundZ; z++) {
                    if (x == -boundX || x == boundX || z == -boundZ || z == boundZ) {
                        // Sauf la porte d'entree vers le Sud (Donjons)
                        if (z == boundZ && x >= -1 && x <= 1) continue;

                        ModelInstance tree = new ModelInstance(treeModel);
                        tree.transform.setToTranslation(x * 1.0f, 0, z * 1.0f);
                        instances.add(tree);
                    }
                }
            }
        }

        // Placer la Taverne (si niveau > 0)
        if (village != null && village.getTavernLevel() > 0 && tavernModel != null) {
            ModelInstance tavern = new ModelInstance(tavernModel);
            tavern.transform.setToTranslation(0f, 0f, -8f);
            instances.add(tavern);
        }
        
        // Setup Player Decal
        Texture rangerTex = assetProvider.getHeroSprite("ranger");
        TextureRegion rangerReg = new TextureRegion(rangerTex, 0, 0, 64, 64);
        playerDecal = Decal.newDecal(1.5f, 1.5f, rangerReg, true);
    }

    public void render(ModelBatch modelBatch, DecalBatch decalBatch, Environment environment, Camera camera, float playerX, float playerZ) {
        if (instances.isEmpty()) {
            // Fallback (on devrait appeler buildScene depuis le Controller ou GdxApp au changement d'etat, on le fait ici en urgence si vide)
            buildScene(new Village());
        }

        modelBatch.begin(camera);
        for (ModelInstance instance : instances) {
            modelBatch.render(instance, environment);
        }
        modelBatch.end();

        if (playerDecal != null) {
            playerDecal.setPosition(playerX, 0.75f, playerZ);
            playerDecal.lookAt(camera.position, camera.up);
            decalBatch.add(playerDecal);
            decalBatch.flush();
        }
    }

    @Override
    public void dispose() {
        if (grassModel != null) grassModel.dispose();
        if (tavernModel != null) tavernModel.dispose();
        if (townTexture != null) townTexture.dispose();
        if (grassTexture != null) grassTexture.dispose();
    }
}

