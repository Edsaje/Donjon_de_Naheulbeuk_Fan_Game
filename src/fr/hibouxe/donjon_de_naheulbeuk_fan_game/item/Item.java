package fr.hibouxe.donjon_de_naheulbeuk_fan_game.item;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

/**
 * Classe parente représentant tout objet du jeu (Potion, Équipement, Trésor).
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Item {
    protected String name;
    protected String description;

    /**
     * Constructeur d'un objet.
     *
     * @param name        Nom de l'objet (ex: "Fiole de Soin")
     * @param description Description des effets de l'objet
     */
    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Utilise l'objet sur un personnage cible.
     *
     * @param target Le personnage qui bénéficie de l'objet
     */
    public void use(Character target) {
        System.out.println(target.getName() + " utilise " + this.name);
    }

    /** @return Nom de l'objet */
    public String getName() {
        return name;
    }

    /** @return Description de l'objet */
    public String getDescription() {
        return description;
    }
}
