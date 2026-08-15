package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * Fabrique de Sprites 2D avec chargement automatique des fichiers PNG ou fallback (SRP).
 * Vérifie l'existence des fichiers PNG dans "sprites/[nom].png" et les charge automatiquement.
 *
 * @author Hibouxe
 * @version 2.0
 */
public class SpriteFactory {

    /**
     * Charge le fichier PNG du Héros s'il existe dans sprites/[nom].png, sinon utilise la couleur fallback.
     *
     * @param className Nom du héros (ranger, nain, elfe, barbare, magicienne, ogre, voleuse)
     * @return Texture 2D du héros
     */
    public static Texture createHeroSprite(String className) {
        String name = className.toLowerCase();
        
        Color fallbackColor = Color.GOLD;

        if (name.contains("magician") || name.contains("magicienne")) fallbackColor = Color.PURPLE;
        else if (name.contains("ranger")) fallbackColor = Color.GREEN;
        else if (name.contains("elf") || name.contains("elfe")) fallbackColor = Color.TEAL;
        else if (name.contains("barbarian") || name.contains("barbare")) fallbackColor = Color.CORAL;

        return loadOrFallback(name, fallbackColor);
    }

    /**
     * Charge le fichier PNG du Monstre s'il existe dans sprites/[nom].png, sinon utilise la couleur fallback.
     *
     * @param monsterName Nom du monstre (orc, gobelin, squelette, troll, undead, golem, zangdar)
     * @return Texture 2D du monstre
     */
    public static Texture createMonsterSprite(String monsterName) {
        String name = monsterName.toLowerCase();
        return loadOrFallback(name, Color.FIREBRICK);
    }

    /**
     * Charge le fichier PNG du Coffres au trésor (chest.png) ou fallback.
     *
     * @return Texture 2D du coffre
     */
    public static Texture createChestSprite() {
        return loadOrFallback("chest", Color.CYAN);
    }

    /**
     * Charge le fichier PNG de l'Escalier (stairs.png) ou fallback.
     *
     * @return Texture 2D de l'escalier
     */
    public static Texture createStairsSprite() {
        return loadOrFallback("stairs", Color.WHITE);
    }

    private static Texture loadOrFallback(String filename, Color fallbackColor) {
        String[] possiblePaths = {
            "sprites/",
            
            "assets/sprites/"
        };
        
        try {
            if (Gdx.files != null) {
                for (String path : possiblePaths) {
                    System.out.println("[HD-2D] Looking for: " + path + filename + "_walk.png");
                    if (Gdx.files.internal(path + filename + "_walk.png").exists()) {
                        System.out.println("[HD-2D] Found: " + path + filename + "_walk.png");
                        return new Texture(Gdx.files.internal(path + filename + "_walk.png"));
                    }
                    if (Gdx.files.internal(path + filename + ".png").exists()) {
                        return new Texture(Gdx.files.internal(path + filename + ".png"));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[HD-2D] Error loading sprite: " + e.getMessage());
        }

        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(fallbackColor);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}
