package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

/**
 * ReprÃ©sente la Magicienne dans la Compagnie de Naheulbeuk.
 * SpÃ©cialiste des sorts offensifs basÃ©s sur l'attaque magique et le Mana.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Magician extends Character {

    /**
     * Initialise la Magicienne avec ses statistiques de dÃ©part et sa ressource Mana.
     */
    public Magician() {
        super("La Magicienne", "Magicienne", 1, 5, 100, 2, 8, 3, 3, 12);
        this.resourceName = "Mana";
        this.maxResource = 100;
        this.currentResource = 100;
    }

    /**
     * ExÃ©cute le sort offensif Boule de Feu contre le monstre.
     * Consomme 4 points de Mana et inflige des dÃ©gÃ¢ts magiques.
     *
     * @param team   La compagnie de Naheulbeuk
     * @param target Le monstre ciblÃ© par le sort
     * @param menu   La vue principale du jeu (InjectÃ©e)
     */
    @Override
    public String useSpecialSkill(Team team, Character target) {
        int cost = 30;
        if (this.currentResource >= cost) {
            this.currentResource -= cost;
            int damage = Math.max(1, (this.getMagicAttack() * 2) - target.getMagicDefense());
            target.setHealthPoint(target.getHealthPoint() - damage);
            return this.name + " lance une BOULE DE FEU pas trop mal rÃ©ussie pour " + damage + " dÃ©gÃ¢ts !";
        } else {
            return this.name + " manque de Mana (" + this.currentResource + "/" + cost + ")...";
        }
    }
    @Override
    public void levelUp() {
        super.levelUp();
        this.healthPoint += 2;
        this.magicAttack += 4;
        this.defense += 1;
        this.magicDefense += 3;
        this.speed += 1;
        this.maxResource += 10;
        System.out.println(this.name + " apprend de nouveaux mots compliqués pour sa magie (Niveau " + this.level + ") !");
    }
}

