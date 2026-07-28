package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.enemy;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

/**
 * Représente un Gobelin faible et peu futé du Donjon de Naheulbeuk.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Goblin extends Character {

    /**
     * Initialise un Gobelin de niveau 1.
     */
    public Goblin() {
        super("Gobelin pas très futé", "Monstre", 1, 6, 0, 4, 0, 2, 1);
    }
}
