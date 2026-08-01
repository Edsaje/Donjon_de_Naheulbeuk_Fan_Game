package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.enemy;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

/**
 * Représente un Squelette réanimé dans le Donjon de Naheulbeuk.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Skeleton extends Character {

    /**
     * Initialise un Squelette de niveau 1.
     */
    public Skeleton() {
        super("Squelette bien membré", "Monstre", 1, 10, 0, 7, 0, 1, 3, 10);
    }
}
