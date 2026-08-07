package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity;

import java.io.Serializable;

/**
 * Représente une compétence spéciale ou magique utilisable par un personnage.
 * Peut être une attaque (ciblant un monstre) ou un soin (ciblant un allié).
 */
public class Skill implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int cost;
    private String description;
    private boolean isHealing; // true si c'est un soin, false si c'est une attaque

    public Skill(String name, int cost, String description, boolean isHealing) {
        this.name = name;
        this.cost = cost;
        this.description = description;
        this.isHealing = isHealing;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public boolean isHealing() {
        return isHealing;
    }
}
