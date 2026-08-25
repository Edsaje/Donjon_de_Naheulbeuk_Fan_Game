package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.settings.GameSettingsManager;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static AudioManager instance;
    private GameSettingsManager settingsManager;

    private Map<String, Sound> sounds;
    private Music currentMusic;
    private String currentMusicPath;

    private AudioManager() {
        sounds = new HashMap<>();
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void setSettingsManager(GameSettingsManager settingsManager) {
        this.settingsManager = settingsManager;
        updateMusicVolume();
    }

    public float getSfxVolume() {
        return settingsManager != null ? settingsManager.getEffectiveSfxVolume() : 1.0f;
    }

    public float getMusicVolume() {
        return settingsManager != null ? settingsManager.getEffectiveBgmVolume() : 1.0f;
    }

    public void playSound(String path) {
        if (getSfxVolume() <= 0) return;
        
        if (!sounds.containsKey(path)) {
            try {
                Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
                sounds.put(path, sound);
            } catch (Exception e) {
                Gdx.app.error("Audio", "Could not load sound: " + path, e);
                return;
            }
        }
        sounds.get(path).play(getSfxVolume());
    }
    
    public void playUIHover() { playSound("audio/ui/cursor.wav"); }
    public void playUIAccept() { playSound("audio/ui/accept.wav"); }
    public void playUIOpen() { playSound("audio/ui/open.wav"); }
    public void playUIClose() { playSound("audio/ui/close.wav"); }

    public void playMusic(String path, boolean loop) {
        if (currentMusicPath != null && currentMusicPath.equals(path)) {
            return; // Already playing
        }
        
        stopMusic();
        
        try {
            currentMusic = Gdx.audio.newMusic(Gdx.files.internal(path));
            currentMusic.setLooping(loop);
            currentMusic.setVolume(getMusicVolume());
            currentMusic.play();
            currentMusicPath = path;
        } catch (Exception e) {
            Gdx.app.error("Audio", "Could not load music: " + path, e);
        }
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
            currentMusicPath = null;
        }
    }

    public void updateMusicVolume() {
        if (currentMusic != null) {
            currentMusic.setVolume(getMusicVolume());
        }
    }

    public void dispose() {
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
        stopMusic();
    }
}