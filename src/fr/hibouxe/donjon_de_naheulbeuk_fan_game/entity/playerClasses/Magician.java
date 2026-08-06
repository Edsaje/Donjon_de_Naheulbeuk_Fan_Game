package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

public class Magician extends Character {
    public Magician() {
        super("La Magicienne", "Magicienne", 1, 5, 100, 2, 8, 3, 3, 12);
        this.resourceName = "Mana";
        this.maxResource = 100;
        this.currentResource = 100;
        this.skills.add(new Skill("Boule de Feu", 30, "Une attaque magique classique.", false));
    }

    @Override
    public String useSpecialSkill(Skill skill, Team team, Character target) {
        if (skill.getName().equals("Boule de Feu")) {
            int cost = skill.getCost();
            if (this.currentResource >= cost) {
                this.currentResource -= cost;
                int damage = Math.max(1, (this.getMagicAttack() * 2) - target.getMagicDefense());
                target.setHealthPoint(target.getHealthPoint() - damage);
                return this.name + " lance une BOULE DE FEU pas trop mal réussie pour " + damage + " dégâts !";
            } else {
                return this.name + " manque de Mana (" + this.currentResource + "/" + cost + ")...";
            }
        }
        return super.useSpecialSkill(skill, team, target);
    }
    
    @Override
    public void levelUp(fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view.IGameView menu) {
        super.levelUp(menu);
        this.healthPoint += 2;
        this.magicAttack += 4;
        this.defense += 1;
        this.magicDefense += 3;
        this.speed += 1;
        this.maxResource += 10;
        menu.displayMessage(this.name + " apprend de nouveaux mots compliqués pour sa magie (Niveau " + this.level + ") !");
    }
}
