package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire de paramètres centralisé (Singleton).
 * Gère la lecture, la sauvegarde et la notification des changements de paramètres.
 * Digne d'une architecture de jeu professionnel.
 */
public class GameSettingsManager {

    private static GameSettingsManager instance;
    private final Preferences prefs;
    private final List<SettingsListener> listeners = new ArrayList<>();

    // --- Constantes des clés de sauvegarde ---
    private static final String PREFS_NAME = "NaheulbeukFanGameSettings";
    
    // Affichage
    private static final String KEY_FULLSCREEN = "display_fullscreen";
    private static final String KEY_VSYNC = "display_vsync";
    private static final String KEY_FRAMERATE_LIMIT = "display_framerate_limit"; // 0 = unlimited, 60, 120, 144
    private static final String KEY_UI_SCALE = "display_ui_scale";

    // Audio
    private static final String KEY_MASTER_VOLUME = "audio_master_volume";
    private static final String KEY_BGM_VOLUME = "audio_bgm_volume";
    private static final String KEY_SFX_VOLUME = "audio_sfx_volume";
    private static final String KEY_MUTE_ALL = "audio_mute_all";

    // Gameplay
    private static final String KEY_TEXT_SPEED = "gameplay_text_speed"; // 0: Lent, 1: Normal, 2: Rapide, 3: Instantané
    private static final String KEY_AUTO_RUN = "gameplay_auto_run"; // Courir par défaut
    private static final String KEY_MOVEMENT_SPEED = "gameplay_movement_speed"; // Multiplicateur de vitesse

    // --- Variables en cache (pour la performance) ---
    private boolean fullscreen;
    private boolean vsync;
    private int framerateLimit;
    private float uiScale;

    private float masterVolume;
    private float bgmVolume;
    private float sfxVolume;
    private boolean muteAll;

    private int textSpeed;
    private boolean autoRun;
    private float movementSpeed;

    private GameSettingsManager() {
        prefs = Gdx.app.getPreferences(PREFS_NAME);
        loadSettings();
    }

    public static GameSettingsManager getInstance() {
        if (instance == null) {
            instance = new GameSettingsManager();
        }
        return instance;
    }

    /**
     * Charge tous les paramètres depuis le fichier (ou applique les valeurs par défaut).
     */
    private void loadSettings() {
        fullscreen = prefs.getBoolean(KEY_FULLSCREEN, false);
        vsync = prefs.getBoolean(KEY_VSYNC, true);
        framerateLimit = prefs.getInteger(KEY_FRAMERATE_LIMIT, 60);
        uiScale = prefs.getFloat(KEY_UI_SCALE, 1.0f);

        masterVolume = prefs.getFloat(KEY_MASTER_VOLUME, 1.0f);
        bgmVolume = prefs.getFloat(KEY_BGM_VOLUME, 0.8f);
        sfxVolume = prefs.getFloat(KEY_SFX_VOLUME, 1.0f);
        muteAll = prefs.getBoolean(KEY_MUTE_ALL, false);

        textSpeed = prefs.getInteger(KEY_TEXT_SPEED, 1);
        autoRun = prefs.getBoolean(KEY_AUTO_RUN, false);
        movementSpeed = prefs.getFloat(KEY_MOVEMENT_SPEED, 1.0f);
    }

    /**
     * Sauvegarde tous les paramètres sur le disque.
     */
    public void saveSettings() {
        prefs.putBoolean(KEY_FULLSCREEN, fullscreen);
        prefs.putBoolean(KEY_VSYNC, vsync);
        prefs.putInteger(KEY_FRAMERATE_LIMIT, framerateLimit);
        prefs.putFloat(KEY_UI_SCALE, uiScale);

        prefs.putFloat(KEY_MASTER_VOLUME, masterVolume);
        prefs.putFloat(KEY_BGM_VOLUME, bgmVolume);
        prefs.putFloat(KEY_SFX_VOLUME, sfxVolume);
        prefs.putBoolean(KEY_MUTE_ALL, muteAll);

        prefs.putInteger(KEY_TEXT_SPEED, textSpeed);
        prefs.putBoolean(KEY_AUTO_RUN, autoRun);
        prefs.putFloat(KEY_MOVEMENT_SPEED, movementSpeed);

        prefs.flush(); // Écrit physiquement dans le fichier
    }

    // --- Système d'Observateur (Listeners) ---

    public interface SettingsListener {
        void onDisplaySettingsChanged();
        void onAudioSettingsChanged();
        void onGameplaySettingsChanged();
    }

    public void addListener(SettingsListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(SettingsListener listener) {
        listeners.remove(listener);
    }

    private void notifyDisplayChanged() {
        for (SettingsListener listener : listeners) listener.onDisplaySettingsChanged();
    }

    private void notifyAudioChanged() {
        for (SettingsListener listener : listeners) listener.onAudioSettingsChanged();
    }

    private void notifyGameplayChanged() {
        for (SettingsListener listener : listeners) listener.onGameplaySettingsChanged();
    }

    // --- Getters & Setters avec Notification ---

    // DISPLAY
    public boolean isFullscreen() { return fullscreen; }
    public void setFullscreen(boolean fullscreen) {
        if (this.fullscreen != fullscreen) {
            this.fullscreen = fullscreen;
            notifyDisplayChanged();
        }
    }

    public boolean isVsync() { return vsync; }
    public void setVsync(boolean vsync) {
        if (this.vsync != vsync) {
            this.vsync = vsync;
            notifyDisplayChanged();
        }
    }

    public int getFramerateLimit() { return framerateLimit; }
    public void setFramerateLimit(int framerateLimit) {
        if (this.framerateLimit != framerateLimit) {
            this.framerateLimit = framerateLimit;
            notifyDisplayChanged();
        }
    }

    public float getUiScale() { return uiScale; }
    public void setUiScale(float uiScale) {
        if (this.uiScale != uiScale) {
            this.uiScale = uiScale;
            notifyDisplayChanged();
        }
    }

    // AUDIO
    public float getMasterVolume() { return masterVolume; }
    public void setMasterVolume(float masterVolume) {
        this.masterVolume = masterVolume;
        notifyAudioChanged();
    }

    public float getBgmVolume() { return bgmVolume; }
    public void setBgmVolume(float bgmVolume) {
        this.bgmVolume = bgmVolume;
        notifyAudioChanged();
    }

    public float getSfxVolume() { return sfxVolume; }
    public void setSfxVolume(float sfxVolume) {
        this.sfxVolume = sfxVolume;
        notifyAudioChanged();
    }

    public boolean isMuteAll() { return muteAll; }
    public void setMuteAll(boolean muteAll) {
        this.muteAll = muteAll;
        notifyAudioChanged();
    }

    public float getEffectiveBgmVolume() {
        return muteAll ? 0f : bgmVolume * masterVolume;
    }

    public float getEffectiveSfxVolume() {
        return muteAll ? 0f : sfxVolume * masterVolume;
    }

    // GAMEPLAY
    public int getTextSpeed() { return textSpeed; }
    public void setTextSpeed(int textSpeed) {
        this.textSpeed = textSpeed;
        notifyGameplayChanged();
    }

    public boolean isAutoRun() { return autoRun; }
    public void setAutoRun(boolean autoRun) {
        this.autoRun = autoRun;
        notifyGameplayChanged();
    }

    public float getMovementSpeed() { return movementSpeed; }
    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
        notifyGameplayChanged();
    }
}
