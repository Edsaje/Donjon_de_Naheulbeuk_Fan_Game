package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

/**
 * Représente le Ranger (Le Leader) dans la Compagnie de Naheulbeuk.
 * Leader autoproclamé équilibré en attaque et en défense.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Ranger extends Character {

    /**
     * Initialise le Ranger avec ses statistiques de départ et sa ressource Énergie.
     */
    public Ranger() {
        super("Le Ranger", "Ranger", 1, 10, 100, 5, 2, 10, 10);
        this.resourceName = "Énergie";
        this.maxResource = 100;
        this.currentResource = 100;
    }

    /**
     * Exécute le tir à l'arc ajusté du Ranger en consommant 30 points d'Énergie.
     *
     * @param team    La compagnie de Naheulbeuk
     * @param monster Le monstre ciblé
     * @param menu    La vue principale du jeu (Injectée)
     */
    @Override
    public String useSpecialSkill(Team team, Character monster) {
        int cost = 30;
        if (this.currentResource >= cost) {
            this.currentResource -= cost;
            int damage = Math.max(1, (this.getAttack() * 2) - monster.getDefense());
            monster.setHealthPoint(monster.getHealthPoint() - damage);
            return this.name + " décoche un TIR DE PRÉCISION au sang-froid impressionnant ! Inflige " + damage + " dégâts !";
        } else {
            return this.name + " n'a pas assez d'Énergie pour ajuster son tir (" + this.currentResource + "/" + cost + ") !";
        }
    }
}
