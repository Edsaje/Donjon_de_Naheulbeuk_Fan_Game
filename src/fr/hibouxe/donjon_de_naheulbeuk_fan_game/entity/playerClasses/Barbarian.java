package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

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
        super("Le Barbare", "Barbare", 1, 12, 0, 10, 0, 3, 3);
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
    public void useSpecialSkill(Team team, Character monster, Menu menu) {
        if (this.currentResource >= 20) {
            this.currentResource -= 20;
            int damage = Math.max(1, (this.getAttack() * 2) - monster.getDefense());
            monster.setHealthPoint(monster.getHealthPoint() - damage);
            menu.displayMessage(this.name + " pousse un HURLEMENT BARBARE et frappe avec une violence inouïe ! Inflige " + damage + " dégâts !");
        } else {
            menu.displayMessage(this.name + " n'a pas assez de Rage pour hurler (20 requis) !");
        }
    }
}
