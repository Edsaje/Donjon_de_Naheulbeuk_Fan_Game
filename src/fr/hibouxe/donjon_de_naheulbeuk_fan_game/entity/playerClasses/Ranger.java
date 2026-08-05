package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

/**
 * ReprÃ©sente le Ranger (Le Leader) dans la Compagnie de Naheulbeuk.
 * Leader autoproclamÃ© Ã©quilibrÃ© en attaque et en dÃ©fense.
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
        this.skills.add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Skill("Tir de Précision", 30, "Un tir ajusté qui inflige de lourds dégâts.", false));
    }

    /**
     * Exécute le tir à l'arc ajusté du Ranger en consommant 30 points d'Énergie.
     *
     * @param skill   La compétence utilisée
     * @param team    La compagnie de Naheulbeuk
     * @param monster Le monstre ciblé
     */
    @Override
    public String useSpecialSkill(fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Skill skill, Team team, Character monster) {
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
    public void levelUp(Menu menu) {
        super.levelUp(menu);
        this.healthPoint += 4;
        this.attack += 2;
        this.defense += 1;
        this.magicDefense += 1;
        this.speed += 2;
        menu.displayMessage(this.name + " en fait toujours trop à propos de ses compétences (Niveau " + this.level + ") !");
    }
}

