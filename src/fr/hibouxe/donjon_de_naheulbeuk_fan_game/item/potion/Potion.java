package fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.potion;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

/**
 * Représente une potion de soin restaurant des points de vie (PV).
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Potion extends Item {
    private int healAmount;

    /**
     * Constructeur d'une potion de soin.
     *
     * @param name        Nom de la potion (ex: "Fiole de Soin")
     * @param description Description des effets
     * @param healAmout   Quantité de points de vie restaurés
     */
    public Potion(String name, String description, int healAmout) {
        super(name, description);
        this.healAmount = healAmout;
    }

    /**
     * Consomme la potion pour restaurer les points de vie du personnage cible.
     *
     * @param target Le personnage qui consomme la potion
     * @param menu   La vue principale du jeu (Injectée)
     */
    @Override
    public void use(Character target, Menu menu) {
        target.setHealthPoint(target.getHealthPoint() + healAmount);
        menu.displayMessage("\n" + target.getName() + " boit une potion et récupère +" + healAmount + " PV !");
    }

    /** @return La quantité de soins apportée par la potion */
    public int getHealAmout() {
        return healAmount;
    }
}
