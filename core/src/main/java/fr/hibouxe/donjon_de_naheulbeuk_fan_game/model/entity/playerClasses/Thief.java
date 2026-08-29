package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.StatType;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.offensiveEquipment.OffensiveEquipment;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentCategory;

public class Thief extends Character {
    public Thief() {
        super("Le Voleur", "Voleur", 1, 5, 100, 3, 1, 5, 5, 16);
        this.equip(new OffensiveEquipment("Dague rouille", "Un outil discret pour les coups en tratre.", EquipmentCategory.LIGHT_WEAPON, 0, 0));
        this.setResourceName("nergie");
        this.setMaxResource(100);
        this.setCurrentResource(100);
        this.skills.add(new Skill("Attaque Sournoise", 30, "Une attaque pernicieuse dans le dos.", false) {
            @Override
            public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult execute(Character user, Team team, Character monster) {
                int cost = getCost();
                if (user.getCurrentResource() >= cost) {
                    user.setCurrentResource(user.getCurrentResource() - cost);
                    int damage = Math.max(1, (user.getAttack() * 2) - (monster.getDefense() / 2));
                    monster.setHealthPoint(monster.getHealthPoint() - damage);
                    return new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult(true, damage, "DAMAGE");
                } else {
                    return new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult(false, 0, "ERROR");
                }
            }
        });
    }
    
    @Override
    public int levelUp() {
        int levels = super.levelUp();
        increaseStat(StatType.PV_MAX, 1, 3);
        increaseStat(StatType.PM_MAX, 1, 3);
        increaseStat(StatType.ATTAQUE, 1, 3);
        increaseStat(StatType.ATTAQUE_MAGIQUE, 1, 3);
        increaseStat(StatType.VITESSE, 1, 3);
        increaseStat(StatType.DEFENSE, 1, 3);
        return levels;
    }
}









