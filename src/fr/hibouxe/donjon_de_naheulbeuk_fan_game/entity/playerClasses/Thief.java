package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

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
        super("Le Voleur", "Voleur", 1, 5, 100, 3, 1, 5, 5, 16);
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
    public String useSpecialSkill(Team team, Character monster) {
        int cost = 30; // cost changed to 30 for better usability
        if (this.currentResource >= cost) {
            this.currentResource -= cost;
            int damage = Math.max(1, (this.getAttack() * 2) - (monster.getDefense() / 2));
            monster.setHealthPoint(monster.getHealthPoint() - damage);
            return this.name + " se glisse dans l'ombre et porte une ATTAQUE SOURNOISE dévastatrice dans le dos ! Inflige " + damage + " dégâts !";
        } else {
            return this.name + " n'a pas assez d'Énergie pour s'éclipser (" + this.currentResource + "/" + cost + ") !";
        }
    }
}
