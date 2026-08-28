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
 * Composant de rendu 3D spÃƒÂ©cialisÃƒÂ© pour la scÃƒÂ¨ne de combat HD-2D Dragon Quest (SRP).
 * GÃƒÂ¨re la disposition tactique des 7 HÃƒÂ©ros sur 3 lignes (Backline, Midline, Frontline)
 * et des Monstres au fond de l'arÃƒÂ¨ne de combat avec centrage dynamique.
 *
 * @author Hibouxe
 * @version 2.0
 */
public class BattleArenaRenderer implements Disposable {
    private Array<Decal> battleBillboards = new Array<>();
    private java.util.Map<Character, Decal> characterDecals = new java.util.HashMap<>();
    
    public class ActiveVFX {
        public Character target;
        public String type;
        public float timer;
        public float maxTimer;
        public int damage;
        public com.badlogic.gdx.math.Vector3 originalPos;
        
        ActiveVFX(Character target, String type, int damage, com.badlogic.gdx.math.Vector3 originalPos) {
            this.target = target;
            this.type = type;
            this.damage = damage;
            this.timer = 0f;
            this.maxTimer = 1.0f; // 1 sec d'effet
            this.originalPos = originalPos;
        }
    }
    
    private List<ActiveVFX> activeVFXs = new java.util.concurrent.CopyOnWriteArrayList<>();
    public List<ActiveVFX> getActiveVFXs() { return activeVFXs; }

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
     * Disposition par dÃƒÂ©faut des 7 HÃƒÂ©ros de la Compagnie de Naheulbeuk rÃƒÂ©partis sur 3 Lignes Tactiques en quinconce.
     */
    public void setupDefaultBattleArena() {
        decalPool.freeAll(battleBillboards);
        battleBillboards.clear();

        // 1. Ligne ArriÃƒÂ¨re (Backline - Z = 3.0m) : Magicienne & Elfe (2 HÃƒÂ©ros Violet - Ãƒâ€°carter ÃƒÂ  X = -2.8m et +2.8m)
        placeRowCustom(2, 3.0f, 1.0f, mageRegion, 1.4f, 2.2f, 4.6f);

        // 2. Ligne MÃƒÂ©diane (Midline - Z = 1.5m) : Le Ranger & La Voleuse (2 HÃƒÂ©ros Vert - IntercalÃƒÂ©s ÃƒÂ  X = -1.6m et +1.6m)
        placeRowCustom(2, 1.5f, 1.0f, rangerRegion, 1.5f, 2.3f, 2.8f);

        // 3. Ligne de Front (Frontline - Z = 0.0m) : Barbare, Nain, Ogre (3 HÃƒÂ©ros DorÃƒÂ© - Ãƒâ€°largis ÃƒÂ  X = -3.2m, 0.0m, +3.2m)
        placeRowCustom(3, 0.0f, 1.0f, heroRegion, 1.6f, 2.4f, 3.2f);

        // 4. Groupe d'Ennemis (Au fond de l'arÃƒÂ¨ne - Z = -10.0m) : 4 Monstres
        placeRowCustom(4, -10.0f, 1.2f, monsterRegion, 1.8f, 2.6f, 3.0f);
    }

    /**
     * Disposition tactique personnalisÃƒÂ©e selon les membres vivants de l'ÃƒÂ©quipe du joueur.
     *
     * @param team ÃƒÂ©quipe de hÃƒÂ©ros du joueur
     */
    public void setupTeamBattleArena(Team team, List<Character> monsters) {
        characterDecals.clear();
        decalPool.freeAll(battleBillboards);
        battleBillboards.clear();
        activeVFXs.clear();

        if (team == null || team.getMembers() == null || team.getMembers().isEmpty()) {
            setupDefaultBattleArena();
            return;
        }

        List<Character> backline = new ArrayList<>();
        List<Character> midline = new ArrayList<>();
        List<Character> frontline = new ArrayList<>();

        for (Character member : team.getMembers()) {
            if (member.getHealthPoint() <= 0) continue; // Ignorer les héros KO

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

        // Placer les 3 lignes avec centrage dynamique et décalage en quinconce
        placeCharacterRowCustom(backline, 3.0f, 1.0f, mageRegion, 1.4f, 2.2f, 5.6f);
        placeCharacterRowCustom(midline, 1.5f, 1.0f, rangerRegion, 1.5f, 2.3f, 3.2f);
        placeCharacterRowCustom(frontline, 0.0f, 1.0f, heroRegion, 1.6f, 2.4f, 3.2f);

        // Groupe d'Ennemis au fond
        if (monsters != null) {
            placeCharacterRowCustom(monsters, -10.0f, 1.2f, monsterRegion, 1.8f, 2.6f, 3.0f);
        }
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
            characterDecals.put(list.get(i), decal);
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

    public void playHitAnimation(Character target, int damage, String vfxType) {
        if (characterDecals.containsKey(target)) {
            Decal d = characterDecals.get(target);
            activeVFXs.add(new ActiveVFX(target, vfxType, damage, new com.badlogic.gdx.math.Vector3(d.getX(), d.getY(), d.getZ())));
        }
    }

    public void render(ModelBatch modelBatch, DecalBatch decalBatch, Environment environment, PerspectiveCamera camera) {
        // Caméra de combat panoramique à -25° cadrant toute la formation tactique
        camera.position.set(0.0f, 7.5f, 12.5f);
        camera.lookAt(0.0f, 0.5f, -4.0f);
        camera.position.y = 7.5f + (float)Math.sin((System.currentTimeMillis() % 10000)/1000.0f * 0.5f) * 0.5f;
        camera.update();

        if (arenaInstance != null) {
            modelBatch.begin(camera);
            modelBatch.render(arenaInstance, environment);
            modelBatch.end();
        }

        float delta = Gdx.graphics.getDeltaTime();
        
        // Update VFX (Shake effects)
        java.util.List<ActiveVFX> toRemove = new java.util.ArrayList<>();
        for (ActiveVFX vfx : activeVFXs) {
            vfx.timer += delta;
            
            if (characterDecals.containsKey(vfx.target)) {
                Decal d = characterDecals.get(vfx.target);
                if (vfx.timer < vfx.maxTimer) {
                    // Shake
                    float shake = (float)Math.sin(vfx.timer * 40f) * 0.2f; // Tremblement rapide sur l'axe X
                    d.setX(vfx.originalPos.x + shake);
                    
                    // TODO: Tinting (Color flashing) can be added here if we modify the shader
                    // For now, we simulate hit by scaling slightly or just shaking violently
                    d.setScale(1.0f - (vfx.timer * 0.1f)); 
                } else {
                    // Restore
                    d.setX(vfx.originalPos.x);
                    d.setScale(1.0f);
                    toRemove.add(vfx);
                }
            } else {
                toRemove.add(vfx); // Target decal was removed
            }
        }
        activeVFXs.removeAll(toRemove);

        for (Decal sprite : battleBillboards) {
            com.badlogic.gdx.math.Vector3 camDir = new com.badlogic.gdx.math.Vector3(-camera.direction.x, 0, -camera.direction.z).nor();
            sprite.setRotation(camDir, com.badlogic.gdx.math.Vector3.Y);
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
