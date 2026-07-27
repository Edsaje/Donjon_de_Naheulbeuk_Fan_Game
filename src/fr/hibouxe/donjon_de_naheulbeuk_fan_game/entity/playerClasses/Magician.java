package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

public class Magician extends Character {

    public Magician(){
        super("La Magicienne","Magicienne",1,5,10,2,8,3,3);
    }

    @Override
    public void useSpecialSkill(Character target){
        if (this.manaPoint >= 4){ //vérifie si possede le mana requis
            this.manaPoint -= 4; //retire le mana necessaire
            int damage = Math.max(1, this.getMagicAttack() - target.getMagicDefense()); //Calcule les dégats
            target.setHealthPoint(target.getHealthPoint() - damage);
            System.out.println(this.name + " lance une BOULE DE FEU pas trop mal réussi !");
        } else {
            System.out.println(this.name + ": Je n'ai plus de sort de combat disponible..");
        }
    }
}
