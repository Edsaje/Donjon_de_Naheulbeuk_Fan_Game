package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.Item.*;

import java.util.Scanner;

public class Character {
    //attributs
    protected String name;
    protected String type; //classe du personnage
    protected int level;
    protected int healthPoint;
    protected int resourcePoint;
    protected int attack;
    protected int magicAttack;
    protected int defense;
    protected int magicDefense;
    protected String resourceName; // "Mana", "Rage" ou "Énergie"
    protected int currentResource; // Valeur actuelle (ex: 10)
    protected int maxResource;     // Valeur maximale (ex: 20)

    //constructeur
    public Character(String name, String type, int level, int healthPoint, int resourcePoint,int attack, int magicAttack, int defense, int magicDefense){
        this.name = name;
        this.type = type;
        this.level = level;
        this.healthPoint = healthPoint;
        this.resourcePoint = resourcePoint;
        this.attack = attack;
        this.magicAttack = magicAttack;
        this.defense = defense;
        this.magicDefense = magicDefense;
    }

    //méthodes

    public void useSpecialSkill(Team team, Character monster, Scanner keyboard) {
        System.out.println(this.name + "n'a pas appris de compétence spéciale, le nul !");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMagicDefense() {
        return magicDefense;
    }

    public void setMagicDefense(int magicDefense) {
        this.magicDefense = magicDefense;
    }

    public int getMagicAttack() {
        return magicAttack;
    }

    public void setMagicAttack(int magicAttack) {
        this.magicAttack = magicAttack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getResourcePoint() {
        return resourcePoint;
    }

    public int getManaPoint() {
        return resourcePoint;
    }

    public void setManaPoint(int resourcePoint) {
        this.resourcePoint = resourcePoint;
    }

    public int getHealthPoint() {
        return healthPoint;
    }

    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getResourceStatus() { //helper
        return resourceName + ": " + currentResource + "/" + maxResource;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", level=" + level +
                ", healthPoint=" + healthPoint +
                ", manaPoint=" + resourcePoint +
                ", attack=" + attack +
                ", magicAttack=" + magicAttack +
                ", defense=" + defense +
                ", magicDefense=" + magicDefense +
                '}';
    }
}
