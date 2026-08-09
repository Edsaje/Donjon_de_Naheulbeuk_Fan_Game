package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console.ConsoleMenu;

/**
 * ReprÃ©sente le Barbare dans la Compagnie de Naheulbeuk.
 * Combattant de mÃ¢tinage lourd rÃ©putÃ© pour sa puissance d'attaque brute.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Barbarian extends Character {

    /**
     * Initialise le Barbare avec ses statistiques de dÃ©part.
     */
    public Barbarian() {
        super("Le Barbare", "Barbare", 1, 12, 0, 10, 0, 3, 3, 11);
        this.resourceName = "Rage";
        this.maxResource = 100;
        this.currentResource = 0;
        this.skills.add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill("Hurlement Barbare", 30, "Une attaque d'une violence inouïe.", false));
    }

    /**
     * ExÃ©cute l'attaque dÃ©vastatrice du Barbare en consommant 20 points de Rage.
     *
     * @param skill   La compétence utilisée
     * @param team    La compagnie de Naheulbeuk
     * @param monster Le monstre ciblé
     */
    @Override
    public String useSpecialSkill(Skill skill, Team team, Character monster) {
        if (skill.getName().equals("Hurlement Barbare")) {
            int cost = skill.getCost();
            if (this.currentResource >= cost) {
                this.currentResource -= cost;
                int damage = Math.max(1, (int)(this.getAttack() * 2.5) - monster.getDefense());
                monster.setHealthPoint(monster.getHealthPoint() - damage);
                return this.name + " pousse un HURLEMENT BARBARE et frappe avec une violence inouïe ! Inflige " + damage + " dégâts !";
            } else {
                return this.name + " n'a pas assez de Rage (" + this.currentResource + "/" + cost + ") pour hurler !";
            }
        }
        return super.useSpecialSkill(skill, team, monster);
    }
    @Override
    public void levelUp() {
        super.levelUp();
        this.healthPoint += 5;
        this.attack += 4;
        this.defense += 1;
        this.speed += 1;
        // menu.displayMessage(this.name + " sent encore plus fort des pieds ! (Niveau " + this.level + ") !");
    }
}

