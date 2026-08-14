package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

/**
 * ReprÃ©sente le Barbare dans la Compagnie de Naheulbeuk.
 * Combattant de mÃ¢tinage lourd rÃ©putÃà pour sa puissance d'attaque brute.
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
        this.resourceName = "Rage";
        this.maxResource = 100;
        this.currentResource = 0;
        this.skills.add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill("Hurlement Barbare", 30, "Une attaque d'une violence inouïe.", false));
    }

    /**
     * ExÃ©cute l'attaque dÃ©vastatrice du Barbare en consommant 20 points de Rage.
     *
     * @param skill   La compétence utilisée
     * @param team    La compagnie de Naheulbeuk
     * @param monster Le monstre ciblé
     */
    @Override
    public String useSpecialSkill(Skill skill, Team team, Character monster) {
        if (skill.getName().equals("Hurlement Barbare")) {
            int cost = skill.getCost();
            if (this.currentResource >= cost) {
                this.currentResource -= cost;
                int damage = Math.max(1, (int)(this.getAttack() * 2.5) - monster.getDefense());
                monster.setHealthPoint(monster.getHealthPoint() - damage);
                return this.name + " pousse un HURLEMENT BARBARE et frappe avec une violence inouïe ! Inflige " + damage + " dégâts !";
            } else {
                return this.name + " n'a pas assez de Rage (" + this.currentResource + "/" + cost + ") pour hurler !";
            }
        }
        return super.useSpecialSkill(skill, team, monster);
    }
    @Override
    public java.util.List<String> levelUp() {
        java.util.List<String> messages = super.levelUp();
        String[] stats = {
            increaseStat("PV Max", 5, 8),
            increaseStat("Attaque", 3, 6),
            increaseStat("Défense", 1, 3),
            increaseStat("Vitesse", 1, 2)
        };
        for (String msg : stats) {
            if (msg != null) messages.add(msg);
        }
        return messages;
    }
}

