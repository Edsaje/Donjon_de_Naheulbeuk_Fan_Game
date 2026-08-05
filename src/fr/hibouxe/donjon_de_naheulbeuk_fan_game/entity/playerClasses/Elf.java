package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

/**
 * ReprÃ©sente l'Ã‰lfette dans la Compagnie de Naheulbeuk.
 * HÃ©ros spÃ©cialisÃ© dans le soutien et les soins magiques.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Elf extends Character {

    /**
     * Initialise l'Ã‰lfette avec ses statistiques de dÃ©part et sa ressource Mana.
     */
    public Elf() {
        super("L'Elfe", "Elfe", 1, 6, 100, 6, 3, 5, 5, 20);
        this.resourceName = "Mana";
        this.maxResource = 100;
        this.currentResource = 100;
        this.skills.add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Skill("Soin Magique", 15, "Rend 15 PV à un allié.", true));
    }

    /**
     * ExÃ©cute la compÃ©tence spÃ©ciale de soin magique sur un coÃ©quipier de la compagnie.
     * Consomme 2 points de Mana.
     *
     * @param skill  La compétence utilisée
     * @param team   La compagnie contenant la cible à soigner
     * @param target Le monstre affronté (non utilisé pour le soin)
     */
    @Override
    public String useSpecialSkill(fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Skill skill, Team team, Character target) {
        if (skill.getName().equals("Soin Magique")) {
            int cost = skill.getCost();
            if (this.currentResource >= cost) {
                this.currentResource -= cost;
                
                if (target != null) {
                    int healAmount = 15;
                    target.setHealthPoint(target.getHealthPoint() + healAmount);
                    return this.name + " lance un sort de soin sur " + target.getName() + " et lui rend " + healAmount + " PV !";
                } else {
                    return this.name + " essaie de soigner mais la cible est invalide...";
                }
            } else {
                return this.name + " n'a pas assez de Mana (" + this.currentResource + "/" + cost + ") pour soigner !";
            }
        }
        return super.useSpecialSkill(skill, team, target);
    }
    @Override
    public void levelUp(Menu menu) {
        super.levelUp(menu);
        this.healthPoint += 2;
        this.attack += 1;
        this.magicAttack += 3;
        this.defense += 1;
        this.magicDefense += 2;
        this.speed += 3;
        menu.displayMessage(this.name + " gagne encore en charisme, il va encore falloir enlever un bouton de la chemise (Niveau " + this.level + ") !");
    }
}

