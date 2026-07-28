package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

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
        super("Le Voleur", "Voleur", 1, 5, 2, 3, 1, 5, 5);
        this.resourceName = "Énergie";
        this.maxResource = 100;
        this.currentResource = 100;
    }
}
