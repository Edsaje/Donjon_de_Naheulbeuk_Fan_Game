package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.StatType;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

/**
 * ReprÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©sente le Barbare dans la Compagnie de Naheulbeuk.
 * Combattant de mÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢tinage lourd rÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©putÃƒÆ’Ã†â€™ÃƒÆ’Ã‚Â  pour sa puissance d'attaque brute.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Barbarian extends Character {

    /**
     * Initialise le Barbare avec ses statistiques de dÃƒÆ’Ã‚Â©part.
     */
    public Barbarian() {
        super("Le Barbare", "Barbare", 1, 12, 0, 10, 0, 3, 3, 11);
        this.setResourceName("Rage");
        this.setMaxResource(100);
        this.setCurrentResource(0);
        this.skills.add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill("Hurlement Barbare", 30, "Une attaque d'une violence inouÃƒÆ’Ã‚Â¯e.", false));
    }

    /**
     * ExÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©cute l'attaque dÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©vastatrice du Barbare en consommant 20 points de Rage.
     *
     * @param skill   La compÃƒÆ’Ã‚Â©tence utilisÃƒÆ’Ã‚Â©e
     * @param team    La compagnie de Naheulbeuk
     * @param monster Le monstre ciblÃƒÆ’Ã‚Â©
     */
    @Override
    public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult useSpecialSkill(Skill skill, Team team, Character monster) {
        if (skill.getName().equals("Hurlement Barbare")) {
            int cost = skill.getCost();
            if (this.getCurrentResource() >= cost) {
                this.setCurrentResource(this.getCurrentResource() - cost);
                int damage = Math.max(1, (int)(this.getAttack() * 2.5) - monster.getDefense());
                monster.setHealthPoint(monster.getHealthPoint() - damage);
                return new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult(true, damage, "DAMAGE");
            } else {
                return new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult(false, 0, "ERROR");
            }
        }
        return super.useSpecialSkill(skill, team, monster);
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







