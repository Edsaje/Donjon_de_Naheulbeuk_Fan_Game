package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.StatType;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

/**
 * Représente le Barbare dans la Compagnie de Naheulbeuk.
 * Combattant de mâtinage lourd réputé pour sa puissance d'attaque brute.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Barbarian extends Character {

    /**
     * Initialise le Barbare avec ses statistiques de départ.
     */
    public Barbarian() {
        super("Le Barbare", "Barbare", 1, 12, 0, 10, 0, 3, 3, 11);
        this.setResourceName("Rage");
        this.setMaxResource(100);
        this.setCurrentResource(0);
        this.skills.add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill("Hurlement Barbare", 30, "Une attaque d'une violence inouïe.", false) {
            @Override
            public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.SkillResult execute(Character user, Team team, Character monster) {
                int cost = getCost();
                if (user.getCurrentResource() >= cost) {
                    user.setCurrentResource(user.getCurrentResource() - cost);
                    int damage = Math.max(1, (int)(user.getAttack() * 2.5) - monster.getDefense());
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







