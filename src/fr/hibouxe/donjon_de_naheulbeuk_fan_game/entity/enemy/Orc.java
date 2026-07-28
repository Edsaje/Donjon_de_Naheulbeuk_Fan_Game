package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.enemy;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

/**
 * Représente un Orc bruyant et agressif du Donjon de Naheulbeuk.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Orc extends Character {

    /**
     * Initialise un Orc de niveau 2.
     */
    public Orc() {
        super("Orc bruyant", "Monstre", 2, 12, 0, 7, 0, 4, 1);
    }
}
