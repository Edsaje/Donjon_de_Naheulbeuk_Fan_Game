package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

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
        super("Le Ranger", "Ranger", 1, 10, 10, 5, 2, 10, 10);
        this.resourceName = "Énergie";
        this.maxResource = 100;
        this.currentResource = 100;
    }
}
