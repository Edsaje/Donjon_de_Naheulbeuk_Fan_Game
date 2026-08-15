package fr.hibouxe.donjon_de_naheulbeuk_fan_game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.settings.ISettingsRepository;

public class LibGDXSettingsRepository implements ISettingsRepository {
    private final String prefsName;
    private Preferences prefs;

    public LibGDXSettingsRepository(String prefsName) {
        this.prefsName = prefsName;
    }

    private Preferences getPrefs() {
        if (prefs == null) {
            prefs = Gdx.app.getPreferences(prefsName);
        }
        return prefs;
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return getPrefs().getBoolean(key, defaultValue);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        getPrefs().putBoolean(key, value);
    }

    @Override
    public int getInteger(String key, int defaultValue) {
        return getPrefs().getInteger(key, defaultValue);
    }

    @Override
    public void putInteger(String key, int value) {
        getPrefs().putInteger(key, value);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return getPrefs().getFloat(key, defaultValue);
    }

    @Override
    public void putFloat(String key, float value) {
        getPrefs().putFloat(key, value);
    }

    @Override
    public void flush() {
        if (prefs != null) {
            prefs.flush();
        }
    }
}
