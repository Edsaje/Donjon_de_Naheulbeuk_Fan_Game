package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy.Monster;

import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private static final Map<String, JsonValue> monsterCache = new HashMap<>();

    public static void loadMonsters() {
        if (!monsterCache.isEmpty()) return;
        JsonReader reader = new JsonReader();
        JsonValue root;
        try {
            if (Gdx.files != null) {
                root = reader.parse(Gdx.files.internal("data/monsters.json"));
            } else {
                if (new java.io.File("../assets/data/monsters.json").exists()) {
                    root = reader.parse(new java.io.FileReader("../assets/data/monsters.json"));
                } else if (new java.io.File("assets/data/monsters.json").exists()) {
                    root = reader.parse(new java.io.FileReader("assets/data/monsters.json"));
                } else {
                    throw new RuntimeException("Could not find monsters.json");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load monsters.json", e);
        }
        JsonValue monsters = root.get("monsters");
        for (JsonValue monster : monsters) {
            monsterCache.put(monster.getString("id"), monster);
        }
    }

    public static Monster createMonster(String id) {
        return new Monster(getMonsterData(id));
    }

    public static JsonValue getMonsterData(String id) {
        if (monsterCache.isEmpty()) {
            loadMonsters();
        }
        JsonValue data = monsterCache.get(id);
        if (data == null) {
            throw new IllegalArgumentException("Unknown monster id: " + id);
        }
        return data;
    }
}
