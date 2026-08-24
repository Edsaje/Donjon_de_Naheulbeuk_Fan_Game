package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.StatType;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.boss.Golem;

import java.util.Random;

/**
 * Représente l'Elfe dans la Compagnie de Naheulbeuk.
 * Spécialisée dans les attaques à distance (arc) et quelques sorts mineurs, mais très fragile.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Elf extends Character {
    private Random random = new Random();

    /**
     * Initialise l'Elfe avec ses statistiques de départ et sa ressource Mana.
     * Possède "Soin Magique" et "Tir Précis (ou presque)" dès le niveau 1.
     */
    public Elf() {
        super("L'Elfe", "Elfe", 1, 6, 100, 6, 3, 5, 5, 20);
        this.setResourceName("Mana");
        this.setMaxResource(100);
        this.setCurrentResource(100);
        this.skills.add(new Skill("Soin Magique", 15, "Rend 15 PV à un allié.", true));
        this.skills.add(new Skill("Tir Précis (ou presque)", 15, "Un tir à l'arc ajusté. Risque élevé de toucher un coéquipier !", false));
    }

    /**
     * Exécute les compétences spéciales de l'Elfe (Soin Magique ou Tir Précis).
     *
     * @param skill  La compétence utilisée
     * @param team   La compagnie de héros
     * @param target Le monstre affronté
     * @return Message de résultat
     */
    @Override
    public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult useSpecialSkill(Skill skill, Team team, Character target) {
        if (skill.getName().contains("Soin")) {
            if (this.getCurrentResource() >= skill.getCost()) {
                this.setCurrentResource(this.getCurrentResource() - skill.getCost());
                if (target != null) {
                    target.setHealthPoint(target.getHealthPoint() + 15);
                    return new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult(true, 15, "HEAL");
                }
            }
        } else {
            if (this.getCurrentResource() >= skill.getCost()) {
                this.setCurrentResource(this.getCurrentResource() - skill.getCost());
                if (target != null) {
                    int damage = this.getAttack() + 10;
                    target.setHealthPoint(target.getHealthPoint() - damage);
                    return new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult(true, damage, "DAMAGE");
                }
            }
        }
        return new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult(false, 0, "ERROR");
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






