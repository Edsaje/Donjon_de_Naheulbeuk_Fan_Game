package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.StatType;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.offensiveEquipment.OffensiveEquipment;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentCategory;

/**
 * Reprsente le Ranger dans la Compagnie de Naheulbeuk.
 * Chef d'quipe autoproclam, polyvalent mais moyen partout.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Ranger extends Character {

    /**
     * Initialise le Ranger avec ses statistiques de dpart et sa ressource nergie.
     */
    public Ranger() {
        super("Le Ranger", "Ranger", 1, 15, 0, 8, 4, 8, 6, 8);
        this.equip(new OffensiveEquipment("pe en fer", "Une arme basique mais fiable.", EquipmentCategory.LIGHT_WEAPON, 0, 0));
        this.setResourceName("nergie");
        this.setMaxResource(100);
        this.setCurrentResource(100);
        this.skills.add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill("Tir de Prcision", 30, "Un tir ajust qui inflige de lourds dgts.", false) {
            @Override
            public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult execute(Character user, Team team, Character monster) {
                int cost = getCost();
                if (user.getCurrentResource() >= cost) {
                    user.setCurrentResource(user.getCurrentResource() - cost);
                    int damage = Math.max(1, (user.getAttack() * 2) - monster.getDefense());
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










