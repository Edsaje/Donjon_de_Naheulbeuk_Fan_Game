package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view;

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

/**
 * Composant de rendu 3D spécialisé pour la scène de combat HD-2D Dragon Quest (SRP).
 * Gère l'alignement 3D des Héros au premier plan et des Monstres au fond de la salle.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class BattleArenaRenderer implements Disposable {
    private Array<Decal> battleBillboards = new Array<>();
    private Texture heroTexture;
    private Texture monsterTexture;

    public BattleArenaRenderer() {
        heroTexture = createColoredTexture(Color.GOLD);
        monsterTexture = createColoredTexture(Color.FIREBRICK);

        setupBattleArena();
    }

    private void setupBattleArena() {
        battleBillboards.clear();

        // 3 Héros alignés au premier plan (Z = 0)
        for (int i = 0; i < 3; i++) {
            Decal hero = Decal.newDecal(1.6f, 2.4f, new TextureRegion(heroTexture), true);
            hero.setPosition(-3.0f + i * 3.0f, 1.0f, 0.0f);
            battleBillboards.add(hero);
        }

        // 3 Monstres alignés au fond de la salle (Z = -10.0m)
        for (int i = 0; i < 3; i++) {
            Decal monster = Decal.newDecal(1.8f, 2.6f, new TextureRegion(monsterTexture), true);
            monster.setPosition(-3.0f + i * 3.0f, 1.2f, -10.0f);
            battleBillboards.add(monster);
        }
    }

    public void render(ModelBatch modelBatch, DecalBatch decalBatch, Environment environment, PerspectiveCamera camera) {
        camera.position.set(0.0f, 6.0f, 10.0f);
        camera.lookAt(0.0f, 0.0f, -5.0f);
        camera.update();

        for (Decal sprite : battleBillboards) {
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
        if (heroTexture != null) heroTexture.dispose();
        if (monsterTexture != null) monsterTexture.dispose();
    }
}
