package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

/**
 * Représente l'Élfette dans la Compagnie de Naheulbeuk.
 * Héros spécialisé dans le soutien et les soins magiques.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Elf extends Character {

    /**
     * Initialise l'Élfette avec ses statistiques de départ et sa ressource Mana.
     */
    public Elf() {
        super("L'Elfe", "Elfe", 1, 6, 100, 6, 3, 5, 5, 20);
        this.resourceName = "Mana";
        this.maxResource = 100;
        this.currentResource = 100;
    }

    /**
     * Exécute la compétence spéciale de soin magique sur un coéquipier de la compagnie.
     * Consomme 2 points de Mana.
     *
     * @param team   La compagnie contenant la cible à soigner
     * @param target Le monstre affronté (non utilisé pour le soin)
     * @param menu   La vue principale du jeu (Injectée)
     */
    @Override
    public String useSpecialSkill(Team team, Character target) {
        int cost = 15;
        if (this.currentResource >= cost) {
            this.currentResource -= cost;
            
            if (target != null) {
                int healAmount = 15;
                target.setHealthPoint(target.getHealthPoint() + healAmount);
                return this.name + " lance un sort de soin sur " + target.getName() + " et lui rend " + healAmount + " PV !";
            } else {
                return this.name + " essaie de soigner mais la cible est invalide...";
            }
        } else {
            return this.name + " n'a pas assez de Mana (" + this.currentResource + "/" + cost + ") pour soigner !";
        }
    }
}
