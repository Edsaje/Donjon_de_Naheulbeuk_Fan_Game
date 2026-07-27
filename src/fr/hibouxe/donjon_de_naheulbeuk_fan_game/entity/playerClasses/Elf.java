package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

import java.util.Scanner;

public class Elf extends Character {

    public Elf() {

        super("L'Elfe", "Elfe", 1, 6, 10, 6, 3, 5, 5);
        this.resourceName = "Mana";
        this.maxResource = 10;
        this.currentResource = 10;
    }

    @Override
    public void useSpecialSkill(Team team, Character target, Scanner keyboard) {
        if (this.currentResource >= 2) { //vérifie si possede le mana requis
            this.currentResource -= 2; //retire le mana necessaire

            for (int i = 0; i < team.getMembers().size(); i++) {
                Character c = team.getMembers().get(i);
                System.out.println(i + ". " + c.getName() + " | PV: " + Math.max(0, c.getHealthPoint()));
            }

            System.out.print("> Choisissez le coéquipier à soigner : ");
            int choice = Integer.parseInt(keyboard.nextLine().trim());

            if (choice >= 0 && choice < team.getMembers().size()) {
                target = team.getMembers().get(choice);
                int healAmount = 8; // quantité de soins
                target.setHealthPoint(target.getHealthPoint() + healAmount);
                System.out.println(this.name + " utilise ses compétences en chirurgie !");
            } else {
                System.out.println(this.name + " n'a plus de sort de combat disponible..");
            }
        }
    }
}
