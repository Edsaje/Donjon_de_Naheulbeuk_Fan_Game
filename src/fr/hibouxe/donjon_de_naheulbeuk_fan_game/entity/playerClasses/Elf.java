package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

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
        super("L'Elfe", "Elfe", 1, 6, 10, 6, 3, 5, 5);
        this.resourceName = "Mana";
        this.maxResource = 10;
        this.currentResource = 10;
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
    public void useSpecialSkill(Team team, Character target, Menu menu) {
        if (this.currentResource >= 2) { // Vérifie si possède le mana requis
            this.currentResource -= 2; // Retire le mana nécessaire

            Character allyToHeal = menu.askAllyToHeal(team);
            if (allyToHeal != null) {
                int healAmount = 8; // Quantité de soins
                allyToHeal.setHealthPoint(allyToHeal.getHealthPoint() + healAmount);
                menu.displayMessage(this.name + " utilise ses compétences en chirurgie et soigne " + allyToHeal.getName() + " de +" + healAmount + " PV !");
            } else {
                menu.displayMessage("Cible invalide.");
            }
        } else {
            menu.displayMessage(this.name + " n'a plus de sort de combat disponible..");
        }
    }
}
