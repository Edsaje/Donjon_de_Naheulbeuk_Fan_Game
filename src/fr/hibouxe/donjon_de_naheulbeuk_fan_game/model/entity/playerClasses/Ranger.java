package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

/**
 * ReprÃ©sente le Ranger (Le Leader) dans la Compagnie de Naheulbeuk.
 * Leader autoproclamÃà Ã©quilibrÃà en attaque et en dÃ©fense.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Ranger extends Character {

    /**
     * Initialise le Ranger avec ses statistiques de départ et sa ressource Énergie.
     */
    public Ranger() {
        super("Le Ranger", "Ranger", 1, 10, 100, 5, 2, 10, 10, 14);
        this.resourceName = "Énergie";
        this.maxResource = 100;
        this.currentResource = 100;
        this.skills.add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill("Tir de Précision", 30, "Un tir ajusté qui inflige de lourds dégâts.", false));
    }

    /**
     * Exécute le tir à l'arc ajusté du Ranger en consommant 30 points d'Énergie.
     *
     * @param skill   La compétence utilisée
     * @param team    La compagnie de Naheulbeuk
     * @param monster Le monstre ciblé
     */
    @Override
    public String useSpecialSkill(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill skill, Team team, Character monster) {
        if (skill.getName().equals("Tir de Précision")) {
            int cost = skill.getCost();
            if (this.currentResource >= cost) {
                this.currentResource -= cost;
                int damage = Math.max(1, (this.getAttack() * 2) - monster.getDefense());
                monster.setHealthPoint(monster.getHealthPoint() - damage);
                return this.name + " décoche un TIR DE PRÉCISION au sang-froid impressionnant ! Inflige " + damage + " dégâts !";
            } else {
                return this.name + " n'a pas assez d'Énergie pour ajuster son tir (" + this.currentResource + "/" + cost + ") !";
            }
        }
        return super.useSpecialSkill(skill, team, monster);
    }
    @Override
    public java.util.List<String> levelUp() {
        java.util.List<String> messages = super.levelUp();
        String[] stats = {
            increaseStat("PV Max", 2, 4),
            increaseStat("PM Max", 1, 2),
            increaseStat("Attaque", 1, 2),
            increaseStat("Défense", 1, 2),
            increaseStat("Attaque Magique", 1, 2),
            increaseStat("Défense Magique", 1, 2),
            increaseStat("Vitesse", 1, 2)
        };
        for (String msg : stats) {
            if (msg != null) messages.add(msg);
        }
        return messages;
    }
}

