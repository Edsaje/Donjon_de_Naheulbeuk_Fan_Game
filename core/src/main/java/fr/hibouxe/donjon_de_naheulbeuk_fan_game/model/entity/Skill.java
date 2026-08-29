package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity;

import java.io.Serializable;

/**
 * Reprsente une comptence spciale ou magique utilisable par un personnage.
 * Peut tre une attaque (ciblant un monstre) ou un soin (ciblant un alli).
 */
public abstract class Skill implements Serializable {
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

    /**
     * Excute la comptence. Doit tre surcharg par les comptences spcifiques.
     * @param user Le personnage qui utilise la comptence
     * @param team L'quipe (pour les soins de groupe)
     * @param target La cible de la comptence
     * @return Le rsultat de l'utilisation de la comptence
     */
    public abstract SkillResult execute(Character user, Team team, Character target);
}
