package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.boss;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;

/**
 * Modle du Boss Golem de Fer (Garde la porte du bureau de Zangdar  l'tage 5).
 * Possde une dfense physique initiale colossale (18), mais sensible au Tir Prcis de l'Elfe.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Golem extends Character {

    /**
     * Instancie le Boss Golem de Fer.
     */
    public Golem() {
        super("Golem de Fer", "Boss", 5, 120, 0, 14, 2, 18, 3, 2);
    }
}
