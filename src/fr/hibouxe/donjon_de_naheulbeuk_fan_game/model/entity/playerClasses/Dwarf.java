package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console.ConsoleMenu;

/**
 * Modèle de classe du héros Nain.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Dwarf extends Character {

    /**
     * Instancie le héros Nain avec ses statistiques de base et sa ressource Rage.
     */
    public Dwarf() {
        super("Le Nain", "Nain", 1, 12, 0, 7, 0, 6, 6, 5);
        this.resourceName = "Rage";
        this.maxResource = 100;
        this.currentResource = 0;
        this.skills.add(new Skill("Coup de Hache Lourd", 20, "Un coup puissant qui consomme de la rage.", false));
    }

    @Override
    public String useSpecialSkill(Skill skill, Team team, Character target) {
        if (skill.getName().equals("Coup de Hache Lourd")) {
            int cost = skill.getCost();
            if (this.currentResource >= cost) { 
                this.currentResource -= cost; 
                int damage = Math.max(1, (int)(this.getAttack() * 1.5) - target.getDefense()); 
                target.setHealthPoint(target.getHealthPoint() - damage);
                return this.name + " hurle de rage et plante sa hache dans le " + target.getName() + " pour " + damage + " dégâts !";
            } else {
                return this.name + " n'a pas assez de Rage (" + this.currentResource + "/" + cost + ") pour son attaque spéciale...";
            }
        }
        return super.useSpecialSkill(skill, team, target);
    }
    
    @Override
    public void levelUp(fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IGameView menu) {
        super.levelUp(menu);
        this.healthPoint += 6;
        this.attack += 2;
        this.defense += 3;
        this.magicDefense += 1;
        this.speed += 1;
        menu.displayMessage(this.name + " chantonne : Je suis niveau " + this.level + " Tralalalala (Niveau " + this.level + ") !");
    }
}
