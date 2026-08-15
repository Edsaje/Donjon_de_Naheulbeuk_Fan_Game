package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.usable.potion;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;

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
     * @return true car la potion a été consommée avec succès.
     */
    @Override
    public boolean use(Character target) {
        target.setHealthPoint(target.getHealthPoint() + healAmount);
        return true;
    }

    /**
     * @return La quantité de soins apportée par la potion
     */
    public int getHealAmout() {
        return healAmount;
    }
}
