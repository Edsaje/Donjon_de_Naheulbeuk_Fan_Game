package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

import java.util.Scanner;

/**
 * Représente le Nain dans la Compagnie de Naheulbeuk.
 * Combatant résistant qui accumule de la Rage au combat pour porter des coups puissants.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Dwarf extends Character {

    /**
     * Initialise le Nain avec ses statistiques de départ et sa ressource Rage.
     */
    public Dwarf() {
        super("Le Nain", "Nain", 1, 12, 0, 7, 0, 6, 6);
        this.resourceName = "Rage";
        this.maxResource = 100;
        this.currentResource = 0;
    }

    /**
     * Exécute l'attaque lourde du Nain en consommant de la Rage.
     * Inflige des dégâts physiques accrus.
     *
     * @param team     La compagnie de Naheulbeuk
     * @param target   Le monstre ciblé par le coup de hache
     * @param keyboard Le scanner de saisie utilisateur
     */
    @Override
    public void useSpecialSkill(Team team, Character target, Scanner keyboard) {
        if (this.currentResource >= 1) { // Vérifie si possède la rage requise
            this.currentResource -= 1; // Retire la rage nécessaire
            int damage = Math.max(1, this.getAttack() + (this.getAttack() / 2) - target.getDefense()); // Calcul des dégâts
            target.setHealthPoint(target.getHealthPoint() - damage);
            System.out.println(this.name + " plante sa hache dans la jambe du " + target.getName() + " !");
        } else {
            System.out.println(this.name + " n'a plus de compétence disponible..");
        }
    }
}
