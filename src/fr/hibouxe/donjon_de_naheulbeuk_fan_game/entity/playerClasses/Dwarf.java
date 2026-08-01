package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

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
    public String useSpecialSkill(Team team, Character target) {
        int cost = 20; // Équilibrage : Un coup puissant coûte 20 de Rage, et non plus 1.
        if (this.currentResource >= cost) { 
            this.currentResource -= cost; 
            int damage = Math.max(1, (int)(this.getAttack() * 1.5) - target.getDefense()); 
            target.setHealthPoint(target.getHealthPoint() - damage);
            return this.name + " hurle de rage et plante sa hache dans le " + target.getName() + " pour " + damage + " dégâts !";
        } else {
            return this.name + " n'a pas assez de Rage (" + this.currentResource + "/" + cost + ") pour son attaque spéciale...";
        }
    }
}
