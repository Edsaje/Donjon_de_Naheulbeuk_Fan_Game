package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.StatType;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.offensiveEquipment.OffensiveEquipment;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentCategory;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.boss.Golem;

import java.util.Random;

/**
 * Reprsente l'Elfe dans la Compagnie de Naheulbeuk.
 * Spcialise dans les attaques  distance (arc) et quelques sorts mineurs, mais trs fragile.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Elf extends Character {
    private Random random = new Random();

    /**
     * Initialise l'Elfe avec ses statistiques de dpart et sa ressource Mana.
     * Possde "Soin Magique" et "Tir Prcis (ou presque)" ds le niveau 1.
     */
    public Elf() {
        super("L'Elfe", "Elfe", 1, 6, 100, 6, 3, 5, 5, 20);
        this.equip(new OffensiveEquipment("Arc de chasse", "Pour tirer de loin, trs loin.", EquipmentCategory.RANGE_WEAPON, 0, 0));
        this.setResourceName("Mana");
        this.setMaxResource(100);
        this.setCurrentResource(100);
        this.skills.add(new Skill("Soin Magique", 15, "Rend 15 PV  un alli.", true) {
            @Override
            public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult execute(Character user, Team team, Character target) {
                if (user.getCurrentResource() >= getCost()) {
                    user.setCurrentResource(user.getCurrentResource() - getCost());
                    if (target != null) {
                        target.setHealthPoint(target.getHealthPoint() + 15);
                        return new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult(true, 15, "HEAL");
                    }
                }
                return new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult(false, 0, "ERROR");
            }
        });
        
        this.skills.add(new Skill("Tir Prcis (ou presque)", 15, "Un tir  l'arc ajust. Risque lev de toucher un coquipier !", false) {
            @Override
            public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult execute(Character user, Team team, Character target) {
                if (user.getCurrentResource() >= getCost()) {
                    user.setCurrentResource(user.getCurrentResource() - getCost());
                    if (target != null) {
                        int damage = user.getAttack() + 10;
                        target.setHealthPoint(target.getHealthPoint() - damage);
                        return new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult(true, damage, "DAMAGE");
                    }
                }
                return new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult(false, 0, "ERROR");
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









