package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

/**
 * ReprÃ©sente l'Ogre dans la Compagnie de Naheulbeuk.
 * Colosse extrÃªmement rÃ©sistant possÃ©dant des points de vie et une dÃ©fense Ã©levÃ©s.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Ogre extends Character {

    /**
     * Initialise l'Ogre avec ses statistiques de dÃ©part et sa ressource Rage.
     */
    public Ogre() {
        super("L'Ogre", "Ogre", 1, 20, 0, 5, 0, 10, 5, 4);
        this.resourceName = "Rage";
        this.maxResource = 100;
        this.currentResource = 0;
    }

    /**
     * ExÃ©cute l'Ã©crasement massif de l'Ogre en consommant 15 points de Rage.
     * Ignore la dÃ©fense de la cible.
     *
     * @param team    La compagnie de Naheulbeuk
     * @param monster Le monstre ciblÃ©
     * @param menu    La vue principale du jeu (InjectÃ©e)
     */
    @Override
    public String useSpecialSkill(Team team, Character monster) {
        int cost = 30;
        if (this.currentResource >= cost) {
            this.currentResource -= cost;
            int damage = Math.max(1, (int)(this.getAttack() * 1.5) + 5);
            monster.setHealthPoint(monster.getHealthPoint() - damage);
            return this.name + " Ã‰CRASE le " + monster.getName() + " sous sa masse immense en ignorant sa dÃ©fense ! Inflige " + damage + " dÃ©gÃ¢ts !";
        } else {
            return this.name + " gronde mais n'a pas assez de Rage (" + this.currentResource + "/" + cost + ") !";
        }
    }
    @Override
    public void levelUp() {
        super.levelUp();
        this.healthPoint += 8;
        this.attack += 3;
        this.defense += 2;
        System.out.println(this.name + " Akala, zog zog, glozou bok ! (Niveau " + this.level + ") !");
    }
}

