package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.StatType;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

public class Magician extends Character {
    public Magician() {
        super("La Magicienne", "Magicienne", 1, 5, 100, 2, 8, 3, 3, 12);
        this.setResourceName("Mana");
        this.setMaxResource(100);
        this.setCurrentResource(100);
        this.skills.add(new Skill("Boule de Feu", 30, "Une attaque magique classique.", false));
    }

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






