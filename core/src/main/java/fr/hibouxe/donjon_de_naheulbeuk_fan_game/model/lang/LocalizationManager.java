package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.lang;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;

public class LocalizationManager {
    private Properties properties = new Properties();
    private String currentLanguage = "fr";

    public void loadLanguage(String langCode) {
        currentLanguage = langCode;
        properties.clear();
        try {
            // Option 1: Load from file system if we are running in IDE
            File file = new File("lang/dialogues_" + langCode + ".properties");
            if (file.exists()) {
                try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
                    properties.load(reader);
                }
            } else {
                // Option 2: Load from classpath if packaged in JAR
                InputStream in = LocalizationManager.class.getResourceAsStream("/lang/dialogues_" + langCode + ".properties");
                if (in != null) {
                    try (InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                        properties.load(reader);
                    }
                } else {
                    System.err.println("Warning: Could not find language file for: " + langCode);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading language file: " + e.getMessage());
        }
    }

    public String getString(String key) {
        if (properties.isEmpty()) {
            loadLanguage(currentLanguage);
        }
        return properties.getProperty(key, "[" + key + "]");
    }
}
