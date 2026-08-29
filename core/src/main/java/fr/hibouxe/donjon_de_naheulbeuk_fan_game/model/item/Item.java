package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;

import java.io.Serializable;

/**
 * Classe parente reprsentant tout objet du jeu (Potion, quipement, Trsor).
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
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
     * @param target Le personnage qui bnficie de l'objet
     * @return true si l'objet a t utilis ou quip avec succs, false sinon.
     */
    public boolean use(Character target) {
        return true;
    }

    /**
     * @return Nom de l'objet
     */
    public String getName() {
        return name;
    }

    /**
     * @return Description de l'objet
     */
    public String getDescription() {
        return description;
    }
}
