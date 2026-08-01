package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.enemy;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

/**
 * Représente un Mort-Vivant pestilentiel du Donjon.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Undead extends Character {

    /**
     * Initialise un Mort-Vivant de niveau 1.
     */
    public Undead() {
        super("Mort Vivant pestilentiel", "Monstre", 1, 10, 5, 6, 4, 3, 3, 6);
    }
}
