package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

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
        super("L'Ogre", "Ogre", 1, 20, 0, 5, 0, 10, 5);
        this.resourceName = "Rage";
        this.maxResource = 100;
        this.currentResource = 0;
    }
}
