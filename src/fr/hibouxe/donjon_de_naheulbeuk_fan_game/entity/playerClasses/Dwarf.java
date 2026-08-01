package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

/**
 * ReprÃ©sente le Nain dans la Compagnie de Naheulbeuk.
 * Combatant rÃ©sistant qui accumule de la Rage au combat pour porter des coups puissants.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Dwarf extends Character {

    /**
     * Initialise le Nain avec ses statistiques de dÃ©part et sa ressource Rage.
     */
    public Dwarf() {
        super("Le Nain", "Nain", 1, 12, 0, 7, 0, 6, 6, 5);
        this.resourceName = "Rage";
        this.maxResource = 100;
        this.currentResource = 0;
    }

    /**
     * ExÃ©cute l'attaque lourde du Nain en consommant de la Rage.
     * Inflige des dÃ©gÃ¢ts physiques accrus.
     *
     * @param team   La compagnie de Naheulbeuk
     * @param target Le monstre ciblÃ© par le coup de hache
     */
    @Override
    public String useSpecialSkill(Team team, Character target) {
        int cost = 20; // Ã‰quilibrage : Un coup puissant coÃƒÂ»te 20 de Rage, et non plus 1.
        if (this.currentResource >= cost) { 
            this.currentResource -= cost; 
            int damage = Math.max(1, (int)(this.getAttack() * 1.5) - target.getDefense()); 
            target.setHealthPoint(target.getHealthPoint() - damage);
            return this.name + " hurle de rage et plante sa hache dans le " + target.getName() + " pour " + damage + " dÃ©gÃ¢ts !";
        } else {
            return this.name + " n'a pas assez de Rage (" + this.currentResource + "/" + cost + ") pour son attaque spÃ©ciale...";
        }
    }
    @Override
    public void levelUp() {
        super.levelUp();
        this.healthPoint += 6;
        this.attack += 2;
        this.defense += 3;
        this.magicDefense += 1;
        this.speed += 1;
        System.out.println(this.name + " chantonne : Je suis niveau " + this.level + " Tralalalalère (Niveau " + this.level + ") !");
    }
}

