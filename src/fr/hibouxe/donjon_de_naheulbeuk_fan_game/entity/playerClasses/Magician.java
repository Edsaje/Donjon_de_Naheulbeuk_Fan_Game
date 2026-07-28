package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

/**
 * Représente la Magicienne dans la Compagnie de Naheulbeuk.
 * Spécialiste des sorts offensifs basés sur l'attaque magique et le Mana.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Magician extends Character {

    /**
     * Initialise la Magicienne avec ses statistiques de départ et sa ressource Mana.
     */
    public Magician() {
        super("La Magicienne", "Magicienne", 1, 5, 20, 2, 8, 3, 3);
        this.resourceName = "Mana";
        this.maxResource = 20;
        this.currentResource = 20;
    }

    /**
     * Exécute le sort offensif Boule de Feu contre le monstre.
     * Consomme 4 points de Mana et inflige des dégâts magiques.
     *
     * @param team   La compagnie de Naheulbeuk
     * @param target Le monstre ciblé par le sort
     * @param menu   La vue principale du jeu (Injectée)
     */
    @Override
    public void useSpecialSkill(Team team, Character target, Menu menu) {
        if (this.currentResource >= 4) { // Vérifie si possède le mana requis
            this.currentResource -= 4; // Retire le mana nécessaire
            int damage = Math.max(1, this.getMagicAttack() - target.getMagicDefense()); // Calcule les dégâts magiques
            target.setHealthPoint(target.getHealthPoint() - damage);
            menu.displayMessage(this.name + " lance une BOULE DE FEU pas trop mal réussie !");
        } else {
            menu.displayMessage(this.name + " n'a plus de sort de combat disponible..");
        }
    }
}
