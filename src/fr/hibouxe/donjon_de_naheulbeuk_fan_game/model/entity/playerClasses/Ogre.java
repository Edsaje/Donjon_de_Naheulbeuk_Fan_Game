package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

public class Ogre extends Character {
    public Ogre() {
        super("L'Ogre", "Ogre", 1, 20, 0, 5, 0, 10, 5, 4);
        this.resourceName = "Rage";
        this.maxResource = 100;
        this.currentResource = 0;
        this.skills.add(new Skill("Écrasement Massif", 30, "Ignore la défense de la cible.", false));
    }

    @Override
    public String useSpecialSkill(Skill skill, Team team, Character monster) {
        if (skill.getName().equals("Écrasement Massif")) {
            int cost = skill.getCost();
            if (this.currentResource >= cost) {
                this.currentResource -= cost;
                int damage = Math.max(1, (int)(this.getAttack() * 1.5) + 5);
                monster.setHealthPoint(monster.getHealthPoint() - damage);
                return this.name + " ÉCRASE le " + monster.getName() + " sous sa masse immense en ignorant sa défense ! Inflige " + damage + " dégâts !";
            } else {
                return this.name + " gronde mais n'a pas assez de Rage (" + this.currentResource + "/" + cost + ") !";
            }
        }
        return super.useSpecialSkill(skill, team, monster);
    }
    
    @Override
    public void levelUp() {
        super.levelUp();
        this.healthPoint += 8;
        this.attack += 3;
        this.defense += 2;
        // menu.displayMessage(this.name + " Akala, zog zog, glozou bok ! (Niveau " + this.level + ") !");
    }
}
