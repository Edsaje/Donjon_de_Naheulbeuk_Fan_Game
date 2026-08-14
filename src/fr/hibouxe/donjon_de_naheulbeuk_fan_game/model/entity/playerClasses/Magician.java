package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

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
    public java.util.List<String> levelUp() {
        java.util.List<String> messages = super.levelUp();
        String[] stats = {
            increaseStat("PV Max", 1, 3),
            increaseStat("PM Max", 4, 7),
            increaseStat("Attaque Magique", 2, 5),
            increaseStat("Défense Magique", 2, 4),
            increaseStat("Défense", 0, 1)
        };
        for (String msg : stats) {
            if (msg != null) messages.add(msg);
        }
        return messages;
    }
}
