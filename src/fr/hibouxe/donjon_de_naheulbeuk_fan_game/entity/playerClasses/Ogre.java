package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

/**
 * Représente l'Ogre dans la Compagnie de Naheulbeuk.
 * Colosse extrêmement résistant possédant des points de vie et une défense élevés.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Ogre extends Character {

    /**
     * Initialise l'Ogre avec ses statistiques de départ et sa ressource Rage.
     */
    public Ogre() {
        super("L'Ogre", "Ogre", 1, 20, 0, 5, 0, 10, 5, 4);
        this.resourceName = "Rage";
        this.maxResource = 100;
        this.currentResource = 0;
    }

    /**
     * Exécute l'écrasement massif de l'Ogre en consommant 15 points de Rage.
     * Ignore la défense de la cible.
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
            int damage = Math.max(1, (int)(this.getAttack() * 1.5) + 5);
            monster.setHealthPoint(monster.getHealthPoint() - damage);
            return this.name + " ÉCRASE le " + monster.getName() + " sous sa masse immense en ignorant sa défense ! Inflige " + damage + " dégâts !";
        } else {
            return this.name + " gronde mais n'a pas assez de Rage (" + this.currentResource + "/" + cost + ") !";
        }
    }
}
