package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

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
     * @param team   La compagnie de Naheulbeuk
     * @param target Le monstre ciblé par le coup de hache
     * @param menu   La vue principale du jeu (Injectée)
     */
    @Override
    public void useSpecialSkill(Team team, Character target, Menu menu) {
        if (this.currentResource >= 1) { // Vérifie si possède la rage requise
            this.currentResource -= 1; // Retire la rage nécessaire
            int damage = Math.max(1, this.getAttack() + (this.getAttack() / 2) - target.getDefense()); // Calcul des dégâts
            target.setHealthPoint(target.getHealthPoint() - damage);
            menu.displayMessage(this.name + " plante sa hache dans la jambe du " + target.getName() + " !");
        } else {
            menu.displayMessage(this.name + " n'a plus de compétence disponible..");
        }
    }
}
