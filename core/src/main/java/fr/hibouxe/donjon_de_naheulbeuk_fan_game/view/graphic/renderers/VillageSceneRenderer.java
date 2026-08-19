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
            params.flipV = false;

            // Construire un modèle d'herbe sur mesure avec ModelBuilder et TextureRegion (évite les bugs UV de Blender)
            com.badlogic.gdx.graphics.g3d.utils.ModelBuilder modelBuilder = new com.badlogic.gdx.graphics.g3d.utils.ModelBuilder();
            TextureRegion grassReg = new TextureRegion(grassTexture, 0, 0, 32, 32); // Coin en haut à gauche = Herbe pure
            com.badlogic.gdx.graphics.g3d.Material grassMat = new com.badlogic.gdx.graphics.g3d.Material(TextureAttribute.createDiffuse(grassReg));
            
            modelBuilder.begin();
            com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder mpb = modelBuilder.part("grass", com.badlogic.gdx.graphics.GL20.GL_TRIANGLES, com.badlogic.gdx.graphics.VertexAttributes.Usage.Position | com.badlogic.gdx.graphics.VertexAttributes.Usage.Normal | com.badlogic.gdx.graphics.VertexAttributes.Usage.TextureCoordinates, grassMat);
            mpb.setUVRange(grassReg);
            
            // Créer un carré 1x1 au sol
            mpb.rect(
                -1f, 0f, 0f,
                0f, 0f, 0f,
                0f, 0f, -1f,
                -1f, 0f, -1f,
                0f, 1f, 0f
            );
            grassModel = modelBuilder.end();

            // La taverne
            tavernModel = objLoader.loadModel(com.badlogic.gdx.Gdx.files.internal("models/village/Inn_Stone_01.obj"), params);
            tavernModel.materials.get(0).set(TextureAttribute.createDiffuse(townTexture));
            
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("VillageSceneRenderer", "Failed to load village assets", e);
        }
    }

    public void buildScene(Village village) {
        instances.clear();

        if (grassModel != null) {
            // Generer un grand sol en herbe sans trou (le modele fait 1x1, on translate de 1.0f)
            for (int x = -20; x <= 20; x++) {
                for (int z = -20; z <= 20; z++) {
                    ModelInstance grass = new ModelInstance(grassModel);
                    grass.transform.setToTranslation(x * 1.0f, 0, z * 1.0f);
                    instances.add(grass);
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

