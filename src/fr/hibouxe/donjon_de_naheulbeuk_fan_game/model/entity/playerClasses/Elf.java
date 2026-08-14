package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.boss.Golem;

import java.util.Random;

/**
 * Représente l'Élfette dans la Compagnie de Naheulbeuk.
 * Héros spécialisé dans le soutien, les soins magiques et le Tir Précis (ou presque).
 *
 * @author Hibouxe
 * @version 2.0
 */
public class Elf extends Character {
    private Random random = new Random();

    /**
     * Initialise l'Élfette avec ses statistiques de départ et sa ressource Mana.
     * Possède "Soin Magique" et "Tir Précis (ou presque)" dès le niveau 1.
     */
    public Elf() {
        super("L'Elfe", "Elfe", 1, 6, 100, 6, 3, 5, 5, 20);
        this.resourceName = "Mana";
        this.maxResource = 100;
        this.currentResource = 100;
        this.skills.add(new Skill("Soin Magique", 15, "Rend 15 PV à un allié.", true));
        this.skills.add(new Skill("Tir Précis (ou presque)", 15, "Un tir à l'arc ajusté. Risque élevé de toucher un coéquipier !", false));
    }

    /**
     * Exécute les compétences spéciales de l'Elfe (Soin Magique ou Tir Précis).
     *
     * @param skill  La compétence utilisée
     * @param team   La compagnie de héros
     * @param target Le monstre affronté
     * @return Message de résultat
     */
    @Override
    public String useSpecialSkill(Skill skill, Team team, Character target) {
        if (skill.getName().equals("Soin Magique")) {
            int cost = skill.getCost();
            if (this.currentResource >= cost) {
                this.currentResource -= cost;

                if (target != null) {
                    int healAmount = 15;
                    target.setHealthPoint(target.getHealthPoint() + healAmount);
                    return this.name + " utilise ses compétences chirurgicales sur " + target.getName() + " et lui rend " + healAmount + " PV !";
                } else {
                    return this.name + " : ça va être compliqué là !";
                }
            } else {
                return this.name + " n'a pas assez de Mana (" + this.currentResource + "/" + cost + ") pour soigner !";
            }
        }

        if (skill.getName().equals("Tir Précis (ou presque)")) {
            int cost = skill.getCost();
            if (this.currentResource >= cost) {
                this.currentResource -= cost;
                int roll = random.nextInt(100);

                if (roll < 70) {
                    // 70% de Succès !
                    if (target instanceof Golem) {
                        target.setDefense(0);
                        int damage = Math.max(1, (this.getAttack() * 2));
                        target.setHealthPoint(target.getHealthPoint() - damage);
                        return this.name + " réussit l'impossible et plante sa flèche dans un ennemi ! La carapace d'acier se fissure (Défense = 0) et inflige " + damage + " dégâts !";
                    }

                    int damage = Math.max(1, (this.getAttack() * 2) - target.getDefense());
                    target.setHealthPoint(target.getHealthPoint() - damage);
                    return this.name + " réussit un Tir Précis (par pure chance) pour " + damage + " dégâts !";
                } else {
                    // 30% de Tir Allié (Friendly Fire) !
                    Character ally = team.getRandomMemberExcept(this);
                    if (ally != null) {
                        int allyDamage = 8;
                        ally.setHealthPoint(ally.getHealthPoint() - allyDamage);
                        return this.name + " vise soigneusement... mais sa flèche dévie et se plante dans le derrière de " + ally.getName() + " ! (-" + allyDamage + " PV à " + ally.getName() + ")";
                    } else {
                        return this.name + " coche sa flèche mais rate totalement sa cible !";
                    }
                }
            } else {
                return this.name + " n'a pas assez de Mana (" + this.currentResource + "/" + cost + ") !";
            }
        }

        return super.useSpecialSkill(skill, team, target);
    }

    @Override
    public void levelUp() {
        super.levelUp();
        this.healthPoint += 2;
        this.attack += 1;
        this.magicAttack += 3;
        this.defense += 1;
        this.magicDefense += 2;
        this.speed += 3;
        // menu.displayMessage(this.name + " gagne encore en charisme, il va encore falloir enlever un bouton de la chemise (Niveau " + this.level + ") !");
    }
}
