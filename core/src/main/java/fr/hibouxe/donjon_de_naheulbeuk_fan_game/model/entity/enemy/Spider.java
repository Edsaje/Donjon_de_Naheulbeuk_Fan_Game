package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;

/**
 * Représente une Araignée géante effrayante dans les couloirs du Donjon.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Spider extends Character {

    /**
     * Initialise une Araignée de niveau 1.
     */
    public Spider() {
        super("Araignée effrayante", "Monstre", 1, 15, 5, 3, 5, 4, 4, 18);
    }
}
