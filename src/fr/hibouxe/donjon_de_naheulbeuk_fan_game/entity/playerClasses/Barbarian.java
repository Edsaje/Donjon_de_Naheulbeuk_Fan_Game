package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

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
}
