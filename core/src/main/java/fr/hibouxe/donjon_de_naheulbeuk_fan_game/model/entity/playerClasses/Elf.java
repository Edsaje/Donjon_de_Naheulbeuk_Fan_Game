package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.StatType;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.boss.Golem;

import java.util.Random;

/**
 * ReprÃƒÆ’Ã‚Â©sente l'ÃƒÆ’Ã¢â‚¬Â°lfette dans la Compagnie de Naheulbeuk.
 * HÃƒÆ’Ã‚Â©ros spÃƒÆ’Ã‚Â©cialisÃƒÆ’Ã‚Â© dans le soutien, les soins magiques et le Tir PrÃƒÆ’Ã‚Â©cis (ou presque).
 *
 * @author Hibouxe
 * @version 2.0
 */
public class Elf extends Character {
    private Random random = new Random();

    /**
     * Initialise l'ÃƒÆ’Ã¢â‚¬Â°lfette avec ses statistiques de dÃƒÆ’Ã‚Â©part et sa ressource Mana.
     * PossÃƒÆ’Ã‚Â¨de "Soin Magique" et "Tir PrÃƒÆ’Ã‚Â©cis (ou presque)" dÃƒÆ’Ã‚Â¨s le niveau 1.
     */
    public Elf() {
        super("L'Elfe", "Elfe", 1, 6, 100, 6, 3, 5, 5, 20);
        this.setResourceName("Mana");
        this.setMaxResource(100);
        this.setCurrentResource(100);
        this.skills.add(new Skill("Soin Magique", 15, "Rend 15 PV ÃƒÆ’Ã‚Â  un alliÃƒÆ’Ã‚Â©.", true));
        this.skills.add(new Skill("Tir PrÃƒÆ’Ã‚Â©cis (ou presque)", 15, "Un tir ÃƒÆ’Ã‚Â  l'arc ajustÃƒÆ’Ã‚Â©. Risque ÃƒÆ’Ã‚Â©levÃƒÆ’Ã‚Â© de toucher un coÃƒÆ’Ã‚Â©quipier !", false));
    }

    /**
     * ExÃƒÆ’Ã‚Â©cute les compÃƒÆ’Ã‚Â©tences spÃƒÆ’Ã‚Â©ciales de l'Elfe (Soin Magique ou Tir PrÃƒÆ’Ã‚Â©cis).
     *
     * @param skill  La compÃƒÆ’Ã‚Â©tence utilisÃƒÆ’Ã‚Â©e
     * @param team   La compagnie de hÃƒÆ’Ã‚Â©ros
     * @param target Le monstre affrontÃƒÆ’Ã‚Â©
     * @return Message de rÃƒÆ’Ã‚Â©sultat
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






