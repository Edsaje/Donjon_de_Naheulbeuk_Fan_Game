package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;

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
        super("Gobelin pas très futé", "Monstre", 1, 6, 0, 4, 0, 2, 1, 15);
        this.xp = 50; //donne 50 xp à sa mort
    }
}
