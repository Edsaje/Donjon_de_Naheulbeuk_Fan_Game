package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.ai.*;

public class Monster extends Character {
    private String spriteName;
    private int goldYield;
    private java.util.List<MonsterDef.DropDef> lootDrops;

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
        this.goldYield = data.goldYield;
        this.lootDrops = data.lootDrops != null ? new java.util.ArrayList<>(data.lootDrops) : new java.util.ArrayList<>();
    }

    public int getGoldYield() { return goldYield; }
    
    /**
     * Calcule aléatoirement le butin lâché par ce monstre.
     */
    public java.util.List<String> rollLoot() {
        java.util.List<String> dropped = new java.util.ArrayList<>();
        for (MonsterDef.DropDef drop : lootDrops) {
            if (this.randomProvider.nextDouble() <= drop.chance) {
                dropped.add(drop.itemId);
            }
        }
        return dropped;
    }

    public String getSpriteName() {
        return spriteName;
    }
}
