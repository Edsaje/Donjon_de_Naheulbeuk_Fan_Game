package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

/**
 * Représente la Magicienne dans la Compagnie de Naheulbeuk.
 * Spécialiste des sorts offensifs basés sur l'attaque magique et le Mana.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Magician extends Character {

    /**
     * Initialise la Magicienne avec ses statistiques de départ et sa ressource Mana.
     */
    public Magician() {
        super("La Magicienne", "Magicienne", 1, 5, 100, 2, 8, 3, 3);
        this.resourceName = "Mana";
        this.maxResource = 100;
        this.currentResource = 100;
    }

    /**
     * Exécute le sort offensif Boule de Feu contre le monstre.
     * Consomme 4 points de Mana et inflige des dégâts magiques.
     *
     * @param team   La compagnie de Naheulbeuk
     * @param target Le monstre ciblé par le sort
     * @param menu   La vue principale du jeu (Injectée)
     */
    @Override
    public String useSpecialSkill(Team team, Character target) {
        int cost = 30;
        if (this.currentResource >= cost) {
            this.currentResource -= cost;
            int damage = Math.max(1, (this.getMagicAttack() * 2) - target.getMagicDefense());
            target.setHealthPoint(target.getHealthPoint() - damage);
            return this.name + " lance une BOULE DE FEU pas trop mal réussie pour " + damage + " dégâts !";
        } else {
            return this.name + " manque de Mana (" + this.currentResource + "/" + cost + ")...";
        }
    }
}
