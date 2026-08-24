package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.ai.*;

public class Monster extends Character {
    private String spriteName;

    public Monster(MonsterDef data) {
        super(
            data.name,
            "Monster",
            1, // level
            data.hp,
            data.mp,
            data.attack,
            0, // magicAttack
            data.defense,
            0, // magicDefense
            data.speed
        );
        this.setXp(data.xpYield);
        this.spriteName = data.spriteName;
        
        String tacticsName = data.aiTactics != null ? data.aiTactics : "WarriorTactics";
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
