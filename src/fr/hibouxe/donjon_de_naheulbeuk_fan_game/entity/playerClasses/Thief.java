package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

/**
 * ReprÃ©sente le Voleur dans la Compagnie de Naheulbeuk.
 * Aventurier agile spÃ©cialisÃ© dans les attaques rapides et la couardise.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Thief extends Character {

    /**
     * Initialise le Voleur avec ses statistiques de dÃ©part et sa ressource Ã‰nergie.
     */
    public Thief() {
        super("Le Voleur", "Voleur", 1, 5, 100, 3, 1, 5, 5, 16);
        this.resourceName = "Ã‰nergie";
        this.maxResource = 100;
        this.currentResource = 100;
    }

    /**
     * ExÃ©cute l'attaque sournoise dans le dos du Voleur en consommant 40 points d'Ã‰nergie.
     *
     * @param team    La compagnie de Naheulbeuk
     * @param monster Le monstre ciblÃ©
     * @param menu    La vue principale du jeu (InjectÃ©e)
     */
    @Override
    public String useSpecialSkill(Team team, Character monster) {
        int cost = 30; // cost changed to 30 for better usability
        if (this.currentResource >= cost) {
            this.currentResource -= cost;
            int damage = Math.max(1, (this.getAttack() * 2) - (monster.getDefense() / 2));
            monster.setHealthPoint(monster.getHealthPoint() - damage);
            return this.name + " se glisse dans l'ombre et porte une ATTAQUE SOURNOISE dÃ©vastatrice dans le dos ! Inflige " + damage + " dÃ©gÃ¢ts !";
        } else {
            return this.name + " n'a pas assez d'Ã‰nergie pour s'Ã©clipser (" + this.currentResource + "/" + cost + ") !";
        }
    }
    @Override
    public void levelUp() {
        super.levelUp();
        this.healthPoint += 3;
        this.attack += 3;
        this.defense += 1;
        this.magicDefense += 1;
        this.speed += 3;
        System.out.println(this.name + " arrive encore mieux à se dissimuler et fuir le combat ! (Niveau " + this.level + ") !");
    }
}

