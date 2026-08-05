package fr.hibouxe.donjon_de_naheulbeuk_fan_game.item;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

import java.io.Serializable;

/**
 * Classe parente représentant tout objet du jeu (Potion, Équipement, Trésor).
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
     * @param target Le personnage qui bénéficie de l'objet
     * @return true si l'objet a été utilisé ou équipé avec succès, false sinon.
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
