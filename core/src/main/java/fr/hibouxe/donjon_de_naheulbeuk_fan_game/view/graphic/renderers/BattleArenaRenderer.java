package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.renderers;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

import java.util.ArrayList;
import java.util.List;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.TacticalRow;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.Gdx;

/**
 * Composant de rendu 3D spÃ©cialisÃ© pour la scÃ¨ne de combat HD-2D Dragon Quest (SRP).
 * GÃ¨re la disposition tactique des 7 HÃ©ros sur 3 lignes (Backline, Midline, Frontline)
 * et des Monstres au fond de l'arÃ¨ne de combat avec centrage dynamique.
 *
 * @author Hibouxe
 * @version 2.0
 */
public class BattleArenaRenderer implements Disposable {
    private Array<Decal> battleBillboards = new Array<>();
    private Texture heroTexture;
    private Texture mageTexture;
    private Texture rangerTexture;
    private Texture monsterTexture;

    private TextureRegion heroRegion;
    private TextureRegion mageRegion;
    private TextureRegion rangerRegion;
    private TextureRegion monsterRegion;

    private Model arenaModel;
    private ModelInstance arenaInstance;

    private final Pool<Decal> decalPool = new Pool<Decal>() {
        @Override
        protected Decal newObject() {
            return Decal.newDecal(1.0f, 1.0f, heroRegion, true);
        }
    };

    public BattleArenaRenderer(fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.AssetProvider assetProvider) {
        heroTexture = assetProvider.getHeroSprite("Barbarian");
        mageTexture = assetProvider.getHeroSprite("Magician");
        rangerTexture = assetProvider.getHeroSprite("Ranger");
        monsterTexture = assetProvider.getMonsterSprite("Orc");

        heroRegion = new TextureRegion(heroTexture);
        mageRegion = new TextureRegion(mageTexture);
        rangerRegion = new TextureRegion(rangerTexture);
        monsterRegion = new TextureRegion(monsterTexture);

        setupDefaultBattleArena();
    }

    /**
     * Disposition par dÃ©faut des 7 HÃ©ros de la Compagnie de Naheulbeuk rÃ©partis sur 3 Lignes Tactiques en quinconce.
     */
    public void setupDefaultBattleArena() {
        decalPool.freeAll(battleBillboards);
        battleBillboards.clear();

        // 1. Ligne ArriÃ¨re (Backline - Z = 3.0m) : Magicienne & Elfe (2 HÃ©ros Violet - Ã‰carter Ã  X = -2.8m et +2.8m)
        placeRowCustom(2, 3.0f, 1.0f, mageRegion, 1.4f, 2.2f, 4.6f);

        // 2. Ligne MÃ©diane (Midline - Z = 1.5m) : Le Ranger & La Voleuse (2 HÃ©ros Vert - IntercalÃ©s Ã  X = -1.6m et +1.6m)
        placeRowCustom(2, 1.5f, 1.0f, rangerRegion, 1.5f, 2.3f, 2.8f);

        // 3. Ligne de Front (Frontline - Z = 0.0m) : Barbare, Nain, Ogre (3 HÃ©ros DorÃ© - Ã‰largis Ã  X = -3.2m, 0.0m, +3.2m)
        placeRowCustom(3, 0.0f, 1.0f, heroRegion, 1.6f, 2.4f, 3.2f);

        // 4. Groupe d'Ennemis (Au fond de l'arÃ¨ne - Z = -10.0m) : 4 Monstres
        placeRowCustom(4, -10.0f, 1.2f, monsterRegion, 1.8f, 2.6f, 3.0f);
    }

    /**
     * Disposition tactique personnalisÃ©e selon les membres vivants de l'Ã©quipe du joueur.
     *
     * @param team Ã©quipe de hÃ©ros du joueur
     */
    public void setupTeamBattleArena(Team team) {
        if (team == null || team.getMembers() == null || team.getMembers().isEmpty()) {
            setupDefaultBattleArena();
            return;
        }

        decalPool.freeAll(battleBillboards);
        battleBillboards.clear();

        List<Character> backline = new ArrayList<>();
        List<Character> midline = new ArrayList<>();
        List<Character> frontline = new ArrayList<>();

        for (Character member : team.getMembers()) {
            if (member.getHealthPoint() <= 0) continue; // Ignorer les hÃ©ros KO

            TacticalRow row = member.getPreferredTacticalRow();
            if (row == TacticalRow.BACKLINE) {
                backline.add(member);
            } else if (row == TacticalRow.MIDLINE) {
                midline.add(member);
            } else {
                frontline.add(member);
            }
        }

        if (backline.isEmpty() && midline.isEmpty() && frontline.isEmpty()) {
            setupDefaultBattleArena();
            return;
        }

        // Placer les 3 lignes avec centrage dynamique et dÃ©calage en quinconce
        placeCharacterRowCustom(backline, 3.0f, 1.0f, mageRegion, 1.4f, 2.2f, 5.6f);
        placeCharacterRowCustom(midline, 1.5f, 1.0f, rangerRegion, 1.5f, 2.3f, 3.2f);
        placeCharacterRowCustom(frontline, 0.0f, 1.0f, heroRegion, 1.6f, 2.4f, 3.2f);

        // Groupe d'Ennemis au fond
        placeRowCustom(3, -10.0f, 1.2f, monsterRegion, 1.8f, 2.6f, 3.0f);
    }

    private void placeRowCustom(int count, float posZ, float posY, TextureRegion region, float width, float height, float stepX) {
        if (count <= 0) return;
        float startX = -(count - 1) * stepX / 2.0f;

        for (int i = 0; i < count; i++) {
            Decal decal = decalPool.obtain();
            decal.setTextureRegion(region);
            decal.setDimensions(width, height);
            decal.setPosition(startX + i * stepX, posY, posZ);
            battleBillboards.add(decal);
        }
    }

    private void placeCharacterRowCustom(List<Character> list, float posZ, float posY, TextureRegion region, float width, float height, float stepX) {
        int count = list.size();
        if (count <= 0) return;
        float startX = -(count - 1) * stepX / 2.0f;

        for (int i = 0; i < count; i++) {
            Decal decal = decalPool.obtain();
            decal.setTextureRegion(region);
            decal.setDimensions(width, height);
            decal.setPosition(startX + i * stepX, posY, posZ);
            battleBillboards.add(decal);
        }
    }

    public void setDungeonTheme(String theme) {
        if (arenaModel != null) arenaModel.dispose();
        try {
            ObjLoader loader = new ObjLoader();
            String path = "models/dungeon/" + theme + "/battle_arena.obj";
            if (Gdx.files.internal(path).exists()) {
                arenaModel = loader.loadModel(Gdx.files.internal(path));
                arenaInstance = new ModelInstance(arenaModel);
            } else { arenaInstance = null; }
        } catch(Exception e) { arenaInstance = null; }
    }

    public void render(ModelBatch modelBatch, DecalBatch decalBatch, Environment environment, PerspectiveCamera camera) {
        // CamÃ©ra de combat panoramique Ã  -25Ã  cadrant toute la formation tactique
        camera.position.set(0.0f, 7.5f, 12.5f);
        camera.lookAt(0.0f, 0.5f, -4.0f);
        camera.position.y = 7.5f + (float)Math.sin((System.currentTimeMillis() % 10000)/1000.0f * 0.5f) * 0.5f;
        camera.update();

        if (arenaInstance != null) {
            modelBatch.begin(camera);
            modelBatch.render(arenaInstance, environment);
            modelBatch.end();
        }

        for (Decal sprite : battleBillboards) {
            sprite.lookAt(new com.badlogic.gdx.math.Vector3(camera.position.x, sprite.getY(), camera.position.z), com.badlogic.gdx.math.Vector3.Y);
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
        if (arenaModel != null) arenaModel.dispose();
    }
}
