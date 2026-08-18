package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy;

import com.badlogic.gdx.utils.JsonValue;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.ai.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.DataManager;

public class Monster extends Character {
    private String spriteName;

    public Monster(String id) {
        this(DataManager.getMonsterData(id));
    }

    public Monster(JsonValue data) {
        super(
            data.getString("name"),
            "Monster",
            1, // level
            data.getInt("hp", 10),
            data.getInt("mp", 0),
            data.getInt("attack", 0),
            0, // magicAttack
            data.getInt("defense", 0),
            0, // magicDefense
            data.getInt("speed", 0)
        );
        this.setXp(data.getInt("xpYield", 0));
        this.spriteName = data.getString("spriteName", "");
        
        String tacticsName = data.getString("aiTactics", "WarriorTactics");
        if ("CowardTactics".equals(tacticsName)) {
            this.tactics = new CowardTactics();
        } else if ("AmbushTactics".equals(tacticsName)) {
            this.tactics = new AmbushTactics();
        } else {
            this.tactics = new WarriorTactics();
        }
    }

    public String getSpriteName() {
        return spriteName;
    }
}
