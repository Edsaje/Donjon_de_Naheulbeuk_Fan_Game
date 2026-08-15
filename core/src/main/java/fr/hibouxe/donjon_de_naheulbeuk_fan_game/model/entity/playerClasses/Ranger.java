package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.StatType;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

/**
 * ReprÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©sente le Ranger (Le Leader) dans la Compagnie de Naheulbeuk.
 * Leader autoproclamÃƒÆ’Ã†â€™ÃƒÆ’Ã‚Â  ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©quilibrÃƒÆ’Ã†â€™ÃƒÆ’Ã‚Â  en attaque et en dÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©fense.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Ranger extends Character {

    /**
     * Initialise le Ranger avec ses statistiques de dÃƒÆ’Ã‚Â©part et sa ressource ÃƒÆ’Ã¢â‚¬Â°nergie.
     */
    public Ranger() {
        super("Le Ranger", "Ranger", 1, 10, 100, 5, 2, 10, 10, 14);
        this.setResourceName("ÃƒÆ’Ã¢â‚¬Â°nergie");
        this.setMaxResource(100);
        this.setCurrentResource(100);
        this.skills.add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill("Tir de PrÃƒÆ’Ã‚Â©cision", 30, "Un tir ajustÃƒÆ’Ã‚Â© qui inflige de lourds dÃƒÆ’Ã‚Â©gÃƒÆ’Ã‚Â¢ts.", false));
    }

    /**
     * ExÃƒÆ’Ã‚Â©cute le tir ÃƒÆ’Ã‚Â  l'arc ajustÃƒÆ’Ã‚Â© du Ranger en consommant 30 points d'ÃƒÆ’Ã¢â‚¬Â°nergie.
     *
     * @param skill   La compÃƒÆ’Ã‚Â©tence utilisÃƒÆ’Ã‚Â©e
     * @param team    La compagnie de Naheulbeuk
     * @param monster Le monstre ciblÃƒÆ’Ã‚Â©
     */
    @Override
    public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult useSpecialSkill(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill skill, Team team, Character monster) {
        if (skill.getName().contains("cision") || skill.getName().equals("Tir de Précision")) {
            int cost = skill.getCost();
            if (this.getCurrentResource() >= cost) {
                this.setCurrentResource(this.getCurrentResource() - cost);
                int damage = Math.max(1, (this.getAttack() * 2) - monster.getDefense());
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







