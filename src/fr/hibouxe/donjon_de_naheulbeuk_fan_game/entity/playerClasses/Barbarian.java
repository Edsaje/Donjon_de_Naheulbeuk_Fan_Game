package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

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
        this.resourceName = "Rage";
        this.maxResource = 100;
        this.currentResource = 0;
    }

    /**
     * Exécute l'attaque dévastatrice du Barbare en consommant 20 points de Rage.
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
            int damage = Math.max(1, (int)(this.getAttack() * 2.5) - monster.getDefense());
            monster.setHealthPoint(monster.getHealthPoint() - damage);
            return this.name + " pousse un HURLEMENT BARBARE et frappe avec une violence inouÃ¯e ! Inflige " + damage + " dégâts !";
        } else {
            return this.name + " n'a pas assez de Rage (" + this.currentResource + "/" + cost + ") pour hurler !";
        }
    }
}
