package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

import java.util.Scanner;

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
     * @param team     La compagnie contenant la cible à soigner
     * @param target   Le monstre affronté (non utilisé pour le soin)
     * @param keyboard Le scanner de saisie pour choisir l'allié à soigner
     */
    @Override
    public void useSpecialSkill(Team team, Character target, Scanner keyboard) {
        if (this.currentResource >= 2) { // Vérifie si possède le mana requis
            this.currentResource -= 2; // Retire le mana nécessaire

            for (int i = 0; i < team.getMembers().size(); i++) {
                Character c = team.getMembers().get(i);
                System.out.println(i + ". " + c.getName() + " | PV: " + Math.max(0, c.getHealthPoint()));
            }

            System.out.print("> Choisissez le coéquipier à soigner : ");
            int choice = Integer.parseInt(keyboard.nextLine().trim());

            if (choice >= 0 && choice < team.getMembers().size()) {
                target = team.getMembers().get(choice);
                int healAmount = 8; // Quantité de soins
                target.setHealthPoint(target.getHealthPoint() + healAmount);
                System.out.println(this.name + " utilise ses compétences en chirurgie !");
            } else {
                System.out.println(this.name + " n'a plus de sort de combat disponible..");
            }
        }
    }
}
