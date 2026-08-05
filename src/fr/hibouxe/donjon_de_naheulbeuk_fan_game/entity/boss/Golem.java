package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.boss;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

/**
 * Modèle du Boss Golem de Fer (Garde la porte du bureau de Zangdar à l'Étage 5).
 * Possède une défense physique initiale colossale (18), mais sensible au Tir Précis de l'Elfe.
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
