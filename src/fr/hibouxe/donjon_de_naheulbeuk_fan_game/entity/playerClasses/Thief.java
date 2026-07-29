package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

/**
 * Représente le Voleur dans la Compagnie de Naheulbeuk.
 * Aventurier agile spécialisé dans les attaques rapides et la couardise.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Thief extends Character {

    /**
     * Initialise le Voleur avec ses statistiques de départ et sa ressource Énergie.
     */
    public Thief() {
        super("Le Voleur", "Voleur", 1, 5, 100, 3, 1, 5, 5);
        this.resourceName = "Énergie";
        this.maxResource = 100;
        this.currentResource = 100;
    }

    /**
     * Exécute l'attaque sournoise dans le dos du Voleur en consommant 40 points d'Énergie.
     *
     * @param team    La compagnie de Naheulbeuk
     * @param monster Le monstre ciblé
     * @param menu    La vue principale du jeu (Injectée)
     */
    @Override
    public void useSpecialSkill(Team team, Character monster, Menu menu) {
        if (this.currentResource >= 40) {
            this.currentResource -= 40;
            int damage = Math.max(1, (this.getAttack() * 2) - (monster.getDefense() / 2));
            monster.setHealthPoint(monster.getHealthPoint() - damage);
            menu.displayMessage(this.name + " se glisse dans l'ombre et porte une ATTAQUE SOURNOISE dévastatrice dans le dos ! Inflige " + damage + " dégâts !");
        } else {
            menu.displayMessage(this.name + " n'a pas assez d'Énergie pour s'éclipser (40 requis) !");
        }
    }
}
