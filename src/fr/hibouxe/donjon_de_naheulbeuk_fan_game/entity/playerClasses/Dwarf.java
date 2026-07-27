package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

import java.util.Scanner;

public class Dwarf extends Character {

    public Dwarf(){
        super("Le Nain","Nain",1,12,0,7,0,6,6);
        this.resourceName = "Rage";
        this.maxResource = 10;
        this.currentResource = 0;
    }
    @Override
    public void useSpecialSkill(Team team, Character target, Scanner keyboard){
        if (this.currentResource >= 1){ //vérifie si possede le mana requis
            this.currentResource -= 1; //retire le mana necessaire
            int damage = Math.max(1, this.getAttack() + (this.getAttack()/2) - target.getDefense()); //Calcule les dégats
            target.setHealthPoint(target.getHealthPoint() - damage);
            System.out.println(this.name + " plante sa hache dans la jambe du " + target.getName() + " !");
        } else {
            System.out.println(this.name + " n'a plus de sort de combat disponible..");
        }
    }
}
