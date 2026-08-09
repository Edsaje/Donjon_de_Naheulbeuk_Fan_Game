package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console.ConsoleMenu;

public class Thief extends Character {
    public Thief() {
        super("Le Voleur", "Voleur", 1, 5, 100, 3, 1, 5, 5, 16);
        this.resourceName = "Énergie";
        this.maxResource = 100;
        this.currentResource = 100;
        this.skills.add(new Skill("Attaque Sournoise", 30, "Une attaque pernicieuse dans le dos.", false));
    }

    @Override
    public String useSpecialSkill(Skill skill, Team team, Character monster) {
        if (skill.getName().equals("Attaque Sournoise")) {
            int cost = skill.getCost();
            if (this.currentResource >= cost) {
                this.currentResource -= cost;
                int damage = Math.max(1, (this.getAttack() * 2) - (monster.getDefense() / 2));
                monster.setHealthPoint(monster.getHealthPoint() - damage);
                return this.name + " se glisse dans l'ombre et porte une ATTAQUE SOURNOISE dévastatrice dans le dos ! Inflige " + damage + " dégâts !";
            } else {
                return this.name + " n'a pas assez d'Énergie pour s'éclipser (" + this.currentResource + "/" + cost + ") !";
            }
        }
        return super.useSpecialSkill(skill, team, monster);
    }
    
    @Override
    public void levelUp() {
        super.levelUp();
        this.healthPoint += 3;
        this.attack += 3;
        this.defense += 1;
        this.magicDefense += 1;
        this.speed += 3;
        // menu.displayMessage(this.name + " arrive encore mieux à se dissimuler et fuir le combat ! (Niveau " + this.level + ") !");
    }
}
