package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 * Fournit les ressources (sprites, textures) et gre la VRAM proprement via AssetManager.
 * Rsout les fuites de mmoire (Correction 2) et applique l'OCP (Correction 4).
 */
public class AssetProvider {
    private final AssetManager assetManager;
    private final Map<String, Color> heroFallbackColors = new HashMap<>();
    private final Map<Color, Texture> fallbackTextures = new HashMap<>();

    public AssetProvider() {
        this.assetManager = new AssetManager();
        
        // Configuration OCP pour les couleurs de hros
        heroFallbackColors.put("magician", Color.PURPLE);
        heroFallbackColors.put("magicienne", Color.PURPLE);
        heroFallbackColors.put("ranger", Color.GREEN);
        heroFallbackColors.put("elf", Color.TEAL);
        heroFallbackColors.put("elfe", Color.TEAL);
        heroFallbackColors.put("barbarian", Color.CORAL);
        heroFallbackColors.put("barbare", Color.CORAL);
    }

    public void loadAssets() {
        // Prchargement basique (optionnel si fait en lazy-load)
        // Les assets spcifiques seront chargs  la vole.
    }

    public void update() {
        assetManager.update();
    }

    public void finishLoading() {
        assetManager.finishLoading();
    }

    public void dispose() {
        assetManager.dispose();
        for (Texture t : fallbackTextures.values()) {
            t.dispose();
        }
        fallbackTextures.clear();
    }

    public Texture getHeroSprite(String className) {
        String name = className.toLowerCase();
        Color fallbackColor = heroFallbackColors.getOrDefault(name, Color.GOLD);
        return getTextureOrFallback(name, fallbackColor);
    }

    public Texture getMonsterSprite(String monsterName) {
        return getTextureOrFallback(monsterName.toLowerCase(), Color.FIREBRICK);
    }

    public Texture getChestSprite() {
        return getTextureOrFallback("chest", Color.CYAN);
    }

    public Texture getStairsSprite() {
        return getTextureOrFallback("stairs", Color.WHITE);
    }

    private Texture getTextureOrFallback(String filename, Color fallbackColor) {
        String[] possiblePaths = {
            "sprites/",
            "assets/sprites/"
        };
        
        for (String path : possiblePaths) {
            String walkPath = path + filename + "_walk.png";
            String normalPath = path + filename + ".png";
            
            if (Gdx.files.internal(walkPath).exists()) {
                if (!assetManager.isLoaded(walkPath)) {
                    assetManager.load(walkPath, Texture.class);
                    assetManager.finishLoadingAsset(walkPath);
                }
                return assetManager.get(walkPath, Texture.class);
            }
            if (Gdx.files.internal(normalPath).exists()) {
                if (!assetManager.isLoaded(normalPath)) {
                    assetManager.load(normalPath, Texture.class);
                    assetManager.finishLoadingAsset(normalPath);
                }
                return assetManager.get(normalPath, Texture.class);
            }
        }
        
        // Fallback gr proprement sans fuite mmoire
        if (!fallbackTextures.containsKey(fallbackColor)) {
            Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
            pixmap.setColor(fallbackColor);
            pixmap.fill();
            Texture texture = new Texture(pixmap);
            pixmap.dispose();
            fallbackTextures.put(fallbackColor, texture);
        }
        return fallbackTextures.get(fallbackColor);
    }
}
