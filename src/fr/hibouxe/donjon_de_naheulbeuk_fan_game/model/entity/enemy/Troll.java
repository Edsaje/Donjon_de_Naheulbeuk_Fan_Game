package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;

/**
 * Représente un Troll des cavernes massif et très résistant.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Troll extends Character {

    /**
     * Initialise un Troll de niveau 1.
     */
    public Troll() {
        super("Troll des cavernes", "Monstre", 1, 20, 0, 10, 0, 2, 2, 4);
    }
}
