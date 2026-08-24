package fr.hibouxe.donjon_de_naheulbeuk_fan_game.infrastructure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy.MonsterDef;

import java.util.HashMap;
import java.util.Map;

public class JsonMonsterLoader implements IMonsterRepository {
    private final Map<String, MonsterDef> monsterCache = new HashMap<>();

    private static JsonMonsterLoader instance;
    public static JsonMonsterLoader getInstance() { if (instance == null) instance = new JsonMonsterLoader(); return instance; }
    public JsonMonsterLoader() {
        loadMonsters();
    }

    private void loadMonsters() {
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
            MonsterDef def = new MonsterDef();
            def.id = monster.getString("id");
            def.name = monster.getString("name");
            def.hp = monster.getInt("hp", 10);
            def.mp = monster.getInt("mp", 0);
            def.attack = monster.getInt("attack", 0);
            def.defense = monster.getInt("defense", 0);
            def.speed = monster.getInt("speed", 0);
            def.xpYield = monster.getInt("xpYield", 0);
            def.spriteName = monster.getString("spriteName", "");
            def.aiTactics = monster.getString("aiTactics", "WarriorTactics");
            monsterCache.put(def.id, def);
        }
    }

    @Override
    public MonsterDef getMonsterData(String id) {
        MonsterDef data = monsterCache.get(id);
        if (data == null) {
            throw new IllegalArgumentException("Unknown monster id: " + id);
        }
        return data;
    }
}

