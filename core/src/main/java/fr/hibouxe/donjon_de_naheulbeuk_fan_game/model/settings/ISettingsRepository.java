package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.settings;

public interface ISettingsRepository {
    boolean getBoolean(String key, boolean defaultValue);
    void putBoolean(String key, boolean value);
    int getInteger(String key, int defaultValue);
    void putInteger(String key, int value);
    float getFloat(String key, float defaultValue);
    void putFloat(String key, float value);
    void flush();
}
