package fr.hibouxe.donjon_de_naheulbeuk_fan_game.infrastructure;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy.MonsterDef;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class JsonMonsterLoader implements IMonsterRepository {
    private final Map<String, MonsterDef> monsterCache = new HashMap<>();

    public JsonMonsterLoader(String jsonFilePath) {
        loadMonsters(jsonFilePath);
    }

    private void loadMonsters(String filePath) {
        JsonReader reader = new JsonReader();
        JsonValue root;
        try (InputStream is = new FileInputStream(filePath)) {
            root = reader.parse(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load monsters.json at " + filePath, e);
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

