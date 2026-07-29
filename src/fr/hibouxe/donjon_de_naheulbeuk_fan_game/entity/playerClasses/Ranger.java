package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

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
    public void useSpecialSkill(Team team, Character monster, Menu menu) {
        if (this.currentResource >= 30) {
            this.currentResource -= 30;
            int damage = Math.max(1, (this.getAttack() + 4) - monster.getDefense());
            monster.setHealthPoint(monster.getHealthPoint() - damage);
            menu.displayMessage(this.name + " décoche un TIR DE PRÉCISION au sang-froid impressionnant ! Inflige " + damage + " dégâts !");
        } else {
            menu.displayMessage(this.name + " n'a pas assez d'Énergie pour ajuster son tir (30 requis) !");
        }
    }
}
